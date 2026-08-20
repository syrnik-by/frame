package ru.autotestframework.util.access_checker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PropertiesHelper {
    private static final String PROPERTIES_PATH = "./src/test/resources/";

    private PropertiesHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * returns properties by file name
     * @param fileName
     * @return
     */
    public static Properties getProperties(final String fileName) {
        var properties = new Properties();
        try (var fis = new FileInputStream(PROPERTIES_PATH.concat(fileName))) {
            properties.load(fis);
        } catch (IOException | NullPointerException e) {
            log.error("Exception while read properties: ", e);
        }
        return properties;
    }
}
