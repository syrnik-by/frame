package ru.autotestframework.orm_steps.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 * Class finder.
 */
@UtilityClass
public class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * Retrieves the list of classes in the package
     *
     * @param scannedPackage the scanned package
     * @return the list
     */
    @SneakyThrows
    public static List<Class<?>> find(String scannedPackage) {
        return new Reflections(scannedPackage, new SubTypesScanner(false))
                .getAllTypes().stream()
                        .map(name -> {
                            try {
                                return Class.forName(name);
                            } catch (ClassNotFoundException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }

    @SneakyThrows
    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            var className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
                throw new Exception(ignore);
            }
        }
        return classes;
    }
}
