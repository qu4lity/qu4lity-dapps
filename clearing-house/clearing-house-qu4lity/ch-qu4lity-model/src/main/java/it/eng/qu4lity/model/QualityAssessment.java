package it.eng.qu4lity.model;


import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QualityAssessment {
    private UUID id;
    private UUID shipment;
    private List<ItemAssessment> itemsAssessment;

    public QualityAssessment() {
    }

    public QualityAssessment(UUID id, UUID shipment) {
        this.id = id;
        this.shipment = shipment;
    }

    public QualityAssessment(UUID id, UUID shipment, List<ItemAssessment> itemsAssessment) {
        this.id = id;
        this.shipment = shipment;
        this.itemsAssessment = itemsAssessment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getShipment() {
        return shipment;
    }

    public void setShipment(UUID shipment) {
        this.shipment = shipment;
    }

    public List<ItemAssessment> getItemsAssessment() {
        return itemsAssessment;
    }

    public void setItemsAssessment(List<ItemAssessment> itemsAssessment) {
        this.itemsAssessment = itemsAssessment;
    }

    public void addItemAssessment(ItemAssessment itemAssessment) {this.itemsAssessment.add(itemAssessment);}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualityAssessment)) return false;
        QualityAssessment that = (QualityAssessment) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
