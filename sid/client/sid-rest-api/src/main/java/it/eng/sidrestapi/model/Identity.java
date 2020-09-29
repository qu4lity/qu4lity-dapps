package it.eng.sidrestapi.model;

public class Identity {

    private BaseEntry baseEntry;
    private StatusEntry statusEntry;

    public Identity() {
    }

    public Identity(BaseEntry baseEntry, StatusEntry statusEntry) {
        this.baseEntry = baseEntry;
        this.statusEntry = statusEntry;
    }

    public BaseEntry getBaseEntry() {
        return baseEntry;
    }

    public void setBaseEntry(BaseEntry baseEntry) {
        this.baseEntry = baseEntry;
    }

    public StatusEntry getStatusEntry() {
        return statusEntry;
    }

    public void setStatusEntry(StatusEntry statusEntry) {
        this.statusEntry = statusEntry;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "baseEntry=" + baseEntry +
                ", statusEntry=" + statusEntry +
                '}';
    }
}
