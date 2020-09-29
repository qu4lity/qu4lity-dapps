package it.eng.sidcommandline.config;

import com.beust.jcommander.JCommander;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;




@Configuration
public class ApplicationConfig {


    public static String WALLET_PATH = "";

    @Bean
    @Scope("singleton")
    public SidRunner getRunner() {
        SidRunner runner = new SidRunner();
        return runner;
    }



    @Bean
    @Scope("singleton")
    public JCommander getJCommander() {
        final JCommander jCommander = JCommander.newBuilder()
                .addObject(getRunner())
                .build();
        return jCommander;
    }




}
