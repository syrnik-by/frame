package ru.autotestframework.context_functions_supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.util.StringUtil;

/**
 * Context variables processing tests.
 */
@Tag("@BackendCore")
class ContextVariablesProcessingTests {

    /**
     * Test not trimmed string.
     */
    // region old logic tests
    @Test
    void testNotTrimmedString() {
        String testString = "someString";
        String result = StringUtil.trimQuotes(testString);
        assertEquals(testString, result);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test double quoted string.
     */
    @Test
    void testDoubleQuotedString() {
        String testString = "\"someString\"";
        String result = StringUtil.trimQuotes(testString);
        assertEquals("someString", result);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test one quoted string.
     */
    @Test
    void testOneQuotedString() {
        String testString = "'someString'";
        String result = StringUtil.trimQuotes(testString);
        assertEquals("someString", result);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test multiple white space trimming.
     */
    @Test
    void testMultipleWhiteSpaceTrimming() {
        String testString = "  someString        ";
        String result = StringUtil.trimQuotes(testString);
        assertEquals("someString", result);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test multiple white space quoted trimming.
     */
    @Test
    void testMultipleWhiteSpaceQuotedTrimming() {
        String testString = "\"  someString       \"";
        String result = StringUtil.trimQuotes(testString);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test one white space trimming.
     */
    @Test
    void testOneWhiteSpaceTrimming() {
        String testString = " someString";
        String result = StringUtil.trimQuotes(testString);
        assertEquals("someString", result);
        String oldResult = StringUtil.oldTrimQuotes(testString);
        assertEquals(oldResult, result);
    }

    /**
     * Test different quoted string not changed.
     */
    // endregion
    // region fixed behavior tests
    @Test
    void testDifferentQuotedStringNotChanged() {
        String testString = "\"someString'";
        String result = StringUtil.trimQuotes(testString);
        assertEquals(testString, result);
    }

    /**
     * Test semi quoted string not changed.
     */
    @Test
    void testSemiQuotedStringNotChanged() {
        String testString = "\"some\"String";
        String result = StringUtil.trimQuotes(testString);
        assertEquals(testString, result);
    }
    // endregion
}
