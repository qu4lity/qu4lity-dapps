package it.eng.qu4lity.model;

import java.util.List;
import java.util.Objects;

public class ItemAssessment {
    private String       id;
    private List<Double> values;

    public ItemAssessment(String id) {
        this.id = id;
    }

    public ItemAssessment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemAssessment)) return false;
        ItemAssessment that = (ItemAssessment) o;
        return getId().equals(that.getId()) &&
                Objects.equals(getValues(), that.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValues());
    }
}

