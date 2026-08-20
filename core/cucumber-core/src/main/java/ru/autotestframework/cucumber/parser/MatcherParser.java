package ru.autotestframework.cucumber.parser;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static ru.autotestframework.Constants.COMPARISON_DECIMAL_PRECISION_SCALE;
import static ru.autotestframework.util.StringUtil.parseArray;
import static ru.autotestframework.util.Validator.exception;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hamcrest.Matcher;

/**
 * Transform Cucumber Text to {@link org.hamcrest.Matcher}
 */
@UtilityClass
public class MatcherParser {
    @Getter
    private static Map<String, IMatcher> matchers = new HashMap<>();

    /**
     * Gets object.
     *
     * @param type   the type
     * @param object the object
     * @return the object
     */
    public static Object getObject(final Type type, final String object) {
        if (object == null) {
            return null;
        }
        switch (type) {
            case STRING:
            case TIMESTAMP:
                return object;
            case BIG_DECIMAL:
                return new BigDecimal(object);
            case BOOLEAN:
                return Boolean.valueOf(object);
            case INT:
                return Integer.parseInt(object);
            case FLOAT:
                return Float.parseFloat(object);
            case DOUBLE:
                return Double.parseDouble(object);
            case STRING_ARRAY:
                return parseArray(object);
            case INT_ARRAY:
                return parseArray(object).stream().map(Integer::parseInt).collect(Collectors.toList());
            case FLOAT_ARRAY:
                return parseArray(object).stream().map(Float::parseFloat).collect(Collectors.toList());
            case UNKNOWN:
                return null;
            default:
                throw exception("Тип {} не найден в списке доступных", type);
        }
    }

    /**
     * Transform object.
     *
     * @param object the object
     * @return the object
     */
    Object transform(Object object) {
        if (object instanceof String) {
            try {
                return Integer.valueOf((String) object);
            } catch (NumberFormatException nfe) {
                try {
                    return Double.valueOf((String) object);
                } catch (NumberFormatException nfe2) {
                }
            }
        }
        return object;
    }

    /**
     * Gets matcher.
     *
     * @param matcherName    the matcher name
     * @param expectedObject the expected object
     * @return the matcher
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Matcher getMatcher(final MatcherName matcherName, final Object expectedObject) {

        switch (matcherName) {
            case CONTAINS_STRING:
                return containsString((String) expectedObject);
            case NOT_CONTAINS_STRING:
                return not(containsString((String) expectedObject));
            case IS_BLANK_STRING:
                return is(blankString());
            case NOT_BLANK_STRING:
                return not(blankString());
            case MATCHES_REGEX_STRING:
                return matchesPattern((String) expectedObject);
            case IS_NULL:
                return is(nullValue());
            case NOT_NULL:
                return is(notNullValue());
            case EQUAL_TO:
                return equalTo(expectedObject);
            case NOT_EQUAL_TO:
                return not(equalTo(expectedObject));
            case BIG_DECIMAL_CLOSE_TO:
                return formBigDecimalCloseToMatcher(expectedObject);
            case BIG_DECIMAL_EQUAL:
                return formBigDecimalEqualsMatcher(expectedObject);
            case GREATER_THAN:
                return greaterThan((Comparable) transform(expectedObject));
            case GREATER_THAN_OR_EQUAL_TO:
                return greaterThanOrEqualTo((Comparable) transform(expectedObject));
            case LESS_THAN:
                return lessThan((Comparable) transform(expectedObject));
            case LESS_THAN_OR_EQUAL_TO:
                return lessThanOrEqualTo((Comparable) transform(expectedObject));
            case IN_BETWEEN:
                return inBetween(expectedObject);
            default:
                return Optional.of(matchers.get(matcherName.label()))
                        .orElseThrow(() -> exception("matcher '{}' не найден в списке доступных: {}", matcherName))
                        .createMatcher(expectedObject);
        }
    }

    private static Matcher<Integer> inBetween(Object expectedObject) {
        List<Integer> leftRight = parseArray((String) expectedObject).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (leftRight.size() != 2) {
            throw new ArithmeticException("Wrong length of array, should be 2 (start & end of section)");
        }
        var left = leftRight.get(0);
        var right = leftRight.get(1);
        if (right.compareTo(left) < 0) {
            throw new ArithmeticException("Wrong section parameters, lower bound is greater than upper");
        }

        return allOf(greaterThanOrEqualTo(left), lessThanOrEqualTo(right));
    }

    /**
     * Метод отвечает за генерацию Matcher-ов для BigDecimal значений. Валидирует числа с точностью
     * 0.5% от значения из Property framework.matcher.decimal.scale
     *
     * @param expectedObject expected value
     * @return Matcher to validate against
     */
    static Matcher formBigDecimalCloseToMatcher(final Object expectedObject) {
        var expectedValue = new BigDecimal((expectedObject).toString());
        int errorPrecision = Integer.getInteger(COMPARISON_DECIMAL_PRECISION_SCALE);
        var divisor = new BigDecimal("2");
        var decimalMultiplicator = new BigDecimal("0.1");
        BigDecimal applicableError = decimalMultiplicator.pow(errorPrecision).divide(divisor);
        return closeTo(expectedValue, applicableError);
    }

    /**
     * Form big decimal equals matcher matcher.
     *
     * @param expectedObject the expected object
     * @return the matcher
     */
    static Matcher formBigDecimalEqualsMatcher(final Object expectedObject) {
        var expectedValue = new BigDecimal((expectedObject).toString());
        var applicableError = new BigDecimal("0.0");
        return closeTo(expectedValue, applicableError);
    }
}
