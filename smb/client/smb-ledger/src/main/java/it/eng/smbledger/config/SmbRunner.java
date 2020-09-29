package it.eng.smbledger.config;

import com.beust.jcommander.Parameter;
import lombok.Data;

/**
 * @author Antonio Scatoloni on 21/05/2020
 **/

@Data
public class SmbRunner {

    @Parameter(names = {"-w", "--wallet",}, required = true, description = "Absolute path of Wallet")
    private String configFabricNetwork;

    @Parameter(names = {"-o", "--operation",}, required = true, description = "Type of operation GET/POST")
    private String operation;

    @Parameter(names = {"-f", "--file",}, description = "Absolute path of file")
    private String file;

    @Parameter(names = {"-d", "--domain",}, description = "String for domain")
    private String domain;

    @Parameter(names = {"-e", "--environment",}, description = "String for environment")
    private String environment;

    @Parameter(names = {"-p", "--process",}, description = "String for process")
    private String process;

    @Parameter(names = {"-n", "--name",}, required = true, description = "String for name")
    private String name;

    @Parameter(names = {"-v", "--version",}, description = "Number for version")
    private Integer version;

    @Parameter(names = {"-h", "--help",}, help = true, description = "Help")
    private boolean help;
}
