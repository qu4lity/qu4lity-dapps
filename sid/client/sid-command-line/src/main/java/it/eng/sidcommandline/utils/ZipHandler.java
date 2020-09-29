package it.eng.sidcommandline.utils;

import it.eng.sidcommandline.model.ECDSApublicKeyXY;
import it.eng.sidcommandline.utils.crypto.ECDSA;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bitcoinj.core.Base58;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ZipHandler {

    public static void crateZip(String psw) {
        try {
            final String HOME_DIR = System.getProperty("user.home");

            //FIXME
            final Pair<PrivateKey, PublicKey> keyPair = ECDSA.generateKey();
            ECDSApublicKeyXY ecdsApublicKeyXY = ECDSA.getPublicKeyAsHex(keyPair.getValue());
            writeKey(keyPair.getKey(), true);
            writeKey(keyPair.getValue(), false);
            createAddressFile(ecdsApublicKeyXY);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            ZipFile zipFile = new ZipFile(System.getProperty("user.home") + File.separator + "zip_wallet.zip", psw.toCharArray());
            zipFile.addFile(new File(System.getProperty("user.home") + File.separator + "pub.pem"), zipParameters);
            zipFile.addFile(new File(System.getProperty("user.home") + File.separator + "priv.pem"), zipParameters);
            zipFile.addFile(new File(System.getProperty("user.home") + File.separator + "address.txt"), zipParameters);
            FileUtils.deleteQuietly(new File(System.getProperty("user.home") + File.separator + "pub.pem"));
            FileUtils.deleteQuietly(new File(System.getProperty("user.home") + File.separator + "priv.pem"));
            FileUtils.deleteQuietly(new File(System.getProperty("user.home") + File.separator + "address.txt"));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void extractPubKey(String password) throws ZipException {
        new ZipFile(System.getProperty("user.home") + File.separator + "zip_wallet.zip", password.toCharArray()).extractAll(System.getProperty("user.home"));
    }

    private static void createAddressFile(ECDSApublicKeyXY ecdsApublicKeyXY) throws Exception {
        try (PrintWriter out = new PrintWriter(System.getProperty("user.home") + File.separator + "address.txt")) {
            out.println(calcolateAddr(JsonHandler.convertToJson(ecdsApublicKeyXY)));
        }

    }


    public static String calcolateAddr(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return Base58.encode(hash);

    }

    public static void writeKey(Object o, boolean isPriv) throws IOException {
        String keyName = "";
        if (isPriv) {
            keyName = "priv.pem";
        } else {
            keyName = "pub.pem";
        }
        //FIXME
        JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(System.getProperty("user.home") + File.separator + keyName));
        writer.writeObject(o);
        writer.close();
    }
}
