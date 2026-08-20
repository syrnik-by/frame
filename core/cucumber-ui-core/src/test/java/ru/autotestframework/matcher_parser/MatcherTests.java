package ru.autotestframework.matcher_parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.autotestframework.Constants;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;

/**
 * Matcher tests.
 */
@Tag("@BackendCore")
class MatcherTests {

    /**
     * Positive tests.
     *
     * @param scale    the scale
     * @param expected the expected
     * @param val      the val
     */
    @ParameterizedTest
    @CsvSource({"4, 1, 1.000", "4, 1.0, 1.0", "3, 1.1, 1.1001"})
    void positiveTests(final String scale, final double expected, final BigDecimal val) {
        System.setProperty(Constants.COMPARISON_DECIMAL_PRECISION_SCALE, scale);
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_CLOSE_TO, expected);
        boolean matchResult = testedMatcher.matches(val);
        assertTrue(matchResult);
    }

    /**
     * Negative tests.
     *
     * @param scale    the scale
     * @param expected the expected
     * @param val      the val
     */
    @ParameterizedTest
    @CsvSource({"4, 1.1, 1.101", "4, 1.1, 1.1001", "5, 1.1, 1.1001", "4, 1, 0.99"})
    void negativeTests(final String scale, final double expected, final BigDecimal val) {
        System.setProperty(Constants.COMPARISON_DECIMAL_PRECISION_SCALE, scale);
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_CLOSE_TO, expected);
        boolean matchResult = testedMatcher.matches(val);
        assertFalse(matchResult);
    }

    /**
     * Test big number.
     */
    @Test
    void testBigNumber() {
        System.setProperty(Constants.COMPARISON_DECIMAL_PRECISION_SCALE, "4");
        Object expectedObject = "111111111111111.2";
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_CLOSE_TO, expectedObject);
        BigDecimal actual = new BigDecimal(expectedObject.toString().concat("0"));
        boolean matchResult = testedMatcher.matches(actual);
        assertTrue(matchResult);
    }

    /**
     * Test big number one hundredth negative.
     */
    @Test
    void testBigNumberOneHundredthNegative() {
        System.setProperty(Constants.COMPARISON_DECIMAL_PRECISION_SCALE, "4");
        Object expectedObject = "111111111111111.2";
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_CLOSE_TO, expectedObject);
        BigDecimal actual = new BigDecimal(expectedObject.toString().concat("01"));
        boolean matchResult = testedMatcher.matches(actual);
        assertFalse(matchResult);
    }

    /**
     * Test big number one hundredth negative equals.
     */
    @Test
    void testBigNumberOneHundredthNegativeEquals() {
        Object expectedObject = "111111111111111.2";
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_EQUAL, expectedObject);
        BigDecimal actual = new BigDecimal(expectedObject.toString().concat("00001"));
        boolean matchResult = testedMatcher.matches(actual);
        assertFalse(matchResult);
    }

    /**
     * Test big number positive equals.
     */
    @Test
    void testBigNumberPositiveEquals() {
        Object expectedObject = "111111111111111.2";
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.BIG_DECIMAL_EQUAL, expectedObject);
        BigDecimal actual = new BigDecimal(expectedObject.toString().concat("00"));
        boolean matchResult = testedMatcher.matches(actual);
        assertTrue(matchResult);
    }

    /**
     * Test big decimal throuth old equals.
     */
    // reason for new Matcher (result should be true)
    @Test
    void testBigDecimalThrouthOldEquals() {
        Object expectedObject = "111111111111111.2";
        Matcher testedMatcher = MatcherParser.getMatcher(MatcherName.EQUAL_TO, expectedObject);
        BigDecimal actual = new BigDecimal(expectedObject.toString().concat("00"));
        boolean matchResult = testedMatcher.matches(actual);
        assertFalse(matchResult);
    }
}
