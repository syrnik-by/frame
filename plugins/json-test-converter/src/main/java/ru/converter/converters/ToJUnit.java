package ru.converter.converters;

import static ru.converter.util.StringUtil.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import ru.converter.model.TestAction;
import ru.converter.model.TestCase;
import ru.converter.model.TestSuite;
import ru.converter.pages.Store;
import ru.converter.template.TemplateProcessor;

@Slf4j
public class ToJUnit {

    private final TemplateProcessor templateProcessor;
    private final Store pageStore;
    private String pageName;
    private final HashMap<String, String> pagesInRun;

    public ToJUnit(Store pageStore) {
        this.templateProcessor = new TemplateProcessor();
        this.pageStore = pageStore;
        this.pagesInRun = new HashMap<>();
    }

    public Store generateTestSuite(TestSuite testSuite, String outputDir) {
        log.info("Generating JUnit tests for suite: {}", testSuite.getSuiteName());
        // Генерация Page Objects
        Map<String, String> pageImports = new HashMap<>();
        log.info("Generating Page Objects...");

        pageStore.getPages().forEach((key, page) -> {
            String className = page.getClassName();
            pageImports.put(className, "import ru.autotestframework.pages." + className + ";");
        });
        // Подготовка контекста для шаблона
        Map<String, Object> context = new HashMap<>();
        context.put("testSuite", testSuite);
        context.put("className", toCamelCase(testSuite.getSuiteName()) + "Tests");
        context.put("testMethods", generateTestMethods(testSuite.getTestCases()));
        context.put("allPages", pagesInRun);
        context.put("pageImports", new ArrayList<>(pageImports.values()));
        context.put("suiteName", testSuite.getSuiteName());
        context.put("packageName", "ru.autotestframework");
        // Генерация кода из шаблона
        String javaCode = templateProcessor.processTemplate("junit-selenide.template", context);
        javaCode = formatJavaCode(javaCode);
        // Сохранение файла
        Path outputPath = Paths.get(outputDir, toCamelCase(testSuite.getSuiteName()) + "Tests.java");
        try {
            Files.createDirectories(outputPath.getParent());
            try (FileWriter writer = new FileWriter(outputPath.toFile())) {
                writer.write(javaCode);
                log.info("Generated JUnit file: {}", outputPath);
            }
        } catch (IOException e) {
            log.error("Unable to create resDir", e);
        }
        return pageStore;
    }

    private List<String> generateTestMethods(List<TestCase> testCases) {
        List<String> methods = new ArrayList<>();
        for (TestCase testCase : testCases) {
            methods.add(generateTestMethod(testCase));
        }
        return methods;
    }

    private String generateTestMethod(TestCase testCase) {
        StringBuilder method = new StringBuilder();
        String methodName = "test" + toCamelCase(testCase.getName());
        this.pageName = extractPageNameFromUrl(testCase.getStartURL());
        pagesInRun.put(pageName, this.pageName.substring(0, 1).toLowerCase() + this.pageName.substring(1));
        String pageClassName = pageName + "Page";
        String pageName = pageClassName.substring(0, 1).toLowerCase() + toCamelCase(pageClassName.substring(1));
        method.append("    @Test\n");
        method.append("    public void ").append(methodName).append("() {\n");
        method.append("        // Test: ").append(testCase.getName()).append("\n");
        method.append("        // ID: ").append(testCase.getId()).append("\n");
        method.append("        \n");
        // Открытие стартовой страницы через Selenide
        String startUrl = testCase.getStartURL() != null ? testCase.getStartURL() : "";
        method.append("        Selenide.open(\"").append(escapeJava(startUrl)).append("\");\n");
        method.append("        \n");
        // Генерация действий
        if (testCase.getActions() != null && !testCase.getActions().isEmpty()) {
            method.append(generatePageActions(testCase.getActions()));
        }
        method.append("    }");
        return method.toString();
    }

    private String generatePageActions(List<TestAction> actions) {
        StringBuilder actionsCode = new StringBuilder();
        for (TestAction action : actions) {
            String pageAction = generatePageAction(action);
            actionsCode.append(pageAction).append("\n");
        }
        return actionsCode.toString();
    }

    private String generatePageAction(TestAction action) {
        if (action == null) {
            return "        System.out.println(\"Action is null\");";
        }
        if (!Objects.equals(action.getAction(), "changeTab")) {
            String elementName =
                    pageStore.getPages().get(this.pageName).findElementFieldNameByLocator(action.getSelector());
            String actionType = action.getAction();
            String pageName = this.pageName.substring(0, 1).toLowerCase() + this.pageName.substring(1);
            if (actionType == null) {
                return "        System.out.println(\"Action type is null\");";
            }
            switch (actionType) {
                case "recordClick":
                case "click":
                    return String.format("        " + pageName + "Page.%s.click();", elementName);
                case "recordInput":
                case "type":
                case "input":
                    String text = action.getInputText() != null ? escapeJava(action.getInputText()) : "";
                    return String.format("        " + pageName + "Page.%s.write(\"%s\");", elementName, text);
                case "assert":
                case "check":
                    String expected = action.getExpected() != null ? escapeJava(action.getExpected()) : "";
                    return String.format(
                            "        " + pageName + "Page.shouldHaveText(\"%s\", \"%s\");", elementName, expected);
                case "changeTab":
                    this.pageName = extractPageNameFromUrl(action.getUrl());
                    pagesInRun.put(pageName, this.pageName.substring(0, 1).toLowerCase() + this.pageName.substring(1));
                    return "";
                case "recordFocus":
                case "focus":
                    return String.format("        " + pageName + "Page.%s.hover();", elementName);
                default:
                    return String.format(
                            "        // Unknown action: %s\n" + "        System.out.println(\"Unknown action: %s\");",
                            actionType, actionType);
            }
        } else {
            this.pageName = extractPageNameFromUrl(action.getUrl());
            pagesInRun.put(pageName, this.pageName.substring(0, 1).toLowerCase() + this.pageName.substring(1));
            return "";
        }
    }
}
