package ru.autotestframework.util.generator;

import java.util.Locale;
import net.datafaker.Faker;

/**
 * Registration numbers.
 */
public abstract class RegistrationNumbers {

    private static final Faker FAKER = new Faker(new Locale("ru", "RU"));

    /**
     * Indicates whether the state registration number is assigned.
     *
     * @return is a random valid attribute.
     */
    public static int getSign() {
        return FAKER.random().nextInt(1, 5);
    }

    /**
     * Gets random range.
     *
     * @param min the min
     * @param max the max
     * @return the random range
     */
    public static int getRandomRange(final int min, final int max) {
        return FAKER.number().numberBetween(min, max);
    }

    /**
     * Gets random range.
     *
     * @param min the min
     * @param max the max
     * @param d   the d
     * @return the random range
     */
    public static String getRandomRange(final int min, final int max, final int d) {
        return zeros(FAKER.number().numberBetween(min, max), d);
    }

    /**
     * Gets random.
     *
     * @param d the d
     * @return the random
     */
    public static int getRandom(final int d) {
        return FAKER.random().nextInt(d);
    }

    /**
     * Gets random.
     *
     * @param d   the d
     * @param lng the lng
     * @return the random
     */
    public static String getRandom(final int d, final int lng) {
        return zeros(FAKER.random().nextInt(d), lng);
    }

    /**
     * Form a string from Integer with given count of padding zeros.
     *
     * @param i   integer to pad.
     * @param lng minimal length to pad a string.
     * @return new String
     */
    public static String zeros(final Integer i, final int lng) {
        var str = new StringBuilder(String.valueOf(i));
        var factLength = str.length();
        if (factLength < lng) {
            for (var p = 0; p < (lng - factLength); p++) {
                str.insert(0, "0");
            }
        }
        return str.toString();
    }
}
