package it.eng.sidcommandline;

import com.beust.jcommander.JCommander;
import it.eng.sidcommandline.config.ApplicationConfig;
import it.eng.sidcommandline.config.SidRunner;
import it.eng.sidcommandline.utils.ChiperHandler;
import it.eng.sidcommandline.utils.WalletManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SidProcessor {
    private static final Logger logger = LogManager.getLogger(SidProcessor.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    private SidRunner sidRunner;

    public void process(String... args) {
        try {
            final JCommander jCommander = applicationConfig.getJCommander();
            jCommander.parse(args);
            sidRunner = applicationConfig.getRunner();
            if (sidRunner.isHelp()) {
                jCommander.usage();
            } else {
                executeOperation(sidRunner.getCommand());
            }
        } catch (Exception e) {
            logger.error("Error encountered in process with message: " + e.getMessage());
        }
    }

    private void executeOperation(String command) {
        WalletManager.verifyWalletPath();
        try {

            switch (command) {
                case "list":
                    WalletManager.list(sidRunner.getPassword());
                    break;
                case "stat":
                    WalletManager.stat(sidRunner.getPassword());
                    break;
                case "gen":
                    WalletManager.generate(sidRunner.getPassword());
                    break;
                case "show":
                    WalletManager.show(sidRunner.getPassword(), sidRunner.getAddress());
                    break;
            }
        } catch (Exception e) {
            logger.error("Error encountered in process with message: " + e.getMessage());
        }

    }
}