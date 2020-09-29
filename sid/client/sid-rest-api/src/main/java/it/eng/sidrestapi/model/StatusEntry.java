package it.eng.sidrestapi.model;


public class StatusEntry {

    private String address;         // (PK)
    private String subAddress;        // (PK)
    private String validFrom;
    private String updatedBy;
    private Integer status;         // 1: ACTIVE, 2: SUSPENDED, 3: REVOKED

    
    public StatusEntry() {
    }

    public StatusEntry(String address, String subAddress, String validFrom, String updatedBy, Integer status) {
        this.address = address;
        this.subAddress = subAddress;
        this.validFrom = validFrom;
        this.updatedBy = updatedBy;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getsubAddress() {
        return subAddress;
    }

    public void setsubAddress(String subAddress) {
        this.subAddress = subAddress;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusEntry{" +
                "address='" + address + '\'' +
                ", subAddress=" + subAddress +
                ", validFrom='" + validFrom + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", status=" + status +
                '}';
    }
}
