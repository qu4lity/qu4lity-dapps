package it.eng.smbledger.config;

import com.beust.jcommander.JCommander;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import it.eng.smbledger.client.gdrive.GDriveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Antonio Scatoloni on 21/05/2020
 **/

@Configuration
public class ApplicationConfig {

    public static final String POST_OPERATION = "POST";
    public static final String GET_OPERATION = "GET";
    public static final String GET_OPERATION_LATEST_VERSION ="VERSION";
    public static  final String VERSION_FILE_NAME = "latestVersion.txt";

    @Autowired
    HLFConfigProperties hlfConfigProperties;

    @Bean
    @Scope("singleton")
    public SmbRunner getRunner() {
        SmbRunner runner = new SmbRunner();
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

    /*@Bean
    @Scope("singleton")
    public MessageService getMessageService() {
        MessageService messageService = null;
        try {
            //TODO Read from INPUT only 1st time!!!
            messageService = new MessageServiceImpl(hlfConfigProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageService;
    }*/

    @Bean
    @Scope("singleton")
    public Drive getDriveService() {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Drive service = new Drive.Builder(HTTP_TRANSPORT,
                    GDriveConfig.JSON_FACTORY, GDriveConfig.getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(GDriveConfig.APPLICATION_NAME)
                    .build();
            return service;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
