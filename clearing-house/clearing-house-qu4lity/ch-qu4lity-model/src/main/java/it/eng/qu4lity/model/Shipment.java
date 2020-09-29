        package it.eng.qu4lity.model;


import java.util.List;
import java.util.Objects;
import java.util.UUID;

    public class Shipment {
    private UUID id;
    private UUID model;
    private List<Item> items;
    public Shipment() {
    }

    public Shipment(UUID id, UUID model, List<Item> items) {
        this.id = id;
        this.model = model;
        this.items = items;
    }

    public Shipment(UUID shUid, UUID qmUid) {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getModel() {
        return model;
    }

    public void setModel(UUID model) {
        this.model = model;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void additem(Item item) { this.items.add(item);}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shipment)) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(getId(), shipment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
