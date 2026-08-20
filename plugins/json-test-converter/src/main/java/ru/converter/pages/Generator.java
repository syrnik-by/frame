package ru.converter.pages;

import static ru.autotestframework.ui_core.services.CoreReflections.determineLocatorType;
import static ru.converter.util.StringUtil.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;
import ru.converter.model.TestAction;
import ru.converter.model.TestCase;
import ru.converter.model.TestSuite;
import ru.converter.template.TemplateProcessor;

/**
 * Генератор Page Objects только с аннотированными полями
 */
@Slf4j
public class Generator {

    private final TemplateProcessor templateProcessor;
    private final Store pageStore;

    public Generator() {
        this.pageStore = new Store();
        this.templateProcessor = new TemplateProcessor();
    }

    /**
     * Извлекает уникальные страницы из тест-кейсов
     */
    public Store extractPagesFromTestSuite(TestSuite testSuite) {
        testSuite.getTestCases().forEach(testCase -> {
            extractElementsFromTestCase(testCase);
            log.info(
                    "Extracted {} unique pages from test suite",
                    pageStore.getPages().size());
        });
        return pageStore;
    }

    /**
     * Форматирует значение локатора для аннотации
     */
    private String formatLocatorValue(String selector, String locatorType) {
        if (selector == null || selector.isEmpty()) return "";
        selector = selector.trim();
        switch (locatorType) {
            case "id":
                // Убираем # для id
                return selector.startsWith("#") ? selector.substring(1) : selector;
            case "className":
                // Убираем . для className, берем первый класс
                if (selector.startsWith(".")) {
                    String className = selector.substring(1);
                    // Если несколько классов, берем первый
                    int dotIndex = className.indexOf('.');
                    if (dotIndex > 0) {
                        className = className.substring(0, dotIndex);
                    }
                    return className;
                }
                return selector;
            case "name":
                // Извлекаем значение из [name="value"] или name="value"
                if (selector.contains("name=\"")) {
                    int start = selector.indexOf("name=\"") + 6;
                    int end = selector.indexOf("\"", start);
                    if (end > start) {
                        return selector.substring(start, end);
                    }
                } else if (selector.contains("[name='")) {
                    int start = selector.indexOf("[name='") + 7;
                    int end = selector.indexOf("']", start);
                    if (end > start) {
                        return selector.substring(start, end);
                    }
                }
                return selector;
            case "xpath":
                // Для xpath возвращаем как есть
                return selector;
            case "tagName":
            case "css":
            default:
                return selector;
        }
    }

    /**
     * Извлекает элементы из тест-кейса
     */
    private void extractElementsFromTestCase(TestCase testCase) {
        String pageName = extractPageNameFromUrl(testCase.getStartURL());
        addPage(testCase.getStartURL());
        for (TestAction action : testCase.getActions()) {
            if (action.getAction().equals("changeTab")) {
                pageName = extractPageNameFromUrl(action.getUrl());
                addPage(action.getUrl());
            } else {
                Store.Page page = pageStore.getPage(pageName);
                String selector = action.getSelector();
                if (selector != null && !selector.isEmpty()) {
                    String elementName = toCamelCase(action.getClassName());
                    String locatorType = determineLocatorType(selector);
                    String locatorValue = formatLocatorValue(selector, locatorType);
                    String elementType = determineElementType(action.getTagName());
                    if (page.getInfo().getElements().stream()
                            .noneMatch(info -> info.getLocatorValue().equals(locatorValue))) {
                        page.getInfo()
                                .addElement(new Store.Page.ElementInfo(
                                        elementName, selector, locatorType, locatorValue, elementType));
                        pageStore.addPage(page);
                    }
                }
            }
        }
    }

    /**
     * Проверяем и добавляем страницу если отсутвует
     */
    private void addPage(String url) {
        String pageName = extractPageNameFromUrl(url);
        if (!pageStore.getPages().containsKey(pageName)) {
            Store.Page.Info pageInfo = new Store.Page.Info(pageName, url);
            pageStore.addPage(new Store.Page(pageInfo));
            log.debug("Found new page: {} from URL: {}", pageName, url);
        }
    }

    /**
     * Определяет тип элемента по действию
     */
    private String determineElementType(String tagName) {
        if (tagName == null) return "TypifiedWebElement";
        switch (tagName) {
            case "SELECT":
            case "BUTTON":
            case "DIV":
            case "LI":
            case "LABEL":
                return "Button";
            case "INPUT":
            case "A":
                return "TextInput";
            default:
                return "TypifiedWebElement";
        }
    }

    //    private String getTypeByAction(String type) {
    //        if (type == null) return "TypifiedWebElement";
    //        switch (type) {
    //            case "recordClick":
    //            case "click":
    //                return "Button";
    //            case "recordInput":
    //            case "type":
    //            case "input":
    //                return "TextInput";
    //            case "assert":
    //            case "check":
    //                return "Text";
    //            case "recordFocus":
    //            case "focus":
    //            default:
    //                return "TypifiedWebElement";
    //        }
    //    }

    /**
     * Генерирует Page Object классы только с аннотированными полями
     */
    public Store generatePageObjects(TestSuite testSuite, String outputDir) {
        Store pageStore = extractPagesFromTestSuite(testSuite);
        pageStore.getPages().forEach((key, page) -> page.setFilePath(generatePageObject(page, outputDir)));
        return pageStore;
    }

    /**
     * Генерирует один Page Object только с аннотированными полями
     */
    private String generatePageObject(Store.Page page, String outputDir) {
        Store.Page.Info pageInfo = page.getInfo();
        // Подготовка контекста для шаблона
        Map<String, Object> context = new HashMap<>();
        context.put("pageInfo", pageInfo);
        context.put("packageName", "ru.autotestframework.pages");
        context.put("className", pageInfo.getClassName());
        context.put("date", new Date());
        // Генерация кода
        String javaCode = templateProcessor.processTemplate("page-object.template", context);
        javaCode = formatJavaCode(javaCode);
        // Сохранение файла
        String fileName = pageInfo.getClassName() + ".java";
        String filePath = outputDir + File.separator + "pages" + File.separator + fileName;
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile())) {
                writer.write(javaCode);
            }
        } catch (IOException ioe) {
            throw new AutotestException("Ошибка извлечения Page Object");
        }
        return filePath;
    }
}
