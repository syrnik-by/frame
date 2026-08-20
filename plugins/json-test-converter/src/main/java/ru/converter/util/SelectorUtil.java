package ru.converter.util;

public class SelectorUtil {

    public static String generateElementNameForPage(String selector) {
        if (selector == null) {
            return "element";
        }
        String cleanSelector =
                selector.replaceAll("[#.\\[\\]=]", " ").replaceAll("\\s+", " ").trim();
        String baseName = "";
        if (selector.startsWith("#")) {
            baseName = selector.substring(1).split("[\\[\\]]")[0];
        } else if (selector.startsWith(".")) {
            baseName = selector.substring(1).split("\\.")[0];
        }
        if (baseName.isEmpty()) {
            String[] parts = cleanSelector.split(" ");
            if (parts.length > 0) {
                baseName = parts[parts.length - 1];
            }
        }
        String camelName = StringUtil.toCamelCase(baseName);
        if (camelName.isEmpty()) {
            camelName = "element";
        }
        return camelName;
    }

    /**
     * Извлекает значение атрибута из селектора
     */
    private static String extractAttributeValue(String selector, String attribute) {
        String[] patterns = {
            attribute + "=\"([^\"]*)\"",
            attribute + "='([^']*)'",
            "\\[" + attribute + "=\"([^\"]*)\"\\]",
            "\\[" + attribute + "='([^']*)'\\]"
        };
        for (String pattern : patterns) {
            try {
                java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
                java.util.regex.Matcher m = r.matcher(selector);
                if (m.find()) {
                    return m.group(1);
                }
            } catch (Exception e) {
                // Продолжаем поиск
            }
        }
        return "";
    }

    /**
     * Извлекает имя data атрибута
     */
    private static String extractDataAttributeName(String selector) {
        try {
            java.util.regex.Pattern r = java.util.regex.Pattern.compile("data-([a-zA-Z-]+)");
            java.util.regex.Matcher m = r.matcher(selector);
            if (m.find()) {
                return m.group(1).replace("-", " ");
            }
        } catch (Exception e) {
            // Игнорируем ошибки
        }
        return "";
    }
}
