package it.eng.sidcommandline.utils;

import it.eng.sidcommandline.SidProcessor;
import it.eng.sidcommandline.config.ApplicationConfig;
import it.eng.sidcommandline.model.ECDSApublicKeyXY;
import it.eng.sidcommandline.model.FileWallet;
import it.eng.sidcommandline.model.KeyPair;
import it.eng.sidcommandline.utils.crypto.ChiperUtils;
import it.eng.sidcommandline.utils.crypto.ECDSA;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChiperHandler {

    private static final Logger logger = LogManager.getLogger(ChiperHandler.class);

//    public static void main(String[] args) throws Exception {

//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.initialize(2048);
//        java.security.KeyPair kp = kpg.generateKeyPair();
//        Key pub = kp.getPublic();
//        Key pvt = kp.getPrivate();


//    logger.info(createGUID() + ":" +createGUID().length());
//        createWallet("ciao");
//        final String s = FileUtils.readFileToString(new File(System.getProperty("user.home") + File.separator + "sid-wallet"), StandardCharsets.UTF_8);
//        final String ciao = ChiperUtils.decrypt("ciao", s);
//        Collection<FileWallet> fileWallets = (Collection<FileWallet>) JsonHandler.convertFromJson(ciao, FileWallet.class, true);
//        logger.info(ciao);
//    }


    public static String createGUID() {
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[64];
        random.nextBytes(r);
        return Base64.getEncoder().encodeToString(r).replace("/", "");
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


    public static void createWallet(String psw) throws Exception {

        final Pair<PrivateKey, PublicKey> privateKeyPublicKeyPair = ECDSA.generateKey();
        Base64.Encoder encoder = Base64.getEncoder();

        String publicKeyStr = encoder.encodeToString(privateKeyPublicKeyPair.getRight().getEncoded());
        String privateKeyStr = encoder.encodeToString(privateKeyPublicKeyPair.getKey().getEncoded());
        ECDSApublicKeyXY ecdsApublicKeyXY;

        ecdsApublicKeyXY = ECDSA.getPublicKeyAsHex(privateKeyPublicKeyPair.getRight());
        ECDSA.getPrivateKeyAsHex(privateKeyPublicKeyPair.getKey());
        final String pkeyJson = JsonHandler.convertToJson(ecdsApublicKeyXY);
        final String addr = ChiperUtils.calcolateAddr(pkeyJson);
//        final String addr = ZipHandler.calcolateAddr(Arrays.toString(privateKeyPublicKeyPair.getRight().getEncoded()));


        Path path = Paths.get(ApplicationConfig.WALLET_PATH + File.separator + "sid-wallet");

        if (Files.exists(path)) {
//            String walletString = Files.readString(Path.of(System.getProperty("user.home") + File.separator + "sid-wallet"));
            String walletString = new String(Files.readAllBytes(Paths.get(ApplicationConfig.WALLET_PATH + File.separator + "sid-wallet")));

            String decrypt;
            try {

                decrypt = ChiperUtils.decrypt(psw, walletString);
            } catch (Exception e) {
                logger.error("Wrong password! Try again.");
                throw new Exception(e);
            }
            logger.info("Wallet exists! Add new Identity...");
            Collection<FileWallet> fileWallets = (Collection<FileWallet>) JsonHandler.convertFromJson(decrypt, FileWallet.class, true);
            FileWallet fileWallet1 = new FileWallet(addr, new KeyPair(publicKeyStr, privateKeyStr, "ECDSA"));
            fileWallets.add(fileWallet1);
            final String json = JsonHandler.convertToJson(fileWallets);
            final String encrypt = ChiperUtils.encrypt(psw, json);
            logger.info("Address created : " + addr);
            logger.info("Publickey created : " + publicKeyStr);

            writeFileCrypted(encrypt, addr);
        } else {
            final String modelFile = createModelFile(addr, publicKeyStr, privateKeyStr, "ECDSA");
            final String encrypt = ChiperUtils.encrypt(psw, modelFile);
            logger.info("Address created : " + addr);
            logger.info("Publickey created : " + publicKeyStr);
            writeFileCrypted(encrypt, addr);
        }

    }

//    public static String encrypt(String plainText, Key secretKey)
//            throws Exception {
//        byte[] plainTextByte = plainText.getBytes();
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptedByte = cipher.doFinal(plainTextByte);
//        Base64.Encoder encoder = Base64.getEncoder();
//        String encryptedText = encoder.encodeToString(encryptedByte);
//        return encryptedText;
//    }
//
//    public static String decrypt(String encryptedText, Key secretKey)
//            throws Exception {
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] encryptedTextByte = decoder.decode(encryptedText);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
//        String decryptedText = new String(decryptedByte);
//        return decryptedText;
//    }

    public static String createModelFile(String address, String publicKey, String privateKey, String ecdsa) throws Exception {

        List<FileWallet> fileWalletList = new ArrayList<>();
        FileWallet fileWallet = new FileWallet(address, new KeyPair(publicKey, privateKey, ecdsa));
        fileWalletList.add(fileWallet);
//        fileWalletList.add(fileWallet);
        return JsonHandler.convertToJson(fileWalletList);
//        return JsonHandler.convertToJson(new FileWallet(address, new KeyPair(publicKey, privateKey, ecdsa)));

    }


    private static Key stringToSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = {
                (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
        };

        // Iteration count
        int count = 20;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, count, 128 * 8);
        Key key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);
        return key;
    }

    private static String secretKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    private static void writeFileCrypted(String text, String fileName) throws IOException {


        FileUtils.writeStringToFile(new File(ApplicationConfig.WALLET_PATH + File.separator + "sid-wallet"), text, StandardCharsets.UTF_8);

    }
}
