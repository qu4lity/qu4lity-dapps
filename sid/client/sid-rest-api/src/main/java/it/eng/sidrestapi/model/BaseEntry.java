package it.eng.sidrestapi.model;


public class BaseEntry {

    private String address;
    private String controller;
    private String created;
    private String createdBy;
    private String pKeyBlob;
    private String keyType;
    private String details;

    public BaseEntry() {
    }

    public BaseEntry(String address, String controller, String created, String createdBy, String pKeyBlob, String keyType, String details) {
        this.address = address;
        this.controller = controller;
        this.created = created;
        this.createdBy = createdBy;
        this.pKeyBlob = pKeyBlob;
        this.keyType = keyType;
        this.details = details;
    }


    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getpKeyBlob() {
        return pKeyBlob;
    }

    public void setpKeyBlob(String pKeyBlob) {
        this.pKeyBlob = pKeyBlob;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "BaseEntry{" +
                "address='" + address + '\'' +
                ", controller='" + controller + '\'' +
                ", created='" + created + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", pKeyBlob='" + pKeyBlob + '\'' +
                ", keyType='" + keyType + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
