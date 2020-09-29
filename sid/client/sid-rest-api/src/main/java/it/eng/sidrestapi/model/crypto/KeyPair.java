package it.eng.sidrestapi.model.crypto;


public class KeyPair {

    private String publicKey;
    private String privateKey;
    private String keyType;

    public KeyPair() {
    }

    public KeyPair(String publicKey, String privateKey, String keyType) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.keyType = keyType;
    }

    public KeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}
