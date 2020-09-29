package it.eng.sidrestapi.config;

import it.eng.sidrestapi.utils.MethodUtils;
import net.lingala.zip4j.ZipFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * @author Antonio Scatoloni on 04/09/2020
 **/

@Component
public class ApplicationStartup {

    private static final Logger log = Logger.getLogger(ApplicationStartup.class.getName());
    public static  String MAIN_DIRECTORY = ""; //TODO Not the BEST SOLUTION IMHO!!!
    public static  String WALLET = "";
    public static  String WALLET_PATH = "";
    public static  String WALLET_PATH_ZIP = "";
    public static  String ENV_NAME = "WALLET_PATH";


    public void createWalletDirOnFileSystem() {

        MethodUtils.verifyWalletPath();
        WALLET = "wallet";
        WALLET_PATH = MAIN_DIRECTORY + File.separator + WALLET;
        WALLET_PATH_ZIP = WALLET_PATH + ".zip";
        final Path pathToWallet = Paths.get(WALLET_PATH);
        if (Files.notExists(pathToWallet)) {
            final Path pathToZip = Paths.get(WALLET_PATH_ZIP);
            log.info(pathToZip.toString());
            final InputStream resource = this.getClass().getResourceAsStream(File.separator + WALLET + ".zip");
            log.info(resource.toString());
            try {
                Files.copy(resource, pathToZip, StandardCopyOption.REPLACE_EXISTING);
                new ZipFile(pathToZip.toFile())
                        .extractAll(Paths.get(MAIN_DIRECTORY).toString());
                Files.delete(pathToZip);
            } catch (IOException e) {
                log.severe("Error moving " + WALLET + ".zip to " + WALLET_PATH);
            }
        }
    }

//    public static void main(String[] args) {
//
//        new ApplicationStartup().createWalletDirOnFileSystem();
//    }
}