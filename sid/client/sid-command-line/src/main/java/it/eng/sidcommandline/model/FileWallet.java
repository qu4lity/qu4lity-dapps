package it.eng.sidcommandline.model;

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

    @Override
    public String toString() {
        return "FileWallet{" +
                "address='" + address + '\'' +
                ", keyPair=" + keyPair +
                '}';
    }
}
