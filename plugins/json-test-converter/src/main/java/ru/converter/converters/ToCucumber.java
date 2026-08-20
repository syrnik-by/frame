package ru.converter.converters;

import static ru.converter.util.StringUtil.extractPageNameFromUrl;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;
import ru.converter.model.TestAction;
import ru.converter.model.TestCase;
import ru.converter.model.TestSuite;
import ru.converter.pages.Store;
import ru.converter.template.TemplateProcessor;

@Slf4j
public class ToCucumber {

    private final TemplateProcessor templateProcessor;
    private final Store pageStore;
    private String pageName;
    private final AtomicInteger counter;

    public ToCucumber(Store pageStore) {
        this.templateProcessor = new TemplateProcessor();
        this.pageStore = pageStore;
        this.counter = new AtomicInteger(0);
    }

    public void generateFeatureFile(TestSuite testSuite, String outputDir) {
        log.info("Generating Cucumber feature file for suite: {}", testSuite.getSuiteName());
        String featureName = toFeatureName(testSuite.getSuiteName());
        // Подготовка контекста для шаблона
        Map<String, Object> context = new HashMap<>();
        context.put("testSuite", testSuite);
        context.put("featureName", featureName);
        context.put("scenarios", generateScenarios(testSuite.getTestCases()));
        // Генерация feature файла
        String featureContent = templateProcessor.processTemplate("cucumber-feature.template", context);
        featureContent = formatFeatureFile(featureContent);
        // Сохранение файла
        Path outputPath = Paths.get(outputDir, featureName + ".feature");
        try {
            try (FileWriter writer = new FileWriter(outputPath.toFile())) {
                writer.write(featureContent);
                log.info("Generated feature file: {}", outputPath);
            }
        } catch (IOException ioe) {
            throw new AutotestException("Ошибка Generated feature file");
        }
    }

    private List<String> generateScenarios(List<TestCase> testCases) {
        List<String> scenarios = new ArrayList<>();
        for (TestCase testCase : testCases) {
            scenarios.add(generateScenario(testCase));
        }
        return scenarios;
    }

    private String generateScenario(TestCase testCase) {
        StringBuilder scenario = new StringBuilder();
        this.pageName = extractPageNameFromUrl(testCase.getStartURL());
        scenario.append("  Сценарий:").append(testCase.getName()).append("\n");
        scenario.append("    И открыть ссылку '")
                .append(escapeForFeature(testCase.getStartURL()))
                .append("'\n");
        scenario.append("    И перейти на страницу '")
                .append(pageName)
                .append("Page")
                .append("'\n");
        // Генерация шагов для действий
        if (testCase.getActions() != null) {
            for (TestAction action : testCase.getActions()) {
                scenario.append("    ").append(generateCucumberStep(action)).append("\n");
            }
        }
        return scenario.toString();
    }

    private String generateCucumberStep(TestAction action) {
        if (action == null || action.getAction() == null) {
            return "# Неизвестное действие";
        }
        if (!Objects.equals(action.getAction(), "changeTab")) {
            String elementName = pageStore.getPages().get(this.pageName).findElementNameByLocator(action.getSelector());
            switch (action.getAction()) {
                case "recordClick":
                case "click":
                    return String.format("И нажать на элемент '%s'", elementName);
                case "recordInput":
                case "type":
                case "input":
                    String text = action.getInputText() != null ? action.getInputText() : "";
                    return String.format("И заполнить поле '%s' значением '%s'", elementName, escapeForFeature(text));
                case "assert":
                case "check":
                    String expected = action.getExpected() != null ? action.getExpected() : "";
                    return String.format(
                            "И проверить что элемент '%s' содержит текст '%s'",
                            elementName, escapeForFeature(expected));
                case "recordFocus":
                case "focus":
                    return String.format("И установить фокус на элемент '%s'", elementName);
                default:
                    return String.format("# Неизвестное действие: %s", action.getAction());
            }
        } else {
            String url = action.getUrl() != null ? action.getUrl() : "";
            pageName = extractPageNameFromUrl(url);
            return String.format("\n    И перейти на страницу '%s'", pageName);
        }
    }

    private String toFeatureName(String suiteName) {
        if (suiteName == null) return "feature" + counter.incrementAndGet();
        return suiteName.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");
    }

    private String escapeForFeature(String text) {
        if (text == null) return "";
        return text.replace("'", "\\'");
    }

    private String formatFeatureFile(String content) {
        if (content == null) return "";
        String[] lines = content.split("\n");
        StringBuilder formatted = new StringBuilder();
        int emptyLineCount = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                emptyLineCount++;
                if (emptyLineCount <= 1) {
                    formatted.append("\n");
                }
            } else {
                emptyLineCount = 0;
                formatted.append(line).append("\n");
            }
        }
        return formatted.toString().trim();
    }
}
