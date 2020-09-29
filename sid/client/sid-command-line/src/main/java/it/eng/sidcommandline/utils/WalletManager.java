package it.eng.sidcommandline.utils;

import it.eng.sidcommandline.config.ApplicationConfig;
import it.eng.sidcommandline.model.FileWallet;
import it.eng.sidcommandline.utils.crypto.ChiperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class WalletManager {

    private static final Logger logger = LogManager.getLogger(WalletManager.class);

    public static void generate(String psw) {
        try {
            ChiperHandler.createWallet(psw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void list(String psw) {
        try {
            final Collection<FileWallet> fileWallets = openWallet(psw);
            for (FileWallet fileWallet : fileWallets
            ) {
                logger.info("Address: " + fileWallet.getAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void show(String psw, String addr) {
        try {
            final Collection<FileWallet> fileWallets = openWallet(psw);
            for (FileWallet fileWallet : fileWallets
            ) {
                if (addr.equals(fileWallet.getAddress())) {
                    logger.info("\n Address: " + fileWallet.getAddress() + "\n Public Key: " + fileWallet.getkeyPair().getPublicKey() + "\n Key type: " + fileWallet.getkeyPair().getKeyType());
                    return;
                }
            }
            logger.error("Identity not found with address: " + addr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void stat(String psw) {
        try {
            final Collection<FileWallet> fileWallets = openWallet(psw);
            logger.info("Identity presents in your wallet are: " + fileWallets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Collection<FileWallet> openWallet(String psw) throws Exception {
        Path path = Paths.get(ApplicationConfig.WALLET_PATH + File.separator + "sid-wallet");

        if (Files.exists(path)) {
            String walletString = new String(Files.readAllBytes(path));
            final String decrypt = ChiperUtils.decrypt(psw, walletString);
            Collection<FileWallet> fileWallets = (Collection<FileWallet>) JsonHandler.convertFromJson(decrypt, FileWallet.class, true);
            return fileWallets;
        } else {
            throw new Exception("Wallet not found!");
        }
    }

    public static void verifyWalletPath() {

        String envName = "WALLET_PATH";
        try {
            final String getenv = System.getenv(envName);
            if (getenv == null || getenv.isEmpty()) {
                ApplicationConfig.WALLET_PATH = System.getProperty("user.home");
                logger.info("Environment Variable not found... use " + ApplicationConfig.WALLET_PATH);
            } else {
                ApplicationConfig.WALLET_PATH = getenv;
//                logger.info(ApplicationConfig.WALLET_PATH + " path");
            }
        } catch (Exception ignored) {

        }

//        if(System.getenv("WALLET_PATH").isEmpty()){
//            ApplicationConfig.WALLET_PATH = System.getProperty("user.home");
//            logger.info("Environment Variable not found... use " + ApplicationConfig.WALLET_PATH + "to save wallet!");
//        }
//        else {
//            ApplicationConfig.WALLET_PATH = System.getenv("WALLET_PATH");
//            logger.info("Set " + ApplicationConfig.WALLET_PATH + " for wallet path!");
//        }

    }
}
