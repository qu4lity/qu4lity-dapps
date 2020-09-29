package it.eng.sidrestapi.utils;

import it.eng.sidrestapi.config.ApplicationStartup;
import it.eng.sidrestapi.crypto.ECDSA;
import it.eng.sidrestapi.crypto.RSA;
import it.eng.sidrestapi.model.BaseEntry;
import it.eng.sidrestapi.model.Identity;
import it.eng.sidrestapi.model.Response;
import it.eng.sidrestapi.model.StatusEntry;
import it.eng.sidrestapi.model.crypto.ECDSApublicKeyXY;
import it.eng.sidrestapi.model.crypto.Signature;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MethodUtils {

    private final SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

//    public static void main(String[] args) throws Exception {
//        Response response = new Response(500, "Marshal error");
//        System.out.println(JsonHandler.convertToJson(response));
//    }

    public static void verifyWalletPath() {

        try {
            final String getenv = System.getenv(ApplicationStartup.ENV_NAME);
            if (getenv == null || getenv.isEmpty()) {
                ApplicationStartup.MAIN_DIRECTORY = System.getProperty("user.home");
                System.out.println("Environment Variable not found... use " + ApplicationStartup.MAIN_DIRECTORY);
            } else {
                ApplicationStartup.MAIN_DIRECTORY = getenv;
                System.out.println(ApplicationStartup.MAIN_DIRECTORY + " path");
            }
        } catch (Exception ignored) {

        }
    }


    public static String createGUID() {
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

    private String toRFC3339(Date d) {
        return rfc3339.format(d);
    }


    private String createStructIdentityJson(PrivateKey privateKey, PublicKey pKeyBlob, String keyType, boolean active, String details, String controller) throws Exception {

        Identity identity = new Identity();
        BaseEntry baseEntry = new BaseEntry();
        StatusEntry statusEntry = new StatusEntry();
        Signature signature = new Signature();
        ECDSApublicKeyXY ECDSApublicKeyXY;

        baseEntry.setController(controller);
        ECDSApublicKeyXY = ECDSA.getPublicKeyAsHex(pKeyBlob);
        signature = ECDSA.signMsg(JsonHandler.convertToJson(ECDSApublicKeyXY), privateKey);

        byte[] byte_pubkey = pKeyBlob.getEncoded();
        String str_pubkey_blob = Base64.getEncoder().encodeToString(byte_pubkey);
        final String blobSignature = RSA.sign(privateKey, str_pubkey_blob);
        final String addrControlSign = RSA.sign(privateKey, "2VdneepQjreroWr3XypHKce1EmsrNhMiyHPY55KZhBcj");
        baseEntry.setAddress("");
//        baseEntry.setpKeyBlob(blobSignature);
        baseEntry.setKeyType(keyType);
        baseEntry.setDetails(details);
        if (active) {
            statusEntry.setStatus(1);
        } else {
            statusEntry.setStatus(2);
        }
        identity.setBaseEntry(baseEntry);
        identity.setStatusEntry(statusEntry);
        return JsonHandler.convertToJson(identity);
    }
}
