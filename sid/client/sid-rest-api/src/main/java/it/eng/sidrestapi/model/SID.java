package it.eng.sidrestapi.model;

public class SID {

    private String address;
    private String pubKey;
    private String controller;
    private String created;
    private Integer status;

    public SID() {
    }


    public SID(String address, String pubKey, String controller, String created, Integer status) {
        this.address = address;
        this.pubKey = pubKey;
        this.controller = controller;
        this.created = created;
        this.status = status;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "SID{" +
                "address='" + address + '\'' +
                ", controller='" + controller + '\'' +
                ", created='" + created + '\'' +
                ", status=" + status +
                '}';
    }
}
