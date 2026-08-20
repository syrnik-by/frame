package ru.converter.util;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.converter.model.TestCase;
import ru.converter.model.TestSuite;

/**
 * Утилита для парсинга JSON файлов с тестами.
 * Поддерживает два формата:
 * 1. Массив тест-кейсов: [{...}, {...}]
 * 2. Объект TestSuite: {"tests": [...], "suiteName": "..."}
 */
@Slf4j
public class JsonParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * Парсит JSON файл и возвращает TestSuite.
     * Автоматически определяет формат (массив или объект).
     */
    public static TestSuite parseTestSuite(File jsonFile) throws IOException {
        if (!jsonFile.exists()) {
            throw new IOException("JSON file not found: " + jsonFile.getAbsolutePath());
        }
        log.info("Parsing JSON file: {} ({} bytes)", jsonFile.getName(), jsonFile.length());
        // Используем JsonParser для определения формата
        try (com.fasterxml.jackson.core.JsonParser parser = mapper.getFactory().createParser(jsonFile)) {
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.START_ARRAY) {
                // Формат 1: Массив тест-кейсов
                log.debug("Detected JSON array format");
                return parseAsTestCaseArray(parser);
            } else if (firstToken == JsonToken.START_OBJECT) {
                // Формат 2: Объект TestSuite
                log.debug("Detected JSON object format");
                return parseAsTestSuiteObject(jsonFile);
            } else {
                throw new IOException("Invalid JSON format. Expected array or object.");
            }
        }
    }

    /**
     * Парсит JSON как массив тест-кейсов.
     */
    private static TestSuite parseAsTestCaseArray(com.fasterxml.jackson.core.JsonParser parser) throws IOException {
        List<TestCase> testCases = new ArrayList<>();
        int count = 0;
        // Читаем массив
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            try {
                TestCase testCase = mapper.readValue(parser, TestCase.class);
                testCases.add(testCase);
                log.debug("Parsed test case {}", testCase.getName());
            } catch (Exception e) {
                log.warn("Failed to parse test case #{}: {}", count + 1, e.getMessage());
                // Пропускаем проблемный тест-кейс и продолжаем
                parser.skipChildren();
            } finally {
                count++;
            }
        }
        if (testCases.isEmpty()) {
            throw new IOException("No valid test cases found in JSON array");
        }
        return new TestSuite(testCases, "Generated Test Suite");
    }

    /**
     * Парсит JSON как объект TestSuite.
     */
    private static TestSuite parseAsTestSuiteObject(File jsonFile) throws IOException {
        try {
            TestSuite testSuite = mapper.readValue(jsonFile, TestSuite.class);
            // Если suiteName не указан, задаем значение по умолчанию
            if (testSuite.getSuiteName() == null
                    || testSuite.getSuiteName().trim().isEmpty()) {
                testSuite.setSuiteName("Test Suite from JSON");
            }
            log.info(
                    "Successfully parsed TestSuite: {} ({} test cases)",
                    testSuite.getSuiteName(),
                    testSuite.getTestCases() != null ? testSuite.getTestCases().size() : 0);
            return testSuite;
        } catch (Exception e) {
            log.error("Failed to parse as TestSuite object: {}", e.getMessage());
            throw e;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class JsonStats {
        private final String suiteName;
        private final int testCaseCount;
        private final long fileSize;
        private final long lastModified;

        @Override
        public String toString() {
            return String.format(
                    "JsonStats{suite='%s', tests=%d, size=%d bytes, modified=%s}",
                    suiteName, testCaseCount, fileSize, new java.util.Date(lastModified));
        }
    }
}
