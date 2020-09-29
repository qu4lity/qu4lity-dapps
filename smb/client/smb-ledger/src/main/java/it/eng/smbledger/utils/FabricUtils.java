package it.eng.smbledger.utils;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.stream.Stream;

public class FabricUtils {

    private static final Logger log = LoggerFactory.getLogger(FabricUtils.class);

    public static String readLineByLine(String dirPath, String certFileName) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(dirPath, certFileName), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();
    }


    public static PrivateKey getPrivateKeyFromBytes(byte[] data) throws NoSuchAlgorithmException,
            IOException, InvalidKeySpecException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
        Security.addProvider(new BouncyCastleProvider());
        return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getPrivateKey(pemPair);
    }


    public static void writeProperty(String walletPath) {
        try {
            Properties prop = new Properties();
            OutputStream outputStreamt = new FileOutputStream("src/main/resources/application.properties");
            prop.setProperty("fabric.walletPath", walletPath);
            prop.store(outputStreamt, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSeal(String sealFromGdrive, String sealFromLedger) {
        return sealFromLedger.equals(sealFromGdrive);
    }
}
