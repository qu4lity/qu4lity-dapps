package it.eng.smbledger;

import com.beust.jcommander.JCommander;
import it.eng.smbledger.client.gdrive.GDriveManager;
import it.eng.smbledger.config.ApplicationConfig;
import it.eng.smbledger.config.HLFConfigProperties;
import it.eng.smbledger.config.SmbRunner;
import it.eng.smbledger.exception.FLedgerClientException;
import it.eng.smbledger.fabric.model.Message;
import it.eng.smbledger.fabric.service.MessageService;
import it.eng.smbledger.fabric.service.MessageServiceImpl;
import it.eng.smbledger.utils.FabricUtils;
import it.eng.smbledger.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

@Component
public class SmbProcessor {
    private static final Logger logger = LogManager.getLogger(SmbProcessor.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private HLFConfigProperties hlfConfigProperties;

    @Autowired
    private GDriveManager gDriveManager;

    private SmbRunner runner;
    private MessageService messageService;

    public void process(String... args) {
        try {
            final JCommander jCommander = applicationConfig.getJCommander();
            jCommander.parse(args);
            runner = applicationConfig.getRunner();
            hlfConfigProperties.setWalletPath(runner.getConfigFabricNetwork());
            messageService = new MessageServiceImpl(hlfConfigProperties);
//            System.out.println("Software version 1.0.0");
//            System.out.println("Copyright by Engineering Ingegneria Informatica");
//            System.out.println("_______________________________________________");
            if (runner.isHelp()) {
                jCommander.usage();
            }
            executeOperation();
        } catch (Exception e) {
            System.out.println("Error encountered in process with message: " + e.getMessage());
            logger.error("Error encountered in process with message: " + e.getMessage());
        }
    }

    private void executeOperation() throws IOException, FLedgerClientException {
        if (ApplicationConfig.POST_OPERATION.equals(runner.getOperation())) {
            doExecutePostMessage();
        } else if (ApplicationConfig.GET_OPERATION.equals(runner.getOperation())) {
            doExecuteGetMessage();
        } else if (ApplicationConfig.GET_OPERATION_LATEST_VERSION.equals(runner.getOperation())) {
            doExecuteGetLatestVersion();
        }
    }


    private void doExecutePostMessage() throws FLedgerClientException, IOException {
        final byte[] fileFromPath = FileUtils.readFileFromPath(runner.getFile());
        final BigDecimal sizeFile = FileUtils.calcolateSizeFile(fileFromPath);
        final String seal = FileUtils.calcolateHashFile(fileFromPath);
        com.google.api.services.drive.model.File file = gDriveManager.upload(new File(runner.getFile()), null);
        URL messageRefUrl = new URL(gDriveManager.getGUrl(file));
        logger.info("Start postMessage API to chaincode...");
        try {
            final String postMessage = messageService.postMessage(runner.getDomain(),
                    runner.getEnvironment(), runner.getProcess(), runner.getName(),
                    messageRefUrl, sizeFile, seal, null, null);
            logger.info("Record on ledger created: " + postMessage);
            logger.info("postMessage API complete!");
            System.out.println("postMessage API complete! Record write on Ledger!");
        } catch (Exception e) {
            gDriveManager.delete(file.getId());
            logger.info("file on Google Drive deleted!");
            System.out.println("file on Google Drive deleted!");
        }

    }

    private void doExecuteGetMessage() throws FLedgerClientException, IOException {
        logger.info("Start getMessage API to chaincode...");
        final Message message = messageService.getMessage(runner.getDomain(), runner.getEnvironment(), runner.getProcess(), runner.getName(), runner.getVersion());
        final String url = message.getmessageRef();
        String created = message.getCreated();
        final String name = message.getName();
        //TODO add name to file name if exist
        final Integer version = message.getVersion();
        if (url.isEmpty()) {
            logger.error("MessageRef from chaincode is empty!!");

            return;
        }
        if (created.isEmpty()) {
            created = LocalDateTime.now().toString();
        }
        created = created.replaceAll(":", "-");
        logger.info("getMessage API complete!");
        System.out.println("getMessage complete, MessageRef retrieve from the Ledger :" + url);
        logger.info("MessageRef :" + url);
        final String fileId = FileUtils.extractFileIdFromUrl(url);
        final ByteArrayOutputStream byteArrayOutputStream = gDriveManager.get(fileId);
        final String sealFromGdrive = FileUtils.calcolateHashFile(byteArrayOutputStream.toByteArray());
        boolean isCorrect = FabricUtils.checkSeal(sealFromGdrive, message.getSeal());
        if (!isCorrect) {
            System.out.println("ATTENTION! Corrupt file!!! File not downloaded!");
            throw new FLedgerClientException("ATTENTION! Corrupt file!!! File not downloaded!");
        } else {
            logger.info("Downloading file...");
            logger.info("Checksum correct!" + sealFromGdrive);
            FileUtils.createFile(byteArrayOutputStream, fileId, runner.getFile(), version, created);
            logger.info("File created as " + runner.getFile());
            System.out.println("File created as " + runner.getFile());
        }
    }

    private void doExecuteGetLatestVersion() throws FLedgerClientException, IOException {
        final Integer latestVersion = messageService.getLatestVersion(runner.getDomain(), runner.getEnvironment(), runner.getProcess(), runner.getName());
        System.out.println("Latest version loaded on the ledger is " + latestVersion.toString());
        logger.info("Latest version loaded on the ledger is " + latestVersion.toString());
        //        final File file = FileUtils.writeVersionInFile(latestVersion, runner.getFile());
//        com.google.api.services.drive.model.File fileGdrive = gDriveManager.upload(file, "text/plain");
//        URL url = new URL(gDriveManager.getGUrl(fileGdrive));
//        logger.info("MessageRef :" + url.toString());

    }

}
