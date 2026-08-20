package ru.autotestframework.core;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.autotestframework.Constants.SUPPORTED_TEXT_FILES;
import static ru.autotestframework.Constants.TEMP_FOLDER;
import static ru.autotestframework.util.Validator.checkThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.util.Validator;

/**
 * File loader.
 */
@Slf4j
@Component
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class FileLoaderImpl implements FileLoader, DisposableBean {

    private static final String WINDOWS_FILE_SEPARATOR = "\\";
    private static final String UNIX_FILE_SEPARATOR = "/";
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    /**
     * The constant ERROR_CREATING_THE_FILE.
     */
    public static final String ERROR_CREATING_THE_FILE = "Error creating the file.";
    /**
     * The constant ERROR_RECEIVING_THE_FILE.
     */
    public static final String ERROR_RECEIVING_THE_FILE = "Error receiving the file '{}'";

    private final PlaceholderResolver placeholderResolver;
    private final ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
    private final FrameworkProperties frameworkProperties;

    /**
     * Gets file extension.
     *
     * @param path the path
     * @return the file extension
     */
    public static String getFileExtension(String path) {

        if (path == null) {
            throw new IllegalArgumentException("fileName must not be null!");
        }

        var extension = "";

        int indexOfLastExtension = path.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        // check last file separator, windows and unix
        int lastSeparatorPosWindows = path.lastIndexOf(WINDOWS_FILE_SEPARATOR);
        int lastSeparatorPosUnix = path.lastIndexOf(UNIX_FILE_SEPARATOR);

        // takes the greater of the two values, which mean last file separator
        int indexOflastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);

        // make sure the file extension appear after the last file separator
        if (indexOfLastExtension > indexOflastSeparator) {
            extension = FILE_EXTENSION_SEPARATOR + path.substring(indexOfLastExtension + 1);
        }
        return extension;
    }

    @Override
    public File createFile(final String path, final InputStream inputStream) {
        Validator.checkDownloadFolder(path, TEMP_FOLDER);
        try {
            var file = innerCreateFile(path);
            FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            throw Validator.exception(ERROR_CREATING_THE_FILE, e);
        }
    }

    @SneakyThrows
    @Override
    public File createTextFile(final String path, final InputStream inputStream) {
        var data = IOUtils.toString(inputStream, UTF_8);
        return createTextFile(path, data);
    }

    @Override
    public File createTextFile(final String path, final String data) {
        Validator.checkDownloadFolder(path, TEMP_FOLDER);
        try {
            var textFile = innerCreateFile(path);
            String resolvedData = placeholderResolver.resolve(data);
            FileUtils.writeStringToFile(textFile, resolvedData, UTF_8);
            return textFile;
        } catch (IOException e) {
            throw Validator.exception(ERROR_CREATING_THE_FILE, e);
        }
    }

    @Override
    public File createFileInAnyDir(final String path, final InputStream inputStream) {
        try {
            var file = innerGetFileOrCreateFile(path);
            FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            throw Validator.exception(ERROR_CREATING_THE_FILE, e);
        }
    }

    private File innerCreateFile(final String path) throws IOException {
        var file = new File(path);
        Validator.checkThat(!file.exists(), "File {} already exists", path);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw Validator.exception("Error creating new directory.");
            }
        }
        if (!file.createNewFile()) {
            throw Validator.exception("Error creating new file.");
        }
        return file;
    }

    private File innerGetFileOrCreateFile(final String path) throws IOException {
        var file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw Validator.exception("Error creating new directory.");
                }
            }
            if (!file.createNewFile()) {
                throw Validator.exception("Error creating new file.");
            }
        }
        return file;
    }

    @SneakyThrows
    @Override
    public File createEmptyFileExclusively(final String path) {
        var file = new File(path);
        if (!file.mkdirs()) {
            throw new ExecutionException("Cannot create directory");
        }
        if (!file.createNewFile()) {
            throw new ExecutionException("Cannot create file");
        }
        return file;
    }

    @SneakyThrows
    @Override
    public File getFile(final String path) {
        return loader.getResource(path).getFile();
    }

    /**
     * Gets file with encoding.
     *
     * @param filePath the file path
     * @param charset  the charset
     * @return the file with encoding
     */
    @SneakyThrows
    public File getFileWithEncoding(final String filePath, Charset charset) {
        var resource = loader.getResource(filePath);

        var data = readAsResolvedString(resource, charset);
        var resolvedFile = new File(TEMP_FOLDER + "/temp" + getFileExtension(resource.getFilename()));
        FileUtils.writeStringToFile(resolvedFile, data, charset);
        return resolvedFile;
    }

    @Override
    public InputStream getFileInputStream(final String filePath) {
        try {
            var file = loader.getResource(filePath);
            if (isTextFile(file)) {
                var content = readAsResolvedString(file);
                return IOUtils.toInputStream(content, UTF_8);
            } else {
                return file.getInputStream();
            }
        } catch (IOException e) {
            throw Validator.exception(ERROR_RECEIVING_THE_FILE, e, filePath);
        }
    }

    /**
     * Gets file input stream with encoding.
     *
     * @param filePath the file path
     * @param charset  the charset
     * @return the file input stream with encoding
     */
    public InputStream getFileInputStreamWithEncoding(final String filePath, Charset charset) {
        try {
            var file = loader.getResource(filePath);
            if (isTextFile(file)) {
                var content = readAsResolvedString(file, charset);
                return IOUtils.toInputStream(content, charset);
            } else {
                return file.getInputStream();
            }
        } catch (IOException e) {
            throw Validator.exception(ERROR_RECEIVING_THE_FILE, e, filePath);
        }
    }

    @Override
    public String readFileAsString(final String filePath) {
        var resource = loader.getResource(filePath);
        return readAsResolvedString(resource);
    }

    /**
     * Read file as string with encoding string.
     *
     * @param filePath the file path
     * @param encoding the encoding
     * @return the string
     */
    public String readFileAsStringWithEncoding(String filePath, Charset encoding) {
        var resource = loader.getResource(filePath);
        return readAsResolvedString(resource, encoding);
    }

    @Override
    public List<String> readFilesAsString(final String filesLocationPattern) {
        var fileContentList = new ArrayList<String>();
        try {
            for (Resource resource : loader.getResources(filesLocationPattern)) {
                var resolvedContent = readAsResolvedString(resource);
                fileContentList.add(resolvedContent);
            }
            return fileContentList;
        } catch (IOException e) {
            throw Validator.exception("Error receiving files by pattern '{}'", e, filesLocationPattern);
        }
    }

    private String readAsResolvedString(final Resource resource) {
        try (var reader = new InputStreamReader(resource.getInputStream(), UTF_8);
                var bufReader = new BufferedReader(reader)) {
            checkThat(
                    isTextFile(resource), "The text file is not supported. Supported files: {}", SUPPORTED_TEXT_FILES);

            var content = bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
            return placeholderResolver.resolve(content);
        } catch (IOException e) {
            throw Validator.exception(ERROR_RECEIVING_THE_FILE, e, resource.getDescription());
        }
    }

    private String readAsResolvedString(Resource resource, Charset encoding) {
        try (var reader = new InputStreamReader(resource.getInputStream(), encoding);
                var bufReader = new BufferedReader(reader)) {
            checkThat(
                    isTextFile(resource),
                    "Текстовый файл не поддерживается. Поддерживаемые файлы: {}",
                    SUPPORTED_TEXT_FILES);

            var content = bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
            return placeholderResolver.resolve(content);
        } catch (IOException e) {
            throw Validator.exception("Ошибка получения файла '{}'", e, resource.getDescription());
        }
    }

    private boolean isTextFile(final Resource resource) {
        try {
            var fileName = resource.getFile().getName();
            int dotIndex = fileName.lastIndexOf('.');
            var fileExtension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
            return SUPPORTED_TEXT_FILES.contains(fileExtension);
        } catch (IOException e) {
            throw Validator.exception(ERROR_RECEIVING_THE_FILE, resource.getDescription());
        }
    }

    /**
     * Clean all.
     */
    public void cleanAll() {
        if (frameworkProperties.getTempFilesCleaningEnabled()) {
            var tempDir = new File(TEMP_FOLDER);
            FileUtils.deleteQuietly(tempDir);
            // todo перенести очистку в @AfterAll или DisposbleBean
            checkThat(!tempDir.exists(), "Error deleting temporary files from a folder '{}'", TEMP_FOLDER);
            log.info("Temporary files from the '{}' folder have been deleted", TEMP_FOLDER);
        } else {
            log.info("Temporary files are saved in the directory '{}'", TEMP_FOLDER);
        }
    }

    @Override
    public void clean() {
        int counterParallelism = Math.max(
                Integer.getInteger("cucumber.execution.parallel.config.fixed.parallelism", 1),
                Integer.getInteger("junit.execution.parallel.config.fixed.parallelism", 1));
        if (counterParallelism == 1) {
            cleanAll();
        }
    }

    /**
     * Delete file.
     *
     * @param path the path
     */
    public void deleteFile(final String path) {
        if (!FileUtils.deleteQuietly(new File(path))) {
            throw Validator.exception("Error deleting temporary files from a folder '{}'", path);
        }
    }

    @Override
    public void destroy() throws Exception {
        cleanAll();
    }
}
