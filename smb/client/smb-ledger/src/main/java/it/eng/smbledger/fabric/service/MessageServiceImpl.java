package it.eng.smbledger.fabric.service;


import it.eng.smbledger.config.ApplicationConfig;
import it.eng.smbledger.config.HLFConfigProperties;
import it.eng.smbledger.exception.FLedgerClientException;
import it.eng.smbledger.fabric.chaincode.Function;
import it.eng.smbledger.fabric.model.Message;
import it.eng.smbledger.fabric.model.PkPreamble;
import it.eng.smbledger.utils.FileUtils;
import it.eng.smbledger.utils.JsonHandler;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileDescriptor;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.*;

public class MessageServiceImpl implements MessageService {

    @Autowired
    private HLFConfigProperties hlfConfigProperties;

    private Gateway.Builder gatewayBuilder;
    private static final int TIMEOUT_VALUE = 15;

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public MessageServiceImpl() throws Exception {
        connect();
    }

    public MessageServiceImpl(String walletAbsolutePath) throws Exception {
        hlfConfigProperties.setWalletPath(walletAbsolutePath);
        connect();
    }


    public MessageServiceImpl(HLFConfigProperties hlfConfigProperties) throws Exception {
        this.hlfConfigProperties = hlfConfigProperties;
        connect();
    }

    private void connect() throws FLedgerClientException {
        try {

            Path walletPath = Paths.get(hlfConfigProperties.getWalletPath());
            Wallet wallet = Wallet.createFileSystemWallet(walletPath);
            String configNetworkPath = hlfConfigProperties.getWalletPath() + File.separator + hlfConfigProperties.getNetworkFilename();
            Path networkConfigPath = Paths.get(configNetworkPath);
            gatewayBuilder = Gateway.createBuilder();
            gatewayBuilder.identity(wallet, FileUtils.readFolderName(walletPath)).networkConfig(networkConfigPath);
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }

    private Contract getContract() throws FLedgerClientException {
        try {
            Gateway gateway = gatewayBuilder.connect();
            Network network = gateway.getNetwork(hlfConfigProperties.getChannelName());
            return network.getContract(hlfConfigProperties.getChaincodeName());
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }

    private Future<Object> executeCallable(Contract contract, String... chaincodeArgs) throws FLedgerClientException {
        Callable<Object> task = () -> {
            if (chaincodeArgs[0].equals(Function.getMessage.name())) {
                byte[] submit;
                if (chaincodeArgs.length == 6) {
                    // with version
                    submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2], chaincodeArgs[3], chaincodeArgs[4], chaincodeArgs[5]);
                } else {
                    // without version
                    submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2], chaincodeArgs[3], chaincodeArgs[4]);
                }
                if (submit != null && submit.length > 0) {
//                    System.out.println(new String(submit, StandardCharsets.UTF_8));
                    return (Message) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Message.class);
                }
                throw new FLedgerClientException("Ledger ERROR: Message not found!");
            }
            if (chaincodeArgs[0].equals(Function.getLatestVersion.name())) {
                final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2], chaincodeArgs[3], chaincodeArgs[4]);
                if (submit != null && submit.length > 0) {
//                    System.out.println(new String(submit, StandardCharsets.UTF_8));
                    return Integer.parseInt(new String(submit, StandardCharsets.UTF_8));
                }
                throw new FLedgerClientException("Ledger ERROR: Version not found!");
            } else {
                final byte[] submit = contract.submitTransaction(chaincodeArgs[0], (chaincodeArgs[1]));
//                System.out.println(new String(submit, StandardCharsets.UTF_8));
                return new String(submit, StandardCharsets.UTF_8);
            }
        };
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            return executor.submit(task);
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        } finally {
            Objects.requireNonNull(executor).shutdown();
        }
    }

    @Override
    public String postMessage(String domain, String environment, String process, String name, URL messageRef, BigDecimal messageSize, String seal, URL signedBy, URL confidentialFor) throws FLedgerClientException {

        try {

            Contract contract = getContract();
            Message message = new Message();
            PkPreamble pkPreamble = new PkPreamble();
            pkPreamble.setDomain(domain);
            pkPreamble.setEnvironment(environment);
            pkPreamble.setProcess(process);
            message.setName(name);
            message.setmessageRef(messageRef.toString());
            message.setMessageSize(messageSize);
            message.setConfidentialFor(JsonHandler.convertToJson(confidentialFor));
            message.setSignedBy(JsonHandler.convertToJson(signedBy));
            message.setPkPreamble(pkPreamble);
//            if(seal.isEmpty()) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//                messageDigest.update(message.toString().getBytes());
//                seal =  new String(messageDigest.digest());
//            }
            message.setSeal(seal);
            final String json = JsonHandler.convertToJson(message);
            final Future<Object> objectFuture = executeCallable(contract, Function.postMessage.name(), json);
            return (String) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }

    @Override
    public Message getMessage(String domain, String environment, String process, String name, Integer version) throws FLedgerClientException {
        try {
            Contract contract = getContract();
            Future<Object> objectFuture = null;
            if (version == null)
                objectFuture = executeCallable(contract, Function.getMessage.name(), nullToEmpty(domain), nullToEmpty(environment), nullToEmpty(process), nullToEmpty(name));
            else
                objectFuture = executeCallable(contract, Function.getMessage.name(), nullToEmpty(domain), nullToEmpty(environment), nullToEmpty(process), nullToEmpty(name), version.toString());
            return (Message) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }

    //TODO
    @Override
    public Integer getLatestVersion(String domain, String environment, String process, String name) throws FLedgerClientException {

        try {
            Contract contract = getContract();
            final Future<Object> objectFuture = executeCallable(contract, Function.getLatestVersion.name(), nullToEmpty(domain), nullToEmpty(environment), nullToEmpty(process), nullToEmpty(name));
            return (Integer) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }


    private String nullToEmpty(Object value) {
        if (null == value) return "";
        return value.toString();
    }
}