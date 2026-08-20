package ru.converter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class TestSuite {

    private List<TestCase> testCases;
    private String suiteName;

    public TestSuite(List<TestCase> testCases, String suiteName) {
        this.testCases = Objects.requireNonNullElseGet(testCases, ArrayList::new);
        this.suiteName = suiteName != null ? suiteName : "Generated Test Suite";
    }
}
