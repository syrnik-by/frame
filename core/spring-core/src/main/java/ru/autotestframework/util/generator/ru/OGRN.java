package ru.autotestframework.util.generator.ru;

import static ru.autotestframework.util.generator.RegistrationNumbers.getRandomRange;

/**
 * Ogrn.
 */
public abstract class OGRN {
    /**
     * The constant CONTROL_NUMBER_ENTITY.
     */
    public static final long CONTROL_NUMBER_ENTITY = 11L;
    /**
     * The constant CONTROL_NUMBER_INDIVIDUAL.
     */
    public static final long CONTROL_NUMBER_INDIVIDUAL = 13L;

    private static final int MAX_REGION_ID = 85;
    private static final int MAX_INSPECTION_ID = 89;

    /**
     * Generate string.
     *
     * @param sign    type.
     * @param number  generated id
     * @param control control value
     * @return Valid OGRN number for given parameters.
     */
    public String generate(final int sign, final int number, final long control) {
        var ogrn = new StringBuilder();
        var year = getRandomRange(10, 25);
        var region = getRandomRange(10, MAX_REGION_ID);
        var inspection = getRandomRange(10, MAX_INSPECTION_ID);
        for (var i : new int[] {sign, year, region, inspection, number}) {
            ogrn.append(i);
        }
        long controlNumber = Long.parseLong(ogrn.toString()) % control;
        controlNumber = controlNumber % 10;
        return ogrn + String.valueOf(controlNumber);
    }
}
