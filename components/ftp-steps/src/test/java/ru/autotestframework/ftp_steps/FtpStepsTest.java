package ru.autotestframework.ftp_steps;

import static org.mockito.Mockito.*;
import static ru.autotestframework.Constants.TEMP_FOLDER;
import static ru.autotestframework.Constants.TEMP_FTP_FOLDER;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.configuration.PlaceholderResolverConfig;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.ftp_steps.ftp.FtpClient;
import ru.autotestframework.ftp_steps.ftp.FtpClientProperties;
import ru.autotestframework.util.generator.FakerRU;

/**
 * Ftp steps test.
 */
@Tag("@FtpDemo")
class FtpStepsTest {
    private final FtpSteps steps = new FtpSteps(
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties()));

    @Mock
    private FTPClient ftpClient;

    @Mock
    private FtpClientProperties properties;

    @Mock
    private FileLoader fileLoader;

    @InjectMocks
    private FtpClient ftp;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ftp.setFtp(ftpClient);
        steps.setClient(ftp);
    }

    /**
     * The Random path.
     */
    String randomPath = FakerRU.instance().file().fileName();

    /**
     * Connect.
     */
    @Test
    // нет возможности тестировать реальный FTP или поднимать заглушки, поэтому
    // проверяем косвенно по ошибкам подключения и неизвестного хоста
    void connect() {
        final FtpClientProperties localProperties =
                new FtpClientProperties("localhost", 21, "user", "password", StandardCharsets.UTF_8.name());
        Assertions.assertThrows(java.net.ConnectException.class, () -> steps.connect(localProperties));
        final FtpClientProperties invalidProperties =
                new FtpClientProperties("localhot", 21, "user", "password", StandardCharsets.UTF_8.name());
        Assertions.assertThrows(java.net.UnknownHostException.class, () -> steps.connect(invalidProperties));
        PlaceholderResolverConfig placeholderResolverConfig = new PlaceholderResolverConfig();

        Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
        context.set("variableName", "variableValue");

        List<ContextFunctionsSupplier> list = new ArrayList<>();
        StringSubstitutor ss = placeholderResolverConfig.placeholderResolver(context, list);
    }

    /**
     * Check file names.
     */
    @Test
    void checkFileNames() {
        final List<String> data = List.of("point 1", "point 2");
        final FtpClient client = mock(FtpClient.class);
        when(client.getFileNames(anyString())).thenReturn(data);
        steps.setClient(client);
        steps.checkFileNames("/some/path");
        Assertions.assertEquals(data, steps.getFileNames());
    }

    /**
     * Upload file.
     *
     * @throws IOException the io exception
     */
    @Test
    void uploadFile() throws IOException {
        final FileLoader fileLoader = mock(FileLoader.class);
        when(fileLoader.getFileInputStream(anyString())).thenReturn(mock(InputStream.class));
        final FtpClient client = new FtpClient(fileLoader, null);
        steps.setClient(client);
        final FTPClient apacheClient = mock(FTPClient.class);
        client.setFtp(apacheClient);
        when(apacheClient.storeFile(anyString(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> steps.uploadFile("/path/to/source", "/path/to/destination"));
        verify(apacheClient, atLeastOnce()).storeFile(anyString(), any());
    }

    /**
     * Download file.
     *
     * @throws IOException the io exception
     */
    @Test
    void downloadFile() throws IOException {
        Files.deleteIfExists(Paths.get(TEMP_FTP_FOLDER, "/downloadTest"));
        final AtomicInteger dataCounter = new AtomicInteger(2000);
        final InputStream fakeInputStream = new InputStream() {
            @Override
            public int read() {
                if (dataCounter.decrementAndGet() > 0) {
                    return (int) (Math.random() * 100) + 100;
                }
                return -1;
            }
        };
        final FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        final FtpClient client = new FtpClient(fileLoader, null);
        steps.setClient(client);
        final FTPClient apacheClient = mock(FTPClient.class);
        client.setFtp(apacheClient);
        when(apacheClient.retrieveFileStream(anyString())).thenReturn(fakeInputStream);
        steps.downloadFile("/some/path/to/source", TEMP_FTP_FOLDER + "/downloadTest");
        Assertions.assertTrue(Files.exists(Paths.get(TEMP_FTP_FOLDER, "/downloadTest")));
        Files.deleteIfExists(Paths.get(TEMP_FTP_FOLDER, "/downloadTest"));
        Files.deleteIfExists(Paths.get(TEMP_FTP_FOLDER));
        Files.deleteIfExists(Paths.get(TEMP_FOLDER));
    }

    /**
     * Delete file.
     *
     * @throws IOException the io exception
     */
    @Test
    void deleteFile() throws IOException {
        final FtpClient client = new FtpClient(null, null);
        steps.setClient(client);
        final FTPClient apacheClient = mock(FTPClient.class);
        client.setFtp(apacheClient);
        when(apacheClient.deleteFile(anyString())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> steps.deleteFile("/some/path/to/file"));
        verify(apacheClient, atLeastOnce()).deleteFile("/some/path/to/file");
    }

    /**
     * Check file names with static path list.
     */
    @Test
    void checkFileNamesWithStaticPathList() {
        final List<String> expected = List.of("path/1", "path/2");
        final List<String> actual = List.of("path/1", "path/2");
        final List<String> wrongActual = List.of("path/1", "path/3");
        steps.setFileNames(actual);
        Assertions.assertDoesNotThrow(() -> steps.checkFileNames(expected));
        steps.setFileNames(wrongActual);
        Assertions.assertThrows(AssertionError.class, () -> steps.checkFileNames(expected));
    }

    /**
     * Gets file names test.
     *
     * @throws IOException the io exception
     */
    @Test
    void getFileNamesTest() throws IOException {
        String path = "/test/directory";
        FTPFile file1 = new FTPFile();
        file1.setName("file1.txt");
        FTPFile file2 = new FTPFile();
        file2.setName("file2.txt");
        FTPFile[] files = {file1, file2};
        when(ftpClient.listFiles(path)).thenReturn(files);

        List<String> result = ftp.getFileNames(path);
        Assertions.assertEquals(Arrays.asList("file1.txt", "file2.txt"), result);
    }

    /**
     * Gets file names with io exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void getFileNamesWithIOExceptionTest() throws IOException {
        when(ftpClient.listFiles(anyString())).thenThrow(IOException.class);
        Assertions.assertThrows(ExecutionException.class, () -> ftp.getFileNames(anyString()));
    }

    /**
     * Delete file with execution exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void deleteFileWithExecutionExceptionTest() throws IOException {
        when(ftpClient.deleteFile(anyString())).thenReturn(false);
        Assertions.assertThrows(ExecutionException.class, () -> ftp.deleteFile(randomPath));
    }

    /**
     * Delete file with io exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void deleteFileWithIOExceptionTest() throws IOException {
        when(ftpClient.deleteFile(anyString())).thenThrow(IOException.class);
        Assertions.assertThrows(ExecutionException.class, () -> ftp.deleteFile(randomPath));
    }

    /**
     * Close success test.
     *
     * @throws IOException the io exception
     */
    @Test
    void closeSuccessTest() throws IOException {
        when(ftpClient.isConnected()).thenReturn(true);
        ftp.close();
        verify(ftpClient).disconnect();
    }

    /**
     * Close connection test.
     *
     * @throws IOException the io exception
     */
    @Test
    void closeConnectionTest() throws IOException {
        when(ftpClient.isConnected()).thenReturn(true);
        steps.closeConnection();
        verify(ftpClient).disconnect();
    }

    /**
     * Upload file with exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void uploadFileWithExceptionTest() throws IOException {
        doThrow(new IOException()).when(ftpClient).storeFile(anyString(), any());
        Assertions.assertThrows(
                ExecutionException.class,
                () -> ftp.uploadFile(
                        FakerRU.instance().file().fileName(),
                        FakerRU.instance().file().fileName()));
    }

    /**
     * Connect failed positive completion test.
     */
    @Test
    void connectFailedPositiveCompletionTest() {
        when(ftpClient.getReplyCode()).thenReturn(500);

        Assertions.assertThrows(ExecutionException.class, () -> ftp.connect());
    }

    /**
     * Connect when already connected test.
     */
    @Test
    void connectWhenAlreadyConnectedTest() {
        when(ftpClient.isConnected()).thenReturn(true);

        Assertions.assertThrows(IllegalStateException.class, () -> ftp.connect());
    }
}
