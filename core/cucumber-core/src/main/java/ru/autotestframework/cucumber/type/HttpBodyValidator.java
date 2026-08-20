package ru.autotestframework.cucumber.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hamcrest.Matcher;

/**
 * Http body validator.
 */
@Getter
@AllArgsConstructor
public class HttpBodyValidator {

    private final String selector;
    private final Matcher<?> matcher;
    private final Object expectedValue;
}
