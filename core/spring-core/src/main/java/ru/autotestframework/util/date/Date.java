package ru.autotestframework.util.date;

import java.util.Arrays;
import java.util.stream.Stream;
import ru.autotestframework.core.exception.ExecutionException;

/**
 * The enum Date.
 */
public enum Date {
    /**
     * Days date.
     */
    DAYS("день", "дня", "дней"),
    /**
     * Weeks date.
     */
    WEEKS("неделя", "недель", "недель", "недели"),
    /**
     * Months date.
     */
    MONTHS("месяц", "месяца", "месяцев"),
    /**
     * Years date.
     */
    YEARS("год", "года", "лет");

    private final String[] variants;

    Date(String... variants) {
        this.variants = variants;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return variants[0];
    }

    /**
     * Determine date.
     *
     * @param name the name
     * @return the date
     */
    public static Date determine(final String name) {
        return Stream.of(values())
                .filter(v -> {
                    String[] variants = v.variants;
                    Arrays.sort(variants);
                    return Arrays.binarySearch(variants, name) > -1;
                })
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Undefined name '{}' for Date enum", name));
    }
}
