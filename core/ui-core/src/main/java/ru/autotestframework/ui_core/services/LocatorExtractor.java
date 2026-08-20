package ru.autotestframework.ui_core.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocatorExtractor {

    // Обнаруженный элемент (с session ID)
    // [[ChromeDriver: chrome on LINUX (14e6dbce1917610bef517598e4b0c179)] -> xpath: //*[@value='cancel']]
    Pattern pattern1 = Pattern.compile("->\\\\s*(.*?)\\\\s*\\\\]");
    // Любой другой формат с By.
    // By.xpath: //button
    Pattern pattern4 = Pattern.compile("By\\.(\\w+):\\s*(.+)");
    // Формат с -> но без [[ ]]
    // -> css selector: .btn
    Pattern pattern5 = Pattern.compile("->\\s*(\\w+\\s*\\w*):\\s*(.+)");

    /**
     * Извлекает локатор из любого представления Selenide элемента
     */
    public static String extractLocator(String elementString) {
        if (elementString == null || elementString.isEmpty()) {
            return null;
        }

        Matcher m1 = pattern1.matcher(elementString);
        if (m1.find()) {
            return normalizeLocator(m1.group(1), m1.group(2));
        }

        Matcher m4 = pattern4.matcher(elementString);
        if (m4.find()) {
            return normalizeLocator(m4.group(1), m4.group(2));
        }

        Matcher m5 = pattern5.matcher(elementString);
        if (m5.find()) {
            return normalizeLocator(m5.group(1), m5.group(2));
        }

        return null;
    }

    /**
     * Нормализует локатор к единому формату
     */
    private static String normalizeLocator(String type, String value) {
        type = type.trim().toLowerCase();

        // Нормализуем тип
        if (type.equals("css selector") || type.equals("css")) {
            type = "css";
        } else if (type.equals("class name") || type.equals("class")) {
            type = "class";
        } else if (type.equals("tag name") || type.equals("tag")) {
            type = "tag";
        } else if (type.equals("link text") || type.equals("link")) {
            type = "link";
        } else if (type.equals("partial link text") || type.equals("partiallink")) {
            type = "partialLink";
        }

        // Очищаем значение
        value = value.trim()
                .replaceAll("'$", "") // Убираем кавычки в конце
                .replaceAll("^'", "") // Убираем кавычки в начале
                .replaceAll("\\]\\]$", "\\]") // Убираем лишнюю ]
                .replaceAll("\\s+", " ") // Нормализуем пробелы
                .replaceAll("\\}$", ""); // Нормализуем пробелы

        return type + ":" + value;
    }
}
