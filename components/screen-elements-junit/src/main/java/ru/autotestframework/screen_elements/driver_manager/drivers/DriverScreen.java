package ru.autotestframework.screen_elements.driver_manager.drivers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openqa.selenium.WebDriver;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.screen_elements.driver_builder.ScreenDriverBuilder;
import ru.autotestframework.screen_elements.elements.typified.TypifiedScreenElement;
import ru.autotestframework.ui_core.driver_builder.DefaultPropertiesBuilder;
import ru.autotestframework.ui_core.driver_manager.Driver;

/**
 * Driver screen.
 */
@Slf4j
public class DriverScreen extends Driver {

    /**
     * The constant SIMILARITY_THRESHOLD.
     */
    public static final double SIMILARITY_THRESHOLD = 0.75;

    /**
     * Instantiates a new Driver screen.
     *
     * @param path         the path
     * @param propertyPath the property path
     */
    public DriverScreen(final String path, final String propertyPath) {
        super(path, propertyPath);
    }

    /**
     * Gets region.
     *
     * @param text       the text
     * @param searchType the search type
     * @param language   the language
     * @return the region
     */
    public static Region getRegion(String text, int searchType, String language) {
        Region found = null;
        var counter = 0;
        while (found == null && counter < 3) {
            var scr = new Screen();
            Settings.OcrLanguage = language;
            if (searchType == 3) {
                found = scr.findWord(text);
            } else {
                try {
                    found = scr.findText(text);
                } catch (FindFailed ff) {
                    log.warn("Failed to find by text with Native sikuli implementation");
                }
            }
            if (found == null) {
                found = findSubRegion(text, searchType, language);
            }
            counter++;
        }
        return found;
    }

    /**
     * Find sub region region.
     *
     * @param text       the text
     * @param searchType the search type
     * @param language   the language
     * @return the region
     */
    @SneakyThrows
    public static Region findSubRegion(String text, int searchType, String language) {
        return findSubRegion(text, searchType, language, new Screen());
    }

    /**
     * Find sub region region.
     *
     * @param text          the text
     * @param searchType    the search type
     * @param language      the language
     * @param regionContext the region context
     * @return the region
     */
    @SneakyThrows
    public static Region findSubRegion(String text, int searchType, String language, Region regionContext) {
        return findSubRegion(text, searchType, language, regionContext, SIMILARITY_THRESHOLD);
    }

    /**
     * Find sub region region.
     *
     * @param text          the text
     * @param searchType    the search type
     * @param language      the language
     * @param regionContext the region context
     * @param threshold     the threshold
     * @return the region
     */
    @SneakyThrows
    public static Region findSubRegion(
            String text, int searchType, String language, Region regionContext, double threshold) {
        Region region = null;
        Settings.OcrLanguage = language;
        List<Match> words;
        if (searchType == 2) {
            words = regionContext.findLines();
        } else if (searchType == 3) {
            words = regionContext.findWords();
        } else {
            throw new ConfigurationException(
                    "Wrong searchType TessPageIteratorLevel provided, Expected 2(line) or 3" + "(word)");
        }
        Optional<Region> opt = getSimilarRegion(words, text, threshold);
        if (opt.isPresent()) {
            region = opt.get();
        }
        return region;
    }

    private static Optional<Region> getSimilarRegion(List<Match> words, String text, double threshold) {
        words = words.stream()
                .sorted(Comparator.comparing(o -> getTextSimilarity(o.getText().trim(), text)))
                .collect(Collectors.toList());
        for (Match word : words) {
            if (word.getText().contains(text)
                    || getTextSimilarity(word.getText().trim(), text) > threshold) {
                return Optional.of(word);
            }
        }
        return Optional.empty();
    }

    private static Double getTextSimilarity(String text1, String text2) {
        var s = new LevenshteinDistance();
        int minLength = Math.min(text1.length(), text2.length());
        return 1 - s.apply(text1, text2).doubleValue() / minLength;
    }

    @Override
    public String getTypifiedElementClassName() {
        return TypifiedScreenElement.class.getName();
    }

    /**
     * Builds WebDriver with needed configuration
     * @return WebDriver
     */
    @Override
    public WebDriver build() {
        System.setProperty("webdriver.screen.driver", getPath());
        DefaultPropertiesBuilder propertiesBuilder = new DefaultPropertiesBuilder().withProperties(getPropertyPath());
        var screenDriverConfiguration = propertiesBuilder.build();
        return new ScreenDriverBuilder(screenDriverConfiguration).build();
    }
}
