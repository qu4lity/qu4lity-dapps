package it.eng.sidrestapi.utils;


import it.eng.sidrestapi.exception.SIDClientException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FabricUtils {

//    private static final Logger log = LoggerFactory.getLogger(FabricUtils.class);


//    public static String readProperty(String propName) {
//        try {
//            Properties prop = new Properties();
//            String propFileName = HLFConfigProperties.CONFIG_PROPERTIES;
//            InputStream inputStream = FabricUtils.class.getClassLoader().getResourceAsStream(propFileName);
//            if (inputStream != null) {
//                prop.load(inputStream);
//            } else {
//                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
//            }
//            if (prop.containsKey(propName)) {
//                return prop.getProperty(propName);
//            } else {
//                throw new Exception("property file '" + propFileName + "' not found in the classpath");
//            }
//        } catch (Exception e) {
////            log.error(e.getMessage());
//        }
//        return null;
//    }

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

//    public static FabricUserInfo readIdentityProperty(String json) throws Exception {
//
//        return (FabricUserInfo) JsonHandler.convertFromJson(json, FabricUserInfo.class);
//
//
//    }

    public static String readIdentity(String folderPath) throws SIDClientException {

        Path path = Paths.get(folderPath);
        try (Stream<Path> walk = Files.walk(path)) {
            List<String> result = walk.filter(Files::isDirectory)
                    .map(Path::toString).collect(Collectors.toList());
            if (result.get(1).isEmpty()) {
                System.out.println("Error, impossible read identity name...");
                throw new SIDClientException("Error, impossible read identity name...");
            }
            String str = result.get(1);
//            str = str.replaceAll(File.separator, "/");
            int lastIndex = str.lastIndexOf(File.separator);
            str = str.substring(lastIndex + 1);
//            System.out.println("Identity read from wallet: " + str);
            System.out.println("Identity read from wallet: " + str);
            return str;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SIDClientException(e.getMessage());
        }
    }
//
//    public static void writeProperty(String propName, String propValue) {
//        try {
//            Properties prop = new Properties();
//            String propFileName = HLFConfigProperties.CONFIG_PROPERTIES;
//            InputStream inputStream = FabricUtils.class.getClassLoader().getResourceAsStream(propFileName);
//            OutputStream outputStream = new FileOutputStream("src/main/resources/config.properties");
//            prop.load(inputStream);
//            prop.setProperty(propName, propValue);
//            prop.store(outputStream, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
