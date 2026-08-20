package ru.autotestframework.ftp_steps.ftp;

import static ru.autotestframework.Constants.TEMP_FTP_FOLDER;
import static ru.autotestframework.util.Validator.checkDownloadFolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.core.exception.ExecutionException;

/**
 * Ftp client.
 */
@Slf4j
public class FtpClient {

    private final FileLoader fileLoader;
    private final FtpClientProperties properties;

    @Setter
    private FTPClient ftp;

    /**
     * Instantiates a new Ftp client.
     *
     * @param fileLoader the file loader
     * @param properties the properties
     */
    public FtpClient(FileLoader fileLoader, FtpClientProperties properties) {
        this.fileLoader = fileLoader;
        this.properties = properties;
        this.ftp = new FTPClient();
    }

    /**
     * Connect to Server.
     *
     * @throws IOException if connection failed
     */
    public void connect() throws IOException {
        if (ftp.isConnected()) {
            throw new IllegalStateException("Previous FTP connection is still open");
        }

        String server = properties.getUrl();
        int port = properties.getPort();
        String user = properties.getUser();
        String password = properties.getPassword();

        ftp.setControlEncoding(properties.getEncoding());
        if (log.isDebugEnabled()) {
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }

        ftp.connect(server, port);
        if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            close();
            throw new ExecutionException("There was a problem connecting to the FTP server");
        }
        ftp.login(user, password);
    }

    /**
     * Get list of filenames for given directory path on FTP Client.
     *
     * @param path - directory
     * @return list of found file names
     */
    public List<String> getFileNames(final String path) {
        try {
            return Arrays.stream(ftp.listFiles(path)).map(FTPFile::getName).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ExecutionException("Error when getting a list of files from a directory: " + path, e);
        }
    }

    /**
     * Copy file from Client FTP Server for given source to location.
     *
     * @param sourcePath      sourceFilePath
     * @param destinationPath location
     */
    public void downloadFile(final String sourcePath, final String destinationPath) {
        checkDownloadFolder(destinationPath, TEMP_FTP_FOLDER);
        try {
            fileLoader.createFile(destinationPath, ftp.retrieveFileStream(sourcePath));
        } catch (IOException e) {
            throw new ExecutionException("File download error '{}' -> '{}'", e, sourcePath, destinationPath);
        }
    }

    /**
     * Upload file on FTP Server.
     *
     * @param source      path to local file within resources
     * @param destination path to locate file on FTP server
     */
    public void uploadFile(final String source, final String destination) {
        try (var inputStream = fileLoader.getFileInputStream(source)) {
            if (!ftp.storeFile(destination, inputStream)) {
                throw new ExecutionException("File upload error '{}' -> '{}'", source, destination);
            }
        } catch (Exception e) {
            throw new ExecutionException("File upload error '{}' -> '{}'", e, source, destination);
        }
    }

    /**
     * Delete file on FTP Server.
     *
     * @param path path to file
     */
    public void deleteFile(final String path) {
        try {
            if (!ftp.deleteFile(path)) {
                throw new ExecutionException("Error deleting file '{}' from server", path);
            }
        } catch (Exception e) {
            throw new ExecutionException("Error deleting file '{}' from server", e, path);
        }
    }

    /**
     * Close FTP Server connection.
     */
    public void close() {
        try {
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
        } catch (IOException e) {
            log.error("FTP server connection termination error", e);
        } finally {
            ftp = null;
        }
    }
}
