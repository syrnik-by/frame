package ru.autotestframework.cucumber.parser;

import static ru.autotestframework.util.Validator.exception;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * enum of text representation of {@link org.hamcrest.Matcher} for using in Cucumber features
 */
@Getter
@RequiredArgsConstructor
public enum MatcherName {
    /**
     * Contains string matcher name.
     */
    CONTAINS_STRING("contains"),
    /**
     * Not contains string matcher name.
     */
    NOT_CONTAINS_STRING("!contains"),
    /**
     * Not blank string matcher name.
     */
    NOT_BLANK_STRING("notBlank"),
    /**
     * Is blank string matcher name.
     */
    IS_BLANK_STRING("isBlank"),
    /**
     * Matches regex string matcher name.
     */
    MATCHES_REGEX_STRING("matchesRegex"),
    /**
     * Not null matcher name.
     */
    NOT_NULL("notNull"),
    /**
     * Is null matcher name.
     */
    IS_NULL("isNull"),
    /**
     * Equal to matcher name.
     */
    EQUAL_TO("=="),
    /**
     * Not equal to matcher name.
     */
    NOT_EQUAL_TO("!="),
    /**
     * Greater than matcher name.
     */
    GREATER_THAN(">"),
    /**
     * Greater than or equal to matcher name.
     */
    GREATER_THAN_OR_EQUAL_TO(">="),
    /**
     * Less than matcher name.
     */
    LESS_THAN("<"),
    /**
     * Less than or equal to matcher name.
     */
    LESS_THAN_OR_EQUAL_TO("<="),
    /**
     * Big decimal close to matcher name.
     */
    BIG_DECIMAL_CLOSE_TO("bigDecimalCloseTo"),
    /**
     * Big decimal equal matcher name.
     */
    BIG_DECIMAL_EQUAL("bigDecimalEquals"),
    /**
     * In between matcher name.
     */
    IN_BETWEEN("inBetween"),
    /**
     * Custom matcher name.
     */
    CUSTOM("#custom#");

    private ThreadLocal<String> label = new ThreadLocal<>();

    /**
     * Gets by.
     *
     * @param matcherSymbol feature-file symbol
     * @return Matcher on given name.
     */
    public static MatcherName getBy(final String matcherSymbol) {
        if (pattern.matcher(matcherSymbol).find()) {
            CUSTOM.setLabel(matcherSymbol);
            return CUSTOM;
        }
        for (var matcher : MatcherName.values()) {
            if (matcher.getSymbol().equalsIgnoreCase(matcherSymbol)) {
                return matcher;
            }
        }
        throw exception("matcher '{}' не найден в списке доступных: '{}'", matcherSymbol, getAvailableMatchers());
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label.set(label);
    }

    /**
     * Label string.
     *
     * @return the string
     */
    public String label() {
        return label.get();
    }

    /**
     * The constant CUSTOM_MATCHER_REGEX.
     */
    public static final String CUSTOM_MATCHER_REGEX = "#[a-zA-Z]{4,}#";
    /**
     * The constant REGEX.
     */
    public static final String REGEX = "(contains|!contains|notBlank|isBlank|matchesRegex"
            + "|notNull|isNull|==|!=|>|>=|<|<=|bigDecimalEquals|bigDecimalCloseTo|inBetween|" + CUSTOM_MATCHER_REGEX
            + "|)";

    /**
     * The constant pattern.
     */
    public static final Pattern pattern = Pattern.compile(CUSTOM_MATCHER_REGEX);

    private final String symbol;

    /**
     * Gets available matchers.
     *
     * @return list of available Matchers.
     */
    public static String getAvailableMatchers() {
        return Stream.of(values()).map(MatcherName::getSymbol).collect(Collectors.joining(", "));
    }

    /**
     * Is string matcher boolean.
     *
     * @return the boolean
     */
    public boolean isStringMatcher() {
        switch (this) {
            case CONTAINS_STRING:
            case NOT_CONTAINS_STRING:
            case NOT_BLANK_STRING:
            case IS_BLANK_STRING:
            case MATCHES_REGEX_STRING:
            case NOT_NULL:
            case IS_NULL:
            case EQUAL_TO:
            case NOT_EQUAL_TO:
                return true;
            default:
                return false;
        }
    }
}
