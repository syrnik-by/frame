package ru.autotestframework.cucumber.step_defs.back;

import static ru.autotestframework.core.FileUtils.createZipAndGetZipPath;
import static ru.autotestframework.core.matcher.IsEqualFile.isEqualFile;
import static ru.autotestframework.util.Validator.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.FileUtils;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;

/**
 * File steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("Шаги работы с файлами")
public class FileSteps {

    private final FileLoaderImpl fileLoader;

    /**
     * Check files equality.
     *
     * @param actualFilePath   the actual file path
     * @param expectedFilePath the expected file path
     */
    @Then("проверить, что содержимое файлов {path} и {path} совпадает")
    @Sample("проверить, что файлы идентичны во всем, кроме наименования")
    @Parameter(type = "path", name = "путь 1 файла")
    @Parameter(type = "path", name = "путь 2 файла")
    @Example(example = "И проверить, что содержимое файлов 'data/https/1.txt' и 'data/https/2.txt' совпадает")
    public void checkFilesEquality(final String actualFilePath, final String expectedFilePath) {
        var actual = fileLoader.getFile(actualFilePath);
        var expected = fileLoader.getFile(expectedFilePath);
        assertThat(actual, isEqualFile(expected), "Contents of Files are not equal");
    }

    /**
     * Check file existance.
     *
     * @param filePath the file path
     */
    @Then("проверить, что существует файл {path}")
    @Sample("Проверяет наличие файла")
    @Parameter(type = "path", name = "путь файла")
    @Example(example = "И проверить, что существует файл 'data/http/1.txt'")
    public void checkFileExistance(final String filePath) {
        Assertions.assertDoesNotThrow(() -> fileLoader.getFile(filePath));
    }

    /**
     * Delete file.
     *
     * @param filePath the file path
     */
    @Then("удалить файл по пути {path}")
    @Sample("Удаляет файл или директорию")
    @Parameter(type = "path", name = "путь файла или директории")
    @Example(example = "И удалить файл по пути 'data/http/1.txt'")
    public void deleteFile(final String filePath) {
        fileLoader.deleteFile(filePath);
    }

    /**
     * Upload file to element.
     *
     * @param filePath the file path
     * @param text     the text
     */
    @When("создать файл {resolvable_string} с текстом:")
    @Parameter(type = ":", name = "содержимое файла")
    public void uploadFileToElement(String filePath, final String text) {
        fileLoader.createTextFile(filePath, text);
    }

    /**
     * Unzip file.
     *
     * @param filePath      the file path
     * @param destDirectory the dest directory
     */
    @SneakyThrows
    @Then("распаковать zip файл {path} в {path}")
    public void unzipFile(String filePath, String destDirectory) {
        var filePath1 = filePath.replace("file:", "");
        FileUtils.unzip(filePath1, destDirectory);
    }

    /**
     * Zip file.
     *
     * @param filePath      the file path
     * @param destDirectory the dest directory
     */
    @SneakyThrows
    @Then("упаковать файл {path} в zip {path}")
    public void zipFile(String filePath, String destDirectory) {
        var filePath1 = filePath.replace("file:", "");
        createZipAndGetZipPath(filePath1, destDirectory);
    }
}
