package ru.converter.util;

import static org.apache.commons.lang.StringUtils.isNumeric;

import java.net.URL;
import ru.autotestframework.core.exception.AutotestException;

public class StringUtil {

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) return "";

        if (input.matches(".*[a-z][A-Z].*") && !input.matches(".*[\\s\\-].*")) {
            return input;
        }
        input = input.replaceAll("^[^a-zA-Z0-9]+", "");
        String[] parts = input.split("[^a-zA-Z0-9]");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static String getDomainPart(String[] domainParts, URL urlObj) {
        String domainPart = domainParts[0];
        // Также учитываем путь, если он есть
        String path = urlObj.getPath();
        if (path != null && !path.isEmpty() && !path.equals("/")) {
            // Берем последнюю часть пути
            String[] pathParts = path.split("/");
            for (int i = pathParts.length - 1; i >= 0; i--) {
                if (!pathParts[i].isEmpty() && !isNumeric(pathParts[i])) {
                    domainPart = pathParts[i];
                    break;
                }
            }
        }
        return domainPart;
    }

    public static String formatJavaCode(String code) {
        if (code == null) return "";
        String[] lines = code.split("\n");
        StringBuilder formatted = new StringBuilder();
        int emptyLineCount = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                emptyLineCount++;
                if (emptyLineCount <= 2) {
                    formatted.append("\n");
                }
            } else {
                emptyLineCount = 0;
                formatted.append(line).append("\n");
            }
        }
        return formatted.toString().trim();
    }

    /**
     * Извлекает имя страницу из URL
     */
    public static String extractPageNameFromUrl(String url) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (Exception e) {
            throw new AutotestException("Ошибка извлечения страницы из URL: " + url);
        }
        return toCamelCase(
                getDomainPart(urlObj.getHost().replaceFirst("^www\\.", "").split("\\."), urlObj));
    }

    public static String escapeJava(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
