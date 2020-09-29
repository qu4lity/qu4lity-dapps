package it.eng.sidrestapi.model.crypto;


public class FileWallet {

    private String address;
    private KeyPair keyPair;

    public FileWallet() {
    }

    public FileWallet(String address, KeyPair keyPair) {
        this.address = address;
        this.keyPair = keyPair;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public KeyPair getkeyPair() {
        return keyPair;
    }

    public void setkeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
