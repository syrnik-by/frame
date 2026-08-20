package ru.autotestframework.http_steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Matcher;
import ru.autotestframework.cucumber.parser.IMatcher;

public class CustomMatcher2 implements IMatcher {

    @Override
    public Matcher createMatcher(Object value) {
        return not(equalTo(Integer.valueOf((String) value)));
    }
}
