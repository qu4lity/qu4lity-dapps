package it.eng.sidrestapi.crypto;

import org.bitcoinj.core.Base58;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

public class Address {

    public static String calcolateAddr(String text) throws NoSuchAlgorithmException {


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return  Base58.encode(hash);

    }


    public static String readAddrFromFile(String pathFile) {

        {
            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines(Paths.get(pathFile), StandardCharsets.UTF_8)) {
                stream.forEach(contentBuilder::append);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentBuilder.toString();
        }
    }
//
//    public static void main(String[] args) throws NoSuchAlgorithmException {
//
//        String s = "ciao";
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hash = digest.digest(s.getBytes());
//        final String encode = Base64.getEncoder().encodeToString(hash);
//        System.out.println("Base64 : " + encode);
////        final byte[] hash = Hashing.sha256().hashString(s, StandardCharsets.UTF_8).asBytes();
////        System.out.println(Base58Codec.doEncode(hash));
//        final String s2 = Base58.encode(hash);
//        System.out.println("Base58 bitcoin : " + s2);
//
//        //        Hashing.sha256()
////                .hashString("your input", StandardCharsets.UTF_8).asBytes();
//    }
}
