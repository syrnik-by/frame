package ru.autotestframework.core;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import ru.autotestframework.core.context.Cleanable;

/**
 * Represents an object that allows you to work with files.
 */
public interface FileLoader extends Cleanable {

    /**
     * Creating a file in the temp directory.
     *
     * @param path location of the file being created
     * @param inputStream is a stream with content
     * @return returns the created {@code File}
     */
    File createFile(String path, InputStream inputStream);

    /**
     * File creation.
     *
     * @param path location of the file being created
     * @param inputStream is a stream with content
     * @return returns the created {@code File}
     */
    File createFileInAnyDir(String path, InputStream inputStream);

    /**
     * Creating an empty file or returning the one found by location.
     *
     * @param path file location
     * @return returns a found or created file {@code File}
     */
    File createEmptyFileExclusively(String path);

    /**
     * Creating a text file, the contents of which will be passed through {@link PlaceholderResolver}.
     *
     * @param path location of the file being created
     * @param inputStream is a stream with content
     * @return returns the created {@code File}
     */
    File createTextFile(String path, InputStream inputStream);

    /**
     * Creating a text file, the contents of which will be passed through {@link PlaceholderResolver}.
     *
     * @param path location of the file being created
     * @param data file contents
     * @return returns the created {@code File}
     */
    File createTextFile(String path, String data);

    /**
     * Returns the corresponding {@code File}, without any processing.
     *
     * @param path file location
     * @return corresponding to {@code Resource}
     */
    File getFile(String path);

    /**
     * Returns {@code InputStream} of the requested file,
     * if the file is text, it will first be passed through {@link PlaceholderResolver}.
     *
     * @param path file location
     * @return processed {@code InputStream}
     */
    InputStream getFileInputStream(String path);

    /**
     * Returns the contents of the text file, previously passed through {@link PlaceholderResolver}.
     *
     * @param path file location
     * @return processed text file content
     */
    String readFileAsString(String path);

    /**
     * Returns the contents of text files previously passed through {@link PlaceholderResolver}.
     *
     * @param filesLocationPattern file location pattern.
     * <br> For example: 'data/*.json'
     * @return list of processed text file contents
     */
    List<String> readFilesAsString(String filesLocationPattern);
}
