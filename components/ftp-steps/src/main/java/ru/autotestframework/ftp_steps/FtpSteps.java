package ru.autotestframework.ftp_steps;

import static ru.autotestframework.util.Validator.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import javax.annotation.PreDestroy;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.ftp_steps.ftp.FtpClient;
import ru.autotestframework.ftp_steps.ftp.FtpClientProperties;

/**
 * Ftp steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("FTP")
public class FtpSteps {

    private final FileLoader fileLoader;

    @Getter
    @Setter
    private FtpClient client;

    @Getter
    @Setter
    private List<String> fileNames;

    /**
     * Connect.
     *
     * @param properties the properties
     * @throws IOException the io exception
     */
    @When("установить подключение к FTP серверу:")
    @Sample("Установить подключение к FTP серверу по параметрам")
    @Parameter(type = ":", name = "список параметров")
    @Example(
            example = "И установить подключение к FTP серверу:"
                    + " |   url   | port |   user   |   password   |  encoding   |"
                    + " | ftp.url |  21  | ftp.user | ftp.password | WINDOWS-125 |")
    public void connect(final FtpClientProperties properties) throws IOException {
        client = new FtpClient(fileLoader, properties);
        client.connect();
    }

    /**
     * Check file names.
     *
     * @param path the path
     */
    @When("получить с cервера список файлов из {path}")
    @Sample("Получить с сервера список файлов из каталога")
    @Parameter(type = "path", name = "путь каталога")
    @Example(example = "И получить с cервера список файлов из 'data/http'")
    public void checkFileNames(final String path) {
        fileNames = client.getFileNames(path);
    }

    /**
     * Upload file.
     *
     * @param source      the source
     * @param destination the destination
     */
    @When("загрузить на сервер файл {path}->{path}")
    @Sample("Загрузить файл на сервер")
    @Parameter(type = "path", name = "путь файла источника")
    @Parameter(type = "path", name = "путь файла назначения на сервере")
    @Example(example = "И загрузить на сервер файл 'data/http/source.txt'->'data/http/dest.txt'")
    public void uploadFile(final String source, final String destination) {
        client.uploadFile(source, destination);
    }

    /**
     * Download file.
     *
     * @param source      the source
     * @param destination the destination
     */
    @When("загрузить c сервера файл {path}->{path}")
    @Sample("Загрузить файл с сервера")
    @Parameter(type = "path", name = "путь файла источника на сервере")
    @Parameter(type = "path", name = "путь файла назначения")
    @Example(example = "И загрузить с сервера файл 'data/http/source.txt'->'data/http/dest.txt'")
    public void downloadFile(final String source, final String destination) {
        client.downloadFile(source, destination);
    }

    /**
     * Delete file.
     *
     * @param path the path
     */
    @When("удалить c сервера файл {path}")
    @Sample("Удалить файл с сервера")
    @Parameter(type = "path", name = "путь к файлу на сервере")
    @Example(example = "И удалить c сервера файл 'data/http/source.txt'")
    public void deleteFile(final String path) {
        client.deleteFile(path);
    }

    /**
     * Check file names.
     *
     * @param expectedNames the expected names
     */
    @Then("полученный список файлов содержит файлы:")
    @Sample("Проверить список с сервера на наличие определенных файлов")
    @Parameter(type = ":", name = "список путей файлов")
    @Example(example = "И полученный список файлов содержит файлы:" + "| filePath |")
    public void checkFileNames(final List<String> expectedNames) {
        SoftAssertions.assertSoftly(
                softly -> expectedNames.forEach(expectedName -> softly.assertThatCode(() -> assertThat(
                                fileNames.contains(expectedName.toString()),
                                "File '{}' is missing in provided list: {}",
                                expectedName,
                                fileNames))
                        .doesNotThrowAnyException()));
    }

    /**
     * Close connection.
     */
    @PreDestroy
    public void closeConnection() {
        if (client != null) {
            client.close();
        }
    }
}
