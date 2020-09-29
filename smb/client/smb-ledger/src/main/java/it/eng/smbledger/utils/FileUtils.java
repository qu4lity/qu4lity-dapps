package it.eng.smbledger.utils;


import it.eng.smbledger.exception.FLedgerClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    private static final Logger logger = LogManager.getLogger(FileUtils.class);
    private static final String HASHING_ALGORITHM = "SHA-256";

    public static byte[] readFileFromPath(String path) throws FLedgerClientException {

        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Error reading file, check path or filename!!");
            throw new FLedgerClientException("Error reading file, check path or filename!!");
        }

    }

    public static BigDecimal calcolateSizeFile(byte[] file) throws FLedgerClientException {

        final double sizeInBytes = file.length;
        double fileSizeInKB = sizeInBytes / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;

        logger.info("Size of File : " + fileSizeInMB + "mb");
        BigDecimal bigDecimal = new BigDecimal(fileSizeInMB);
        bigDecimal.round(new MathContext(4, RoundingMode.HALF_UP));
        return bigDecimal;

    }

    public static String readFolderName(Path folderPath) throws FLedgerClientException {

        try (Stream<Path> walk = Files.walk(folderPath)) {
            List<String> result = walk.filter(Files::isDirectory)
                    .map(Path::toString).collect(Collectors.toList());
            if (result.get(1).isEmpty()) {
                System.out.println("Error, impossible read identity name...");
                throw new FLedgerClientException("Error, impossible read identity name...");
            }
            String str = result.get(1);
//            str = str.replaceAll(File.separator, "/");
            int lastIndex = str.lastIndexOf(File.separator);
            str = str.substring(lastIndex + 1);
//            System.out.println("Identity read from wallet: " + str);
            logger.info("Identity read from wallet: " + str);
            return str;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new FLedgerClientException(e.getMessage());
        }
    }

    public static File writeVersionInFile(Integer version, String pathname) throws FLedgerClientException {

        try {
            String pathnameFile = pathname + File.separator;
//            System.out.println("pathname : " + pathnameFile);
            logger.info("pathname : " + pathnameFile);
            File versionFile = new File(pathnameFile + "version.txt");
            FileWriter myWriter = new FileWriter(versionFile);
            myWriter.write(version.toString());
            myWriter.close();
            System.out.println("Successfully wrote  version.txt  in " + pathnameFile);
            logger.info("Successfully wrote  version.txt  in " + pathnameFile);
            return versionFile;
        } catch (IOException e) {
            System.out.println("Error : " +e.getMessage());
            throw new FLedgerClientException(e.getMessage());
        }

    }

    public static String calcolateHashFile(byte[] file) throws FLedgerClientException {
        MessageDigest messageDigest = null;
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASHING_ALGORITHM);
            byte[] encodedHash = digest.digest(file);
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Hashing of Payload with NO Algorithm available to perform hashing: " + e.getMessage());
        }
        //messageDigest.update(file);
        return new String(messageDigest.digest());
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static String readAndMoveFile(String from, String to) throws FLedgerClientException {
        try {
            Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            return to;
        } catch (IOException e) {
            throw new FLedgerClientException(e.getMessage());
        }
    }

    public static String extractFileIdFromUrl(String url) {
        url = url.replaceAll("\"", "");
        url = url.substring(0, url.length() - 5);
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static void createFile(ByteArrayOutputStream byteArrayOutputStream, String fileId, String
            pathFile, Integer version, String data) throws FLedgerClientException, IOException {
        FileOutputStream fos = null;
        try {
//            String pathname = pathFile + File.separator + fileId + "_v" + version.toString() + "_" + data;
//            System.out.println("pathname : " + pathname);
            fos = new FileOutputStream(new File(pathFile));
            byteArrayOutputStream.writeTo(fos);
        } catch (IOException e) {
            System.out.println("Create file error, check path or filename!");
            throw new FLedgerClientException("Create file error, check path or filename!");
        } finally {
            fos.close();
        }
    }


    public static String checkSlashInPath(String path) {

        if (path.endsWith(File.separator)) {
            return path.substring(path.length() - 1);
        }
        return path;
    }
//
//    public static void main(String[] args) {
//        String url = "\"https://drive.google.com/file/d/1zGiW7f2owL7_8iG7pCAmgoFc1uCbSL8f/view\"";
//        System.out.println(extractFileIdFromUrl(url));
//    }


}
