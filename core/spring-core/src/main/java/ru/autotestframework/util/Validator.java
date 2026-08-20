package ru.autotestframework.util;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.experimental.UtilityClass;
import org.hamcrest.Matcher;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Validator.
 */
@UtilityClass
public class Validator {

    /**
     * Check download folder.
     *
     * @param destination    the destination
     * @param expectedFolder the expected folder
     */
    public static void checkDownloadFolder(final String destination, final String expectedFolder) {
        checkThat(
                destination.replace('/', '\\').startsWith(expectedFolder.replace('/', '\\')),
                "File download destination should be '{}'",
                expectedFolder);
    }

    /**
     * Check that.
     *
     * @param condition the condition
     * @param message   the message
     * @param args      the args
     */
    public static void checkThat(final boolean condition, final String message, final Object... args) {
        if (!condition) {
            throw exception(message, args);
        }
    }

    /**
     * Not null.
     *
     * @param object  the object
     * @param message the message
     */
    public static void notNull(final Object object, final String message) {
        if (object == null) {
            throw exception(message);
        }
    }

    /**
     * Not blank.
     *
     * @param string  the string
     * @param message the message
     */
    public static void notBlank(final String string, final String message) {
        if (isBlank(string)) {
            throw exception(message);
        }
    }

    /**
     * All not blank.
     *
     * @param message the message
     * @param strings the strings
     */
    public static void allNotBlank(final String message, final String... strings) {
        if (isAnyBlank(strings)) {
            throw exception(message);
        }
    }

    /**
     * Assert that.
     *
     * @param assertion the assertion
     * @param message   the message
     * @param args      the args
     */
    /*
    Asserts
     */
    public static void assertThat(final boolean assertion, final String message, final Object... args) {
        if (!assertion) {
            throwAssert(message, args);
        }
    }

    /**
     * Assert that.
     *
     * @param <T>     the type parameter
     * @param actual  the actual
     * @param matcher the matcher
     */
    public static <T> void assertThat(final T actual, final Matcher<? super T> matcher) {
        org.hamcrest.MatcherAssert.assertThat("", actual, matcher);
    }

    /**
     * Assert that.
     *
     * @param <T>     the type parameter
     * @param actual  the actual
     * @param matcher the matcher
     * @param message the message
     * @param args    the args
     */
    public static <T> void assertThat(
            final T actual, final Matcher<? super T> matcher, final String message, final Object... args) {
        if (!matcher.matches(actual)) {
            throwAssert(message, args);
        }
    }

    /**
     * Throw assert.
     *
     * @param message the message
     * @param args    the args
     */
    public static void throwAssert(final String message, final Object... args) {
        throw new AssertionError(StringUtil.format(message, args));
    }

    /**
     * Exception autotest exception.
     *
     * @param message the message
     * @param args    the args
     * @return the autotest exception
     */
    /*
    Exception and Errors
     */
    public static AutotestException exception(final String message, final Object... args) {
        return new AutotestException(message, args);
    }

    /**
     * Exception autotest exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     * @return the autotest exception
     */
    public static AutotestException exception(final String message, final Throwable cause, final Object... args) {
        return new AutotestException(message, cause, args);
    }

    /**
     * Try some Runnable, if failed throw AutotestException with given text.
     *
     * @param runnable Runnable.
     * @param message  given text.
     * @param args     ordered placeholders for messages.
     */
    public static void tryOrThrow(final Runnable runnable, final String message, final Object... args) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw exception(message, e, args);
        }
    }

    /**
     * Try some Runnable, if failed throw AssertionError with given text.
     *
     * @param runnable Runnable.
     * @param message  given text.
     * @param args     ordered placeholders for messages.
     */
    public static void tryOrAssertion(final Runnable runnable, final String message, final Object... args) {
        try {
            runnable.run();
        } catch (Exception e) {
            throwAssert(message, e, args);
        }
    }

    /**
     * Try some Runnable, if failed return true
     *
     * @param runnable Runnable.
     * @return true if succeeded, false otherwise.
     */
    public static boolean hasIgnoreWhenTry(final Runnable runnable) {
        var ignore = false;
        try {
            runnable.run();
        } catch (Exception e) {
            ignore = true;
        }
        return ignore;
    }
}
