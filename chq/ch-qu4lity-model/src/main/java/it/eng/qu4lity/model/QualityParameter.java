package it.eng.qu4lity.model;

import java.util.Objects;

public class QualityParameter {
    private String contractPath;
    private Double minVal;
    private Double maxVal;

    public QualityParameter() {
    }
    

    public QualityParameter(String id, Double minVal, Double maxVal) {
        this.contractPath = id;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    public String getId() {
        return contractPath;
    }

    public void setId(String id) {
        this.contractPath = id;
    }

    public Double getMinVal() {
        return minVal;
    }

    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    public Double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualityParameter)) return false;
        QualityParameter that = (QualityParameter) o;
        return Objects.equals(contractPath, that.contractPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractPath);
    }
}
