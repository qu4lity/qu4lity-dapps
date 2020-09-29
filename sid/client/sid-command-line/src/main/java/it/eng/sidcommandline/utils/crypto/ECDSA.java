package it.eng.sidcommandline.utils.crypto;

import it.eng.sidcommandline.model.ECDSApublicKeyXY;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;

public class ECDSA {



    public static void writeKey(Object o, Path paths, boolean isPriv) throws IOException {
        String keyName = "";
        if (isPriv) {
            keyName = "priv.pem";
        } else {
            keyName = "pub.pem";
        }
        //FIXME
        JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(paths +"/"+ keyName));
        writer.writeObject(o);
        writer.close();
    }


    public static PrivateKey getKey(String filename) throws IOException, GeneralSecurityException {
        // Read key from file
        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line).append("\n");
        }
        br.close();
        final byte[] decode = Base64.getDecoder().decode(strKeyPEM.toString());
        return keyToValue(decode);
    }


    public static ECPublicKey hexTOPublicKey(BigInteger x, BigInteger y) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        java.security.spec.ECPoint w = new java.security.spec.ECPoint(x, y);
        ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256r1");
        KeyFactory fact = KeyFactory.getInstance("EC", "BC");
        ECCurve curve = params.getCurve();
        java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, params.getSeed());
        java.security.spec.ECParameterSpec params2 = EC5Util.convertSpec(ellipticCurve, params);
        java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(w, params2);
        return (ECPublicKey) fact.generatePublic(keySpec);
    }

    public static ECDSApublicKeyXY getPublicKeyAsHex(PublicKey publicKey) throws Exception {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        final ECParameterSpec params = ecPublicKey.getParams();
        ECPoint ecPoint = ecPublicKey.getW();
        byte[] affineXBytes = ecPoint.getAffineX().toByteArray();
        byte[] affineYBytes = ecPoint.getAffineY().toByteArray();
        String hexX = Hex.encodeHexString(affineXBytes);
        String hexY = Hex.encodeHexString(affineYBytes);
        ECDSApublicKeyXY ECDSApublicKeyXY = new ECDSApublicKeyXY(hexX, hexY);
//        ECDSApublicKeyXY ECDSApublicKeyXY = new ECDSApublicKeyXY(hexX, hexY, JsonHandler.convertToJson(params));
        return ECDSApublicKeyXY;
    }

    public static org.apache.commons.lang3.tuple.Pair<PrivateKey, PublicKey> generateKey() throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());

        KeyPair pair = keyGen.generateKeyPair();
        ECPrivateKey priv = (ECPrivateKey) pair.getPrivate();
//        ECPublicKey ecPublicKey = (ECPublicKey) pair.getPublic();
//        System.out.println(ecPublicKey.getParams().getCofactor());
//        System.out.println(ecPublicKey.getParams().getOrder().intValue());
//        System.out.println(ecPublicKey.getParams().getCurve());

        PublicKey pub = pair.getPublic();
        ImmutablePair<PrivateKey, PublicKey> pairs = new ImmutablePair<>(priv, pub);
//        writeKey(priv, paths, true);
//        writeKey(pub, paths, false);
        return pairs;
    }

    /*public static org.apache.commons.lang3.tuple.Pair<PrivateKey, PublicKey> generateKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        keyGen.initialize(256, random); //256 bit key size

        KeyPair pair = keyGen.generateKeyPair();
        ECPrivateKey priv = (ECPrivateKey) pair.getPrivate();
//        ECPublicKey ecPublicKey = (ECPublicKey) pair.getPublic();
//        System.out.println(ecPublicKey.getParams().getCofactor());
//        System.out.println(ecPublicKey.getParams().getOrder().intValue());
//        System.out.println(ecPublicKey.getParams().getCurve());

        PublicKey pub = pair.getPublic();
        ImmutablePair<PrivateKey, PublicKey> pairs = new ImmutablePair<>(priv, pub);
//        writeKey(priv, paths, true);
//        writeKey(pub, paths, false);
        return pairs;
    }*/


    private static ECPrivateKey keyToValue(byte[] pkcs8key)
            throws GeneralSecurityException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8key);
        KeyFactory factory = KeyFactory.getInstance("ECDSA");
        ECPrivateKey privateKey = (ECPrivateKey) factory.generatePrivate(spec);
        return privateKey;
    }

    public static boolean verifySignature(PublicKey pubKey, String msg, byte[] signature) throws Exception {
        byte[] message = msg.getBytes("UTF-8");
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initVerify(pubKey);
        ecdsa.update(message);
        return ecdsa.verify(signature);
    }

    public static PrivateKey base64ToPrivateKey(String encodedKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = factory.generatePrivate(spec);
        return privateKey;
    }

    public static PublicKey base64ToPublicKey(String encodedKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("EC");
        PublicKey publicKey = factory.generatePublic(spec);
        return publicKey;
    }

    public static String getPrivateKeyAsHex(PrivateKey privateKey) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
        byte[] privateKeyBytes = ecPrivateKey.getS().toByteArray();
        String hex = Hex.encodeHexString(privateKeyBytes);
//        System.out.println("priv hex " +hex);
        return hex;
    }


   /* public static ECDSApublicKeyXY getPublicKeyAsHex(PublicKey publicKey) {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        ECPoint ecPoint = ecPublicKey.getW();

        byte[] affineXBytes = ecPoint.getAffineX().toByteArray();
        byte[] affineYBytes = ecPoint.getAffineY().toByteArray();

        String hexX = Hex.encodeHexString(affineXBytes);
        String hexY = Hex.encodeHexString(affineYBytes);
//        System.out.println("x " +hexX);
//        System.out.println("y " +hexY);

        ECDSApublicKeyXY ECDSApublicKeyXY = new ECDSApublicKeyXY(hexX, hexY);
        return ECDSApublicKeyXY;
    }*/


    public static byte[] sign(String msg, PrivateKey priv) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");

        ecdsa.initSign(priv);

        byte[] strByte = msg.getBytes("UTF-8");
        ecdsa.update(strByte);

        byte[] realSig = ecdsa.sign();
        return realSig;
    }


    //https://stackoverflow.com/questions/48783809/ecdsa-sign-with-bouncycastle-and-verify-with-crypto
    public static BigInteger extractR(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
    }

    public static BigInteger extractS(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        int startS = startR + 2 + lengthR;
        int lengthS = signature[startS + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
    }

    public static PublicKey getPubKey(String pathPubKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        // Read key from file
        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(pathPubKey));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line).append("\n");
        }
        br.close();
        final byte[] decode = Base64.getDecoder().decode(strKeyPEM.toString());
        return keyToValuePub(decode);
    }

    private static PublicKey keyToValuePub(byte[] decode) throws NoSuchAlgorithmException, InvalidKeySpecException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode);
        KeyFactory factory = KeyFactory.getInstance("ECDSA");
        PublicKey publicKey = factory.generatePublic(spec);
        return publicKey;
    }
}
