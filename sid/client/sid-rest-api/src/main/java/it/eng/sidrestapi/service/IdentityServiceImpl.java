package it.eng.sidrestapi.service;


import it.eng.sidrestapi.chaincode.Function;
import it.eng.sidrestapi.chaincode.HttpStatusCode;
import it.eng.sidrestapi.config.ApplicationStartup;
import it.eng.sidrestapi.config.HLFConfigPropertiesBean;
import it.eng.sidrestapi.crypto.ECDSA;
import it.eng.sidrestapi.crypto.RSA;
import it.eng.sidrestapi.exception.SIDClientException;
import it.eng.sidrestapi.model.BaseEntry;
import it.eng.sidrestapi.model.Identity;
import it.eng.sidrestapi.model.Response;
import it.eng.sidrestapi.model.StatusEntry;
import it.eng.sidrestapi.model.crypto.ECDSApublicKeyXY;
import it.eng.sidrestapi.model.crypto.Signature;
import it.eng.sidrestapi.utils.JsonHandler;
import it.eng.sidrestapi.utils.MethodUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Service
public class IdentityServiceImpl implements IdentityService {

    private static final Logger log = Logger.getLogger(IdentityServiceImpl.class.getName());
    private static final int TIMEOUT_VALUE = 5;

    private final SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String WALLET = "wallet";
    public static String CONFIG_PATH = "";

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    private HLFConfigPropertiesBean hlfConfigProperties;

    private Gateway.Builder gatewayBuilder;


    @Autowired
    public IdentityServiceImpl(HLFConfigPropertiesBean hlfConfigProperties,
                               ApplicationStartup applicationStartup) throws SIDClientException {
        applicationStartup.createWalletDirOnFileSystem();
        this.hlfConfigProperties = hlfConfigProperties;
        if (null == hlfConfigProperties) {
            this.hlfConfigProperties = new HLFConfigPropertiesBean();
        }
        ClassLoader classLoader = getClass().getClassLoader();
/*        WALLET_PATH = classLoader.getResource("wallet").getFile();
        WALLET_PATH = WALLET_PATH.substring(5);

        WALLET_PATH = WALLET_PATH.replace("!", "");

        String identity = FabricUtils.readIdentity(WALLET_PATH);
       this.hlfConfigProperties.setUserName(identity);*/
        connect();
    }

    private void connect() throws SIDClientException {
        try {
         /*   final ArrayList<String> strings = checkConfigParam();
            String walletString = strings.get(0);
            String userName = strings.get(1);
            Path walletPath = Paths.get(walletString);
            String configNetworkPath = WALLET_PATH + File.separator + hlfConfigProperties.getNetworkFilename();
            Path networkConfigPath = Paths.get(CONFIG_PATH);*/
            gatewayBuilder = Gateway.createBuilder();
            ClassLoader classLoader = getClass().getClassLoader();
            log.info("before gatewayBuilder.identity");
            gatewayBuilder.identity(createWalletFromFileSystem(), hlfConfigProperties.getUserName()).networkConfig(Paths.get(ApplicationStartup.WALLET_PATH + File.separator + "connection-org1.json"));
            log.info("after gatewayBuilder.identity!");
        } catch (Exception e) {
            log.info("P");
            log.severe("Error encountered in connecting to Blockchain with trace... " + e.getMessage());
            throw new SIDClientException(e);
        }
    }

    private Wallet createWalletFromFileSystem() throws SIDClientException {
        Wallet wallet = null;
        try {
            final Path path = Paths.get(ApplicationStartup.WALLET_PATH);
            log.info("PATH WALLET : " + path.toString());
            wallet = Wallet.createFileSystemWallet(path);
            log.info("WALLET CREATE");
        } catch (IOException e) {
            log.severe("Error encountered in connecting to Blockchain creating Wallet from fileSystem with trace... " + e.getMessage());
            throw new SIDClientException(e);
        }
        return wallet;
    }

    private Contract getContract() throws SIDClientException {
        try {
            Gateway gateway = gatewayBuilder.connect();
            Network network = gateway.getNetwork(hlfConfigProperties.getChannelName());
            return network.getContract(hlfConfigProperties.getChaincodeName());
        } catch (Exception e) {
            log.info("H");
            log.severe("Error encountered in connecting to Blockchain retrieving chaincode with trace... " + e.getMessage());
            throw new SIDClientException(e);
        }
    }


    private Future<Object> executeCallable(Contract contract, String... chaincodeArgs) {
        Callable<Object> task = () -> {

            if (chaincodeArgs[0].equals(Function.getIdentity.name())) {
                try {
                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2]);
                    return (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);

                } catch (ContractException e) {
                    final Collection<ProposalResponse> proposalResponses = e.getProposalResponses();
                    for (ProposalResponse response : proposalResponses
                    ) {
                        return (Response) JsonHandler.convertFromJson(response.getProposalResponse().getResponse().getMessage(), Response.class);
                    }
                    return e;
                }

            } else if (chaincodeArgs[0].equals(Function.postIdentity.name())) {
                try {
                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2]);
                    return (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);
                } catch (ContractException e) {
                    final Collection<ProposalResponse> proposalResponses = e.getProposalResponses();
                    for (ProposalResponse response : proposalResponses
                    ) {
                        return (Response) JsonHandler.convertFromJson(response.getProposalResponse().getResponse().getMessage(), Response.class);
                    }
                    return e;
                }
            } else {
                try {
                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2], chaincodeArgs[3], chaincodeArgs[4]);
                    return (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);
                } catch (ContractException e) {
                    final Collection<ProposalResponse> proposalResponses = e.getProposalResponses();
                    for (ProposalResponse response : proposalResponses
                    ) {
                        return (Response) JsonHandler.convertFromJson(response.getProposalResponse().getResponse().getMessage(), Response.class);
                    }
                    return e;
                }
            }
        };
            /*if (chaincodeArgs[0].equals(Function.getIdentity.name())) {
                try {
                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2]);
                    if (submit != null && submit.length > 0) {
                        return (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);
                    } else {
                        return null;
                    }
                } catch (ContractException e) {
                    log.severe(e.getMessage());
                    try {
                        final Response resp = (Response) JsonHandler.convertFromJson(e.getMessage(), Response.class);
                        return resp;
                    } catch (Exception exception) {
                        log.severe(exception.getMessage());
                        throw new SIDClientException(exception.getMessage());
                    }
                }
//                //FIXME return non error se non si trova!!!!
//                log.severe("Identity not found!");
//                throw new SIDClientException("Identity not found!");
            }
            //post
            else if (chaincodeArgs[0].equals(Function.postIdentity.name())) {
                try {

                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2]);

                    log.info("post qui!");
                    if (submit != null && submit.length > 0) {
                        final String s = new String(submit, StandardCharsets.UTF_8);
                        log.info("Post resp : " + s);
                        return (Response) JsonHandler.convertFromJson(s, Response.class);
                    }
                    log.info("G");
                    throw new ContractException("ERROR");
                } catch (ContractException e) {
                    log.severe("Contrac exec" + e.getMessage());
                    try {
                        final Response resp = (Response) JsonHandler.convertFromJson(e.getMessage(), Response.class);
                        return resp;
                    } catch (Exception exception) {
                        log.severe("gen exc" + exception.getMessage());
                        throw new SIDClientException(exception.getMessage());
                    }
                }
            }
            //Edit
            else {
                try {
                    final byte[] submit = contract.submitTransaction(chaincodeArgs[0], chaincodeArgs[1], chaincodeArgs[2], chaincodeArgs[3], chaincodeArgs[4]);
                    if (submit != null && submit.length > 0) {
                        return (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);
                    } else {
                        return null;
                    }
                } catch (ContractException e) {
                    log.severe(e.getMessage());
                    try {
                        final Response resp = (Response) JsonHandler.convertFromJson(e.getMessage(), Response.class);
                        return resp;
                    } catch (Exception exception) {
                        log.severe(exception.getMessage());
                        throw new Exception(exception.getMessage());
                    }
                }
            }*/


        ExecutorService executor = null;
        executor = Executors.newSingleThreadExecutor();
        return executor.submit(task);
//        finally {
//            Objects.requireNonNull(executor).shutdown();
//        }
    }

    @Override
    public Response postIdentity(PrivateKey privKeyController, PublicKey pKeyBlob, String keyType, boolean active, String details, String controller) {
        try {
            return doPostIdentity(privKeyController, pKeyBlob, keyType, active, details, controller);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private Response doPostIdentity(PrivateKey privKeyController, PublicKey pKeyBlob, String keyType, boolean active, String details, String controller) throws Exception {
        Contract contract = null;
        try {
            contract = getContract();
        } catch (SIDClientException sidClientException) {
            sidClientException.printStackTrace();
        }
        Identity identity = new Identity();
        BaseEntry baseEntry = new BaseEntry();
        StatusEntry statusEntry = new StatusEntry();
        Signature signature = null;
        ECDSApublicKeyXY ECDSApublicKeyXY = null;

        baseEntry.setController(controller);
        try {
            ECDSApublicKeyXY = ECDSA.getPublicKeyAsHex(pKeyBlob);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            signature = ECDSA.signMsg(JsonHandler.convertToJson(ECDSApublicKeyXY), privKeyController);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            baseEntry.setpKeyBlob(JsonHandler.convertToJson(ECDSApublicKeyXY));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        baseEntry.setDetails(details);
        baseEntry.setKeyType(keyType);
        baseEntry.setDetails(details);
        if (active) {
            statusEntry.setStatus(1);
        } else {
            statusEntry.setStatus(2);
        }
        statusEntry.setsubAddress(MethodUtils.createGUID());
        identity.setBaseEntry(baseEntry);
        identity.setStatusEntry(statusEntry);
        String json_identity = null;
        try {
            json_identity = JsonHandler.convertToJson(identity);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        log.info("Processing chaincode function...");
//            final byte[] submit = contract.createTransaction(Function.postIdentity.name()).submit(json_identity, JsonHandler.convertToJson(signature));
//            final byte[] submit = contract.submitTransaction(Function.postIdentity.name(), json_identity, JsonHandler.convertToJson(signature));
        final Future<Object> objectFuture = executeCallable(contract, Function.postIdentity.name(), json_identity, JsonHandler.convertToJson(signature));
        Response resp = (Response) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
//            Response response = (Response) JsonHandler.convertFromJson(new String(submit, StandardCharsets.UTF_8), Response.class);
        return resp;

//            final Future<Object> objectFuture = executeCallable(contract, Function.postIdentity.name(), json_identity, JsonHandler.convertToJson(signature));
//            final Response resp = (Response) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
//            final Response resp = (Response) JsonHandler.convertFromJson(respJson, Response.class);
//            return resp;
//            final Identity convertFromJson = (Identity) JsonHandler.convertFromJson(identityJson, Identity.class);
//            if (StringUtils.isNotBlank(convertFromJson.getBaseEntry().getAddress())) {
//                log.info("PostIdentity completed!");
//                return convertFromJson.getBaseEntry().getAddress();
//            }
//            log.severe("Error encountered in creating Identity model");
//            throw new SIDClientException("Post Identity error!");
//        } catch (ContractException e) {
//            log.info("B");
//            log.severe(e.getMessage());
//            try {
//                final Response resp = (Response) JsonHandler.convertFromJson(e.getMessage(), Response.class);
//                return resp;
//            } catch (Exception exception) {
//                log.info("C");
//                exception.printStackTrace();
//            }
//        } catch (SIDClientException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.getMessage();
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("D");
//        return null;
    }


    @Override
    public Response suspendIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws
            SIDClientException {

        return doEditIdenity(addr, guid, controller, privKeyController, Function.suspendIdentity.name());

    }


    @Override
    public Response activateIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws
            SIDClientException {
        return doEditIdenity(addr, guid, controller, privKeyController, Function.activateIdentity.name());

    }


    @Override
    public Response revokeIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws
            SIDClientException {
        return doEditIdenity(addr, guid, controller, privKeyController, Function.revokeIdentity.name());

    }

    private Response doEditIdenity(String addr, String subAddr, String controller, PrivateKey
            privKeyController, String functionName) throws SIDClientException {

        Contract contract = getContract();
        String msg = addr + subAddr;
        Signature signature = null;
        try {
            signature = ECDSA.signMsg(msg, privKeyController);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Processing chaincode function...");
        Response response = null;
        try {
            final Future<Object> objectFuture = executeCallable(contract, functionName, addr, subAddr, controller, JsonHandler.convertToJson(signature));
            response = (Response) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;


    }

    @Override
    public Response getIdentity(String addr, Date date) {
        return doGetIdentity(addr, date);
    }

    private Response doGetIdentity(String addr, Date date) {
        try {
            Contract contract = getContract();
            log.info("Processing chaincode function...");
            final Future<Object> objectFuture = executeCallable(contract, Function.getIdentity.name(), addr, toRFC3339(date));
            final Response response = (Response) objectFuture.get(TIMEOUT_VALUE, TimeUnit.SECONDS);
            return response;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }


    private String toRFC3339(Date d) {
        return rfc3339.format(d);
    }


}