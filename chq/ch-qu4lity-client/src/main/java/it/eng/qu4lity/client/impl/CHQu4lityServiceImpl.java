package it.eng.qu4lity.client.impl;

import it.eng.qu4lity.client.CHQu4lityService;
import it.eng.qu4lity.client.config.HLFConfigProperties;
import it.eng.qu4lity.client.config.HLFConfigPropertiesBean;
import it.eng.qu4lity.client.event.ChaincodeEventCapture;
import it.eng.qu4lity.client.utils.FabricUtils;
import it.eng.qu4lity.model.QualityAssessment;
import it.eng.qu4lity.model.QualityModel;
import it.eng.qu4lity.model.Shipment;
import it.eng.qu4lity.model.utils.JsonHandler;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;
import org.hyperledger.fabric.sdk.ChaincodeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Vector;

public class CHQu4lityServiceImpl implements CHQu4lityService {

    private static final Logger log = LoggerFactory.getLogger(CHQu4lityServiceImpl.class);
    private static final String EDIT_API_CHAINCODE = "editRecordQu4lity";
    private static final String GET_API_CHAINCODE = "retrieveRecordQu4lity";

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    private HLFConfigProperties hlfConfigProperties;
    private Gateway.Builder gatewayBuilder;
    Vector<ChaincodeEventCapture> chaincodeEvents;


    public CHQu4lityServiceImpl() throws Exception {
        hlfConfigProperties = new HLFConfigPropertiesBean();
        chaincodeEvents = new Vector<>();
        ChaincodeEventListener chaincodeEventListener = new ChaincodeEventListener() {
            @Override
            public void received(String handle, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {
                {
                    chaincodeEvents.add(new ChaincodeEventCapture(handle, blockEvent, chaincodeEvent));
                }
                System.out.println("RECEIVED CHAINCODE EVENT with handle: " + handle + ", chaincodeId: " + chaincodeEvent.getChaincodeId() + ", chaincode event name: " + chaincodeEvent.getEventName() + ", transactionId: " + chaincodeEvent.getTxId() + ", event Payload: " + new String(chaincodeEvent.getPayload()));
            }
        };
        connect();
    }

    public CHQu4lityServiceImpl(HLFConfigProperties hlfConfigProperties) throws Exception {
        this.hlfConfigProperties = hlfConfigProperties;
        if (null == hlfConfigProperties) {
            this.hlfConfigProperties = new HLFConfigPropertiesBean();
        }
        chaincodeEvents = new Vector<>();
        connect();
    }

    private void connect() throws Exception {
        String walletPathProp = hlfConfigProperties.getWalletPath();
        Path walletPath = Paths.get(walletPathProp);
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
        Path networkConfigFile = Paths.get(walletPathProp, hlfConfigProperties.getNetworkFilename());

        final String cert = FabricUtils.readLineByLine(walletPathProp, hlfConfigProperties.getCertFilename());
        byte[] keyBytes = Files.readAllBytes(Paths.get(walletPathProp, hlfConfigProperties.getKeystoreFilename()));

        final PrivateKey key = FabricUtils.getPrivateKeyFromBytes(keyBytes);
        Wallet.Identity user = Wallet.Identity.createIdentity(hlfConfigProperties.getOrganizationName(), cert, key);
        String userName = hlfConfigProperties.getUserName();
        wallet.put(userName, user);
        gatewayBuilder = Gateway.createBuilder()
                .identity(wallet, userName)
                .networkConfig(networkConfigFile);
    }


    @Override
    public void editRecordQu4lity(Object o) throws Exception {
        Contract contract = getContract();
        byte[] createRecordQu4lities = contract.createTransaction("editRecordQu4lity").submit(JsonHandler.convertToJson(o), o.getClass().getName());
        log.info(new String(createRecordQu4lities, StandardCharsets.UTF_8));

    }

    @Override
    public Object retrieveRecordQu4lity(String key, Object o) throws Exception {
        Contract contract = getContract();
        final byte[] retrieveTransactions = contract.createTransaction("retrieveRecordQu4lity").submit(key, o.getClass().getName());
        String retrTransactionString = new String(retrieveTransactions, StandardCharsets.UTF_8);
        String[] parts = retrTransactionString.split("=");
        String objectString = parts[0];
        String typeString = parts[1];
        if (typeString.equals(QualityModel.class.getName())) {
            return JsonHandler.convertFromJson(objectString, QualityModel.class);
        } else if (typeString.equals(Shipment.class.getName())) {
            return JsonHandler.convertFromJson(objectString, Shipment.class);
        } else if (typeString.equals(QualityAssessment.class.getName())) {
            return JsonHandler.convertFromJson(objectString, QualityAssessment.class);
        }
        return new Exception();
    }


    private Contract getContract() throws Exception {
        try {
            Gateway gateway = gatewayBuilder.connect();
            Network network = gateway.getNetwork(hlfConfigProperties.getChannelName());
            return network.getContract(hlfConfigProperties.getChaincodeName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}