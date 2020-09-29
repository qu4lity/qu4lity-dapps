package it.eng.qu4lity.model;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QualityModel {
    private UUID id;
    private Integer  version;
    private URL      contract;
    private List<QualityParameter> qualityParameters ;

    public QualityModel(UUID id, Integer version, URL contract, List<QualityParameter> qualityParameters) {
        this.id = id;
        this.version = version;
        this.contract = contract;
        this.qualityParameters = qualityParameters;
    }

    public QualityModel() {
    }

    public QualityModel(UUID uid, int i, URL url) {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public URL getContract() {
        return contract;
    }

    public void setContract(URL contract) {
        this.contract = contract;
    }

    public List<QualityParameter> getQualityParameter() {
        return qualityParameters;
    }

    public void setQualityParameter(List<QualityParameter> qualityParameters) {
        this.qualityParameters = qualityParameters;
    }
    public void addQualityParameter(QualityParameter qualityparameter) {
        this.qualityParameters.add(qualityparameter);
    }
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof QualityModel)) return false;
        QualityModel that = (QualityModel) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

