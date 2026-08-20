package ru.autotestframework.http_steps;

import io.cucumber.java.BeforeAll;
import java.util.Map;
import ru.autotestframework.cucumber.parser.MatcherParser;

public class Hooks {

    @BeforeAll
    public static void customMatcher() {
        MatcherParser.getMatchers().putAll(Map.of("#custom#", new CustomMatcher(), "#notEqual#", new CustomMatcher2()));
    }
}
