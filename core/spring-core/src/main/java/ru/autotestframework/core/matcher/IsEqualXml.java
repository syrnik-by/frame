package ru.autotestframework.core.matcher;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.placeholder.PlaceholderDifferenceEvaluator;
import ru.autotestframework.util.Validator;

/**
 * Utility class to Compare XML
 */
@RequiredArgsConstructor
public class IsEqualXml extends TypeSafeMatcher<String> {

    private final String expectedXml;

    /**
     * Is equal xml is equal xml.
     *
     * @param expectedXml the expected xml
     * @return the is equal xml
     */
    @Factory
    public static IsEqualXml isEqualXml(final String expectedXml) {
        return new IsEqualXml(expectedXml);
    }

    @Override
    protected boolean matchesSafely(final String actualXmlString) {
        try {
            var diff = DiffBuilder.compare(expectedXml)
                    .withTest(actualXmlString)
                    .ignoreComments()
                    .ignoreWhitespace()
                    .normalizeWhitespace()
                    .checkForSimilar()
                    .withDifferenceEvaluator(new PlaceholderDifferenceEvaluator())
                    .build();
            return !diff.hasDifferences();
        } catch (Exception e) {
            throw Validator.exception("Невозможно сравнить XML файлы", e);
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("XML документ эквивалентен: \n " + expectedXml + " \n");
    }
}
