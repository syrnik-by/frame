package ru.autotestframework.cucumber.parser;

import org.hamcrest.Matcher;

/**
 * Matcher.
 */
public interface IMatcher {
    /**
     * Create matcher.
     *
     * @param value the value
     * @return the matcher
     */
    Matcher createMatcher(Object value);
}
