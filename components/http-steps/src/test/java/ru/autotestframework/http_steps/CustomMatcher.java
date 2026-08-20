package ru.autotestframework.http_steps;

import static org.hamcrest.Matchers.greaterThan;

import org.hamcrest.Matcher;
import ru.autotestframework.cucumber.parser.IMatcher;

public class CustomMatcher implements IMatcher {

    @Override
    public Matcher createMatcher(Object value) {
        return greaterThan(Integer.valueOf((String) value));
    }
}
