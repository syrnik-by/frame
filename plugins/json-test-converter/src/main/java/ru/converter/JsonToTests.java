package ru.converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import ru.converter.converters.ToCucumber;
import ru.converter.converters.ToJUnit;
import ru.converter.model.TestSuite;
import ru.converter.pages.Generator;
import ru.converter.pages.Store;
import ru.converter.util.JsonParser;

@Slf4j
public class JsonToTests {

    public static void generate(String inputFile, String outputDir) throws IOException {
        log.info("Starting JSON Test Converter with Page Objects");
        File jsonFile = new File(inputFile);
        if (!jsonFile.exists()) {
            log.error("Input file not found: {}", inputFile);
            return;
        }
        log.info("Input file: {}", jsonFile.getAbsolutePath());
        log.info("Output directory: {}", outputDir);
        TestSuite testSuite = JsonParser.parseTestSuite(jsonFile);
        log.info("Parsed {} test cases from suite: {}", testSuite.getTestCases().size(), testSuite.getSuiteName());
        Files.createDirectories(Paths.get(outputDir));
        log.info("Generate Pages");
        Generator pageGenerator = new Generator();
        Store pageStore = pageGenerator.generatePageObjects(testSuite, outputDir);
        log.info("Generating JUnit tests with Page Objects...");
        ToJUnit junitConverter = new ToJUnit(pageStore);
        pageStore = junitConverter.generateTestSuite(testSuite, outputDir);
        log.info("Generating Cucumber feature file...");
        ToCucumber cucumberConverter = new ToCucumber(pageStore);
        cucumberConverter.generateFeatureFile(testSuite, outputDir);
        log.info("Generation completed successfully!");
        log.info("Output directory: {}", outputDir);
        log.info("Page Objects in: {}/pages/", outputDir);
    }
}
