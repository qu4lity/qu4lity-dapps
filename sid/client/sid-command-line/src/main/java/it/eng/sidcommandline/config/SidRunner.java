package it.eng.sidcommandline.config;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class SidRunner {


    @Parameter(names = {"-c", "--command",}, help = true,required = true, description = "gen , list , show , stat")
    private String command;

    @Parameter(names = {"-p", "--password",}, help = true, required = true, description = "Password for personal wallet")
    private String password;

    @Parameter(names = {"-a", "--address",}, help = true, description = "Identity address")
    private String address;

    @Parameter(names = {"-h", "--help",}, help = true, description = "Help")
    private boolean help;


//    @Parameter(names = {"-list", "--list",}, help = true, required = true, description = "List personal address")
//    private String list;
//
//    @Parameter(names = {"-show", "--show",}, help = true, required = true, description = "Password for personal wallet")
//    private String show;
//
//    @Parameter(names = {"-stat", "--password",}, help = true, required = true, description = "Password for personal wallet")
//    private String stat;
//    @Parameter(names = {"-gen", "--password",}, help = true, required = true, description = "Password for personal wallet")
//    private String gen;
}
