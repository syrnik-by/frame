package ru.autotestframework.sql_steps.components;

import static java.util.stream.Collectors.toList;
import static ru.autotestframework.cucumber.parser.Type.STRING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import ru.autotestframework.Messages;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.parser.Type;
import ru.autotestframework.util.Validator;

/**
 * This class need for match expected record or part of record in actual records
 */
public final class ContainsSqlRecords extends TypeSafeMatcher<List<Map<String, Object>>> {

    public static final int EXPR_PART_CONST = 3;
    private final List<Map<String, String>> expectedResults;

    private ContainsSqlRecords(final List<Map<String, String>> expectedResults) {
        this.expectedResults = convertKeysToUpperCase(expectedResults);
    }

    @Factory
    public static ContainsSqlRecords containsSqlRecords(final List<Map<String, String>> expectedRecords) {
        return new ContainsSqlRecords(expectedRecords);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("SQL result contains: \n " + expectedResults + " \n");
    }

    private static String getExpressionPart(final String expression, final int expressionPart) {
        var strings = expression.split("::", -1);
        if (strings.length == EXPR_PART_CONST) {
            return strings[expressionPart - 1];
        } else if (strings.length == 1) {
            return expressionPart == EXPR_PART_CONST ? expression : null;
        } else {
            throw new ExecutionException(
                    "The expression is specified incorrectly {}. {}", expression, Messages.SUPPORTED_SQL_VALIDATORS);
        }
    }

    private static Predicate<Map.Entry<String, String>> containColumn(final Map<String, Object> actualRecord) {
        return expectedColumn -> {
            var columnName = expectedColumn.getKey();
            if (!actualRecord.containsKey(columnName)) {
                return false;
            }

            var actualValue = actualRecord.get(columnName);
            var expectedExpression = expectedColumn.getValue();

            var typeName = getTypeName(expectedExpression);
            var matcherSymbol = getMatcherSymbol(expectedExpression);
            var expectedValue = getExpectedValue(expectedExpression);

            var matcherName = MatcherName.getBy(matcherSymbol);
            var objectType = Type.getByName(typeName);
            var expectedObject = MatcherParser.getObject(objectType, expectedValue);

            var matcher = MatcherParser.getMatcher(matcherName, expectedObject);

            if (actualValue != null && objectType == STRING) {
                return matcher.matches(actualValue.toString());
            } else {
                return matcher.matches(actualValue);
            }
        };
    }

    private static String getTypeName(final String expression) {
        var typeName = getExpressionPart(expression, 1);
        if (typeName == null) {
            return STRING.getTypeName();
        } else if (typeName.isBlank()) {
            return Type.UNKNOWN.getTypeName();
        } else {
            return typeName;
        }
    }

    private static String getMatcherSymbol(final String expression) {
        var matcherSymbol = getExpressionPart(expression, 2);
        if (matcherSymbol == null) {
            return MatcherName.EQUAL_TO.getSymbol();
        } else {
            return matcherSymbol;
        }
    }

    private static String getExpectedValue(final String expression) {
        return getExpressionPart(expression, EXPR_PART_CONST);
    }

    public static <V> List<Map<String, V>> convertKeysToUpperCase(final List<Map<String, V>> resultMapList) {
        return resultMapList.stream()
                .map(mapRecord -> mapRecord.entrySet().stream()
                        .collect(
                                HashMap<String, V>::new,
                                (m, v) -> m.put(v.getKey().toUpperCase(), v.getValue()),
                                HashMap::putAll))
                .collect(toList());
    }

    @Override
    protected boolean matchesSafely(final List<Map<String, Object>> records) {
        var actualRecords = convertKeysToUpperCase(records);
        expectedResults.stream().map(Map::entrySet).forEach(expectedRecord -> {
            var recordFound = actualRecords.stream()
                    .anyMatch(actualRecord -> expectedRecord.stream().allMatch(containColumn(actualRecord)));
            Validator.assertThat(
                    recordFound, "SQL Record '{}' wasn't found or didn't match filter criteria", expectedRecord);
        });

        return true;
    }
}
