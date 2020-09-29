package it.eng.smbledger.fabric.model;

public class PkPreamble {

    private String domain;
    private String environment;
    private String process;

    public PkPreamble() {
    }

    public PkPreamble(String domain, String environment, String process) {
        this.domain = domain;
        this.environment = environment;
        this.process = process;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }


    @Override
    public String toString() {
        return "PkPreamble{" +
                "domain='" + domain + '\'' +
                ", environment='" + environment + '\'' +
                ", process='" + process + '\'' +
                '}';
    }
}
