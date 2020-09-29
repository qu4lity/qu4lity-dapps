package it.eng.smbledger.fabric.model;

import java.math.BigDecimal;

public class Message {

    private PkPreamble pkPreamble;  // (PK)
    private String name;            // (PK)
    private Integer version;        // (PK)
    private String created;
    private String createdBy;
    private String signedBy;
    private String seal; //HASH o FIRMA
    private String confidentialFor;
    private String messageRef;
    private BigDecimal messageSize;


    public Message() {
    }

    public Message(PkPreamble pkPreamble, String name, Integer version, String created, String createdBy, String signedBy, String seal, String confidentialFor, String messageRef, BigDecimal messageSize) {
        this.pkPreamble = pkPreamble;
        this.name = name;
        this.version = version;
        this.created = created;
        this.createdBy = createdBy;
        this.signedBy = signedBy;
        this.seal = seal;
        this.confidentialFor = confidentialFor;
        this.messageRef = messageRef;
        this.messageSize = messageSize;
    }

    public PkPreamble getPkPreamble() {
        return pkPreamble;
    }

    public void setPkPreamble(PkPreamble pkPreamble) {
        this.pkPreamble = pkPreamble;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getConfidentialFor() {
        return confidentialFor;
    }

    public void setConfidentialFor(String confidentialFor) {
        this.confidentialFor = confidentialFor;
    }

    public String getmessageRef() {
        return messageRef;
    }

    public void setmessageRef(String messageRef) {
        this.messageRef = messageRef;
    }

    public BigDecimal getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(BigDecimal messageSize) {
        this.messageSize = messageSize;
    }

    @Override
    public String toString() {
        return "Message{" +
                "pkPreamble=" + pkPreamble +
                ", name='" + name + '\'' +
                ", messageRef='" + messageRef + '\'' +
                '}';
    }

}
