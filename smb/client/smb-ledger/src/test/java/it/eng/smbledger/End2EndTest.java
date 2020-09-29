package it.eng.smbledger;

import it.eng.smbledger.client.gdrive.GDriveManager;
import it.eng.smbledger.config.ApplicationConfig;
import it.eng.smbledger.config.HLFConfigProperties;
import it.eng.smbledger.exception.FLedgerClientException;
import it.eng.smbledger.fabric.model.Message;
import it.eng.smbledger.fabric.service.MessageServiceImpl;
import it.eng.smbledger.utils.FileUtils;
import it.eng.smbledger.utils.JsonHandler;
import org.apache.commons.io.IOUtils;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class End2EndTest {

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private SmbProcessor smbProcessor;

    @Autowired
    private GDriveManager gDriveManager;

    @Autowired
    private HLFConfigProperties hlfConfigProperties;

    private MessageServiceImpl service;


    @Before
    public void setup() {
        try {
            service = new MessageServiceImpl(hlfConfigProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    -w /home/clod16/Scrivania/wallet -o POST -f /home/clod16/Scaricati/MAZDA.pdf -e messaging


    @Test
    public void testCommandLine() {
        String[] strArray1 = new String[20];
        strArray1[0] = "-w";
        strArray1[1] = "/home/clod16/Scrivania/wallet";
        strArray1[2] = "-o";
        strArray1[3] = "POST";
        strArray1[4] = "-f";
        strArray1[5] = "/home/clod16/Scaricati/MAZDA.pdf";
        strArray1[6] = "-d";
        strArray1[7] = "prima";
        strArray1[8] = "-e";
        strArray1[9] = "messaging";
//        strArray1[6] = "-s";
//        strArray1[7] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
        try {
            smbProcessor.process(strArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCommandLineWithAllArgs() {
        String[] strArray1 = new String[20];
        strArray1[0] = "-w";
        strArray1[1] = "/home/clod16/Documenti/Sviluppo/Java/SMART-INDUSTRY/secure-object-distribution-service/wallet";
        strArray1[2] = "-o";
        strArray1[3] = "POST";
        strArray1[4] = "-f";
        strArray1[5] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
//        strArray1[2] = "-p";
//        strArray1[3] = "publisher";
//        strArray1[4] = "-c";
//        strArray1[5] = "channel";
//        strArray1[6] = "-s";
//        strArray1[7] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
        try {
            smbProcessor.process(strArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ///    TEST API GOOGLE DRIVE


    @Test
    public void testUploadGDrive() {
        String[] strArray1 = new String[20];
        strArray1[0] = "-w";
        strArray1[1] = "/home/clod16/Documenti/Sviluppo/Java/SMART-INDUSTRY/secure-object-distribution-service/wallet";
        strArray1[2] = "-o";
        strArray1[3] = "POST";
        strArray1[4] = "-f";
        strArray1[5] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
//        strArray1[2] = "-p";
//        strArray1[3] = "publisher";
//        strArray1[4] = "-c";
//        strArray1[5] = "channel";
//        strArray1[6] = "-s";
//        strArray1[7] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";

//        com.google.api.services.drive.model.File file = gDriveManager.upload(new File(), information);
        try {
            smbProcessor.process(strArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUploadGDriveError() {
        String[] strArray1 = new String[20];
        strArray1[0] = "-w";
        strArray1[1] = "/home/clod16/Documenti/Sviluppo/Java/SMART-INDUSTRY/secure-object-distribution-service/wallet";
        strArray1[2] = "-o";
        strArray1[3] = "POST";
        strArray1[4] = "-f";
        strArray1[5] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
//        strArray1[2] = "-p";
//        strArray1[3] = "publisher";
//        strArray1[4] = "-c";
//        strArray1[5] = "channel";
//        strArray1[6] = "-s";
//        strArray1[7] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
        try {
            smbProcessor.process(strArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetGDrive() {
        String[] strArray1 = new String[20];
        strArray1[0] = "-w";
        strArray1[1] = "/home/clod16/Documenti/Sviluppo/Java/SMART-INDUSTRY/secure-object-distribution-service/wallet";
        strArray1[2] = "-o";
        strArray1[3] = "POST";
        strArray1[4] = "-f";
        strArray1[5] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";
//        strArray1[2] = "-p";
//        strArray1[3] = "publisher";
//        strArray1[4] = "-c";
//        strArray1[5] = "channel";
//        strArray1[6] = "-s";
//        strArray1[7] = "/home/clod16/Scaricati/ricevuta_spedizione.pdf";


        try {
            smbProcessor.process(strArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ///    TEST API CHAINCODE


    @Test(expected = FLedgerClientException.class)
    public void TestPostArgumentEmpty() throws FLedgerClientException {
        service.postMessage("", "", "", "", null, null, "", null, null);
    }

    /**
     * GetState must be all arguments for call the ledger service
     *
     * @throws FLedgerClientException
     */
    @Test(expected = FLedgerClientException.class)
    public void TestGetArgumentEmpty() throws FLedgerClientException {

        service.getMessage("", "", "", "", null);
        service.getMessage("dom", "", "", "name", null);
        service.getMessage("", "env", "", "", 1);
    }


    @Test
    public void TestPostCorrect() throws Exception {

        URL msgRef = new URL("http://msg.com");
        URL signBy = new URL("http://sign.com");
        URL confBy = new URL("http://conf.com");
        BigDecimal bigDecimal = new BigDecimal("10.5");
        final String result = service.postMessage("domain", "env", "proc", "name", msgRef, bigDecimal, "seal", signBy, confBy);

        if (!(result.isEmpty())) {
            final Message msg = (Message) JsonHandler.convertFromJson(result, Message.class);
            if (msg.getVersion() != null) {
                assert true;
            } else assert false;
        } else assert false;
    }

@Test
public void test1(){
        String str = "prova\"ciao";
   str =  str.replaceAll("\"", "/");
    System.out.println(str);

}
    @Test
    public void testGetGDriveAfterChaincodeGet() throws Exception {

        final Message message = service.getMessage("", "messaging", "", "", 0);
        final String url = message.getmessageRef();
        final String fileId = FileUtils.extractFileIdFromUrl(url);
        final ByteArrayOutputStream byteArrayOutputStream = gDriveManager.get(fileId);
        FileUtils.createFile(byteArrayOutputStream, fileId, "/home/clod16/Scrivania/", 0, "");

    }

    @Test
    public void testGetCorrect() throws Exception {

        URL msgRef = new URL("http://msg.com");
        URL signBy = new URL("http://sign.com");
        URL confBy = new URL("http://conf.com");
        BigDecimal bigDecimal = new BigDecimal("10.5");
        final String result = service.postMessage("domain", "env", "proc", "name", msgRef, bigDecimal, "seal", signBy, confBy);
        Message message = (Message) JsonHandler.convertFromJson(result, Message.class);
        final Message msg = service.getMessage("domain", "env", "proc", "name", message.getVersion());
        if (msg.toString().isEmpty()) {
            assert false;
        } else assert true;
    }

    @Test(expected = FLedgerClientException.class)
    public void testGetError() throws FLedgerClientException {
        Object o = new Attribute("name", "value");
        final Message objectDistribution = service.getMessage("ERROR", "ERROR", "ERROR", "ERROR", -1);
    }


}
