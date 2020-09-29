package it.eng.smbledger.client.gdrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import it.eng.smbledger.config.ApplicationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * @author Antonio Scatoloni on 21/05/2020
 **/
@Service
public class GDriveManager {
    private static final Logger logger = LogManager.getLogger(GDriveManager.class);

    @Autowired
    private ApplicationConfig config;

    @Value("${gdrive.mimeType}")
    private String mimeType;

    public File upload(java.io.File file, String info) throws IOException {
        File gFile = doUpload(file, info);
        return gFile;
    }

    public ByteArrayOutputStream get(String fileId) throws IOException {
        // String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        config.getDriveService().files().get(fileId);
        config.getDriveService().files().get(fileId)
                .executeMediaAndDownloadTo(outputStream);
        return outputStream;
    }

    public String getGUrl(File file) {
        //The url is something like this and file id is present in this pattern "/d/XXXXXXXX/" for almost all GoogleDrive/Docs links:
        String url = "https://drive.google.com/file/d/" + file.getId() + "/view";
        System.out.println("Your file is available on Gdrive at: " + url);
        logger.info("Your file is available on Gdrive at: " + url);
        return url;
    }

    private File doUpload(java.io.File fileOrigin, String info) throws IOException {
        if (info == null) info = mimeType;
        Drive driveService = config.getDriveService();
        File fileMetadata = new File();
        fileMetadata.setName(fileOrigin.getName());
        FileContent mediaContent = new FileContent(info, fileOrigin);
        File file = driveService.files().create(fileMetadata, mediaContent)
                // .setFields("id")
                .execute();
        System.out.println("File uploaded on GDrive!!");
        logger.info("File uploaded in GDrive!!");
        return file;
    }

    public void delete(String fileId) throws IOException {
        // String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
        config.getDriveService().files().delete(fileId).execute();
    }
}
