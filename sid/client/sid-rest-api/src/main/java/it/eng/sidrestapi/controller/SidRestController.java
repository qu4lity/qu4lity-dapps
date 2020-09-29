package it.eng.sidrestapi.controller;


import io.swagger.annotations.Api;
import it.eng.sidrestapi.chaincode.HttpStatusCode;
import it.eng.sidrestapi.config.ApplicationStartup;
import it.eng.sidrestapi.crypto.ECDSA;
import it.eng.sidrestapi.exception.SIDClientException;
import it.eng.sidrestapi.model.Identity;
import it.eng.sidrestapi.model.Response;
import it.eng.sidrestapi.model.SID;
import it.eng.sidrestapi.model.crypto.FileWallet;
import it.eng.sidrestapi.service.IdentityServiceImpl;
import it.eng.sidrestapi.utils.ChiperUtils;
import it.eng.sidrestapi.utils.JsonHandler;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/identity")
@Api(value = "", tags = "SID API")
public class SidRestController {


    private static final Logger log = Logger.getLogger(SidRestController.class.getName());


    @Autowired
    IdentityServiceImpl identityService;


    @GetMapping(value = "/{addr}")
    public SID getIdentity(@PathVariable(name = "addr") String addr) {
        log.info("GetIdentity API starting....");
        Date date = new Date();
        final Identity identity;
        final Response response;
        Base64.Encoder encoder = Base64.getEncoder();
        response = identityService.getIdentity(addr, date);
        if (response.getCode() != HttpStatusCode.NOT_FOUND.getCode()) {
            try {
                identity = (Identity) JsonHandler.convertFromJson(response.getMessage(), Identity.class);
                final PublicKey ecPublicKey = ECDSA.hexTOPublicKey(identity.getBaseEntry().getpKeyBlob());
                SID sid = new SID(identity.getBaseEntry().getAddress(), encoder.encodeToString(ecPublicKey.getEncoded()), identity.getBaseEntry().getController(), identity.getBaseEntry().getCreated(), identity.getStatusEntry().getStatus());
                return sid;
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Identity not found"
        );
    }


//    @PostMapping(path = "/postController", consumes = MediaType.ALL_VALUE)
//    public String postController() throws Exception {
//        FileWallet fileWallet = searchIdentity("GbQWSF93eEoyoNV9iW4UZE37km7jerNunYNhFvzw7fxs", "engineering");
//        Base64.Decoder decoder = Base64.getDecoder();
//        KeyFactory keyFactory = KeyFactory.getInstance("EC");
//        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decoder.decode(fileWallet.getkeyPair().getPublicKey())));
//        PrivateKey decodedPrivateKey =
//                keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(fileWallet.getkeyPair().getPrivateKey())));
//
//        String controller = fileWallet.getAddress();
//        final Response postIdentity = identityService.postIdentity(decodedPrivateKey, publicKey, "ECDSA", true, "", "");
//        return postIdentity.getMessage();
//    }

    @PostMapping(path = "/", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public String postIdentity(@RequestParam(name = "pubKey") String pubKey, @RequestParam(name = "password") String password, @RequestParam(name = "ctrlAddr ") String controllerAddress) throws Exception {

        log.info("PostIdentity API starting....");
        FileWallet fileWallet = searchIdentity(controllerAddress, password);
        Base64.Decoder decoder = Base64.getDecoder();
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decoder.decode(pubKey)));
            final int keyLength = getKeyLength(publicKey);
//            if (keyLength != 256) {
//                log.severe("Error in PublicKey value. Make sure it is ECDSA type with a key length of 256 bits");
//                throw new ResponseStatusException(
//                        HttpStatus.INTERNAL_SERVER_ERROR, "PublicKey length must be 256 bit!"
//                );
//            }
        } catch (Exception e) {
            log.severe("Error in PublicKey value. Make sure it is ECDSA type with a key length of 256 bits ..." + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error in Public key value"
            );
        }


        PrivateKey decodedPrivateKey =
                keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(fileWallet.getkeyPair().getPrivateKey())));
        String controller = fileWallet.getAddress();
        String type = "";
        if (publicKey.getAlgorithm().equals("EC")) {
            type = "ECDSA";
        } else {
            type = "RSA";
        }
        final Response response = identityService.postIdentity(decodedPrivateKey, publicKey, type, true, "", controller);
        if (response.getCode() == HttpStatusCode.CONFLICT.getCode()) {
            log.severe(response.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Identity already present");
        } else if (response.getCode() == HttpStatusCode.UNAUTHORIZED.getCode()) {
            log.severe(response.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, response.getMessage());
        } else if (response.getCode() == HttpStatusCode.BAD_REQUEST.getCode()) {
            log.severe(response.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.getMessage());
        } else if (response.getCode() == HttpStatusCode.NOT_FOUND.getCode()) {
            log.severe(response.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        }
        Identity identity = (Identity) JsonHandler.convertFromJson(response.getMessage(), Identity.class);
        return identity.getBaseEntry().getAddress();

    }


    @PutMapping(path = "/{addr}/{op}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void editIdentity(@PathVariable(name = "addr") String addr, @PathVariable(name = "op") String op, @RequestParam(name = "password") String password, @RequestParam(name = "ctrlAddr") String controllerAddress) {
        if ("revoke".equals(op) || "activate".equals(op) || "suspend".equals(op)) {
            try {
                log.info(op + " API starting....");

                Response response = doEdit(addr, password, op, controllerAddress);
                if (response.getCode() == HttpStatusCode.CONFLICT.getCode()) {
                    log.severe(response.getMessage());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Identity already in that state");
                }
            } catch (SIDClientException e) {
                log.severe(e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
                );
            }
        } else {
            log.severe("Bad operation name in  PUT API!");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad operation name"
            );
        }
    }


    private Response doEdit(String addr, String password, String functionName, String controllerAddress) throws SIDClientException {
        Base64.Decoder decoder = Base64.getDecoder();
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
        FileWallet fileWallet = searchIdentity(controllerAddress, password);

//        final String walletString = FileUtils.readFileToString(new File(System.getProperty("user.home") + File.separator + "sid-wallet"), StandardCharsets.UTF_8);
//        final String decrypt = ChiperUtils.decrypt(password, walletString);
//        final FileWallet fileWallet = (FileWallet) JsonHandler.convertFromJson(decrypt, FileWallet.class);
        PrivateKey decodedPrivateKey = null;
        try {

            decodedPrivateKey =
                    keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(fileWallet.getkeyPair().getPrivateKey())));
        } catch (InvalidKeySpecException e) {
            log.severe(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password wrong, try whit different password");
        }
        String controller = fileWallet.getAddress();
        switch (functionName) {
            case "revoke":
                return identityService.revokeIdentity(addr, createGUID(), controller, decodedPrivateKey);
            case "activate":
                return identityService.activateIdentity(addr, createGUID(), controller, decodedPrivateKey);
            case "suspend":
                return identityService.suspendIdentity(addr, createGUID(), controller, decodedPrivateKey);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in operation name");


    }


    private static FileWallet searchIdentity(String controllerAddress, String psw) {


        //FIXME   FIXARE ERORRI QUI!!!!!!!


        String fileToString = null;
        try {
            fileToString = FileUtils.readFileToString(new File(ApplicationStartup.MAIN_DIRECTORY + File.separator + "sid-wallet"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.severe(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Identity wallet not found!");
        }

        String ciao = null;
        try {
            ciao = ChiperUtils.decrypt(psw, fileToString);
        } catch (SIDClientException e) {
            log.severe(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password wrong, try whit different password");
        }
        Collection<FileWallet> fileWallets = null;
        try {
            fileWallets = (Collection<FileWallet>) JsonHandler.convertFromJson(ciao, FileWallet.class, true);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }

        for (FileWallet fileWallet : fileWallets
        ) {
            if (fileWallet.getAddress().equals(controllerAddress)) {
                log.info("Controller identity extract correctly ");
                return fileWallet;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Controller not found in wallet with address:" + controllerAddress);
    }

    private static String createGUID() {
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[64];
        random.nextBytes(r);
        return Base64.getEncoder().encodeToString(r).replace("/", "");
    }

    private static String generateRandomString() {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    private static int getKeyLength(final PublicKey pk) {
        int len = -1;
        final ECPublicKey ecpriv = (ECPublicKey) pk;
        final java.security.spec.ECParameterSpec spec = ecpriv.getParams();
        if (spec != null) {
            len = spec.getOrder().bitLength(); // does this really return something we expect?
        } else {
            // We support the key, but we don't know the key length
            len = 0;
        }
        return len;
    }

}
