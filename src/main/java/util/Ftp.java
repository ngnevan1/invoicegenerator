package util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class Ftp {
    private static final String USERNAME = "invoice";
    private static final String PASSWORD = "test123";

    public static void upload(String parentName, String fileName, InputStream inputStream) {

        FTPClient ftpClient = new FTPClient();

        try {

            ftpClient.connect("103.7.8.26", 21);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            parentName = parentName.replaceAll("\\s+","");
            fileName = fileName.replaceAll("\\s+","");
            String dirname = "/" + parentName;
            ftpCreateDirectoryTree(ftpClient, dirname);
            String remoteFile = "/" + parentName + "/" + fileName;
            //String remoteFile = fileName;

            // APPROACH #1: uploads first file using an InputStream
            System.out.println("Start uploading file");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println(fileName + " is uploaded successfully.");
            } else {
                System.out.println(ftpClient.getReplyString());
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();

        } finally {

            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * utility to create an arbitrary directory hierarchy on the remote ftp server
     * @param client
     * @param dirTree  the directory tree only delimited with / chars.  No file name!
     * @throws Exception
     */
    private static void ftpCreateDirectoryTree( FTPClient client, String dirTree ) throws IOException {

        boolean dirExists = true;

        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] directories = dirTree.split("/");
        for (String dir : directories ) {
            if (!dir.isEmpty() ) {
                if (dirExists) {
                    dirExists = client.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!client.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                    if (!client.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                }
            }
        }
    }
}
