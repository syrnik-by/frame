package ru.autotestframework.util.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.RandomService;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.util.generator.ru.INN;
import ru.autotestframework.util.generator.ru.OGRN;

/**
 * Ru datafaker extension class for template processing
 */
public class FakerRU extends Faker {
    private static final SecureRandom RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    ;
    /**
     * The constant MAX_REGION_ID.
     */
    public static final int MAX_REGION_ID = 85;
    /**
     * The constant MAX_INSPECTION_ID.
     */
    public static final int MAX_INSPECTION_ID = 89;

    private static final String WRONG_PATTERN_MESSAGE = "Cannot generate data because key - '{}' not match pattern"
            + " (\"#\\{([a-z0-9A-Z_.]+)\\s?((?:,?'([^']+)')*)\\}\")";
    private static final Faker FAKER = new Faker(new Locale("ru", "RU"));
    private static final Map<String, IRuDataGenerator> generators_map = new HashMap<>();

    static {
        generators_map.put("innfl", new INNFL());
        generators_map.put("innul", new INNUL());
        generators_map.put("ogrn", new RandomOGRN());
        generators_map.put("ogrnip", new OGRNIP());
        generators_map.put("kpp", new KPP());
        generators_map.put("oktmo", new OKTMO());
        generators_map.put("snils", new SNILS());
    }

    private FakerRU() {}

    /**
     * Instance faker ru.
     *
     * @return the faker ru
     */
    public static FakerRU instance() {
        return new FakerRU();
    }

    /**
     * Generate Random Russian Specific Value based on key (INN, SNILS etc.).
     *
     * @param key name of data type to generate.
     * @return randomly generated appropriate value.
     */
    public static String generate(final String key) {
        if (generators_map.containsKey(key)) {
            return generators_map.get(key).generate();
        } else {
            return generateFaker(key);
        }
    }

    private static String generateFaker(final String key) {
        String generatedValue;
        var fakeValuesService = new FakeValuesService(new Locale("ru", "RU"), new RandomService());
        try {
            generatedValue = fakeValuesService.expression("#{" + key + "}", FAKER);
        } catch (RuntimeException re) {
            throw new AutotestException("Cannot find key - '{}' in Faker data generator  ", re, key);
        }
        if (generatedValue.equals(key)) {
            throw new AutotestException(WRONG_PATTERN_MESSAGE);
        }
        return generatedValue;
    }

    /**
     * Generate a new account number using the BIC and the existing account number.
     * To do this, take a part of the existing invoice up to the 13th character (with the branch code), change the key (9th character) to 0,
     * change the branch code in the invoice to the code of the Far Eastern branch 0700 and add a random 7-digit number instead of the remaining characters.
     * Then we substitute the BIC and calculate the key, which we then substitute into the bill instead of the 9th character.
     *
     * @param bik - BIK
     * @param accountNum - existing account in the database
     * @param branchCode - branch code
     * @return String account number
     */
    public static String getAccountNumber(String bik, String accountNum, String branchCode) {
        var num = String.format(
                "%s%07d",
                accountNum.substring(0, 8) + 0 + branchCode + accountNum.substring(13, 13), RANDOM.nextInt(9999999));
        var key = String.valueOf(getNumKey(bik + num));
        return num.substring(0, 8) + key + num.substring(9);
    }

    /**
     * The method creates a centralized key to meet legal requirements: https://www.cyberforum.ru/java-beginners/thread2620059.html
     *
     * @param number - account number
     * @return a long numeric key
     */
    public static long getNumKey(String number) {
        long summ = 0;
        for (var i = 0; i < number.length(); i++) {
            summ += (long) Character.digit(number.charAt(i), 10)
                    * Character.digit("71371371371371371371371".charAt(i), 10);
        }
        return Math.abs((Math.abs(summ) % 10) * 3) % 10;
    }

    /**
     * Innfl innfl.
     *
     * @return the innfl
     */
    public INNFL innfl() {
        return new INNFL();
    }

    /**
     * Innul innul.
     *
     * @return the innul
     */
    public INNUL innul() {
        return new INNUL();
    }

    /**
     * Kpp kpp.
     *
     * @return the kpp
     */
    public KPP kpp() {
        return new KPP();
    }

    /**
     * Ogrnip ogrnip.
     *
     * @return the ogrnip
     */
    public OGRNIP ogrnip() {
        return new OGRNIP();
    }

    /**
     * Snils snils.
     *
     * @return the snils
     */
    public SNILS snils() {
        return new SNILS();
    }

    /**
     * Ogrn random ogrn.
     *
     * @return the random ogrn
     */
    public RandomOGRN ogrn() {
        return new RandomOGRN();
    }

    /**
     * Oktmo oktmo.
     *
     * @return the oktmo
     */
    public OKTMO oktmo() {
        return new OKTMO();
    }

    /**
     * Innfl.
     */
    public static class INNFL extends INN implements IRuDataGenerator {
        @Override
        public String generate() {
            return generate(
                    Map.of(
                            "n2_12", N1_INN_CONTROL_NUMBERS,
                            "n1_12", N2_INN_CONTROL_NUMBERS),
                    RegistrationNumbers.getRandomRange(111111, 888888),
                    List.of("n2_12", "n1_12"));
        }
    }

    /**
     * Innul.
     */
    public static class INNUL extends INN implements IRuDataGenerator {

        @Override
        public String generate() {
            return generate(
                    Map.of("n1_10", BASE_INN_CONTROL_NUMBERS),
                    RegistrationNumbers.getRandomRange(11111, 88888),
                    List.of("n1_10"));
        }
    }

    /**
     * Random ogrn.
     */
    public static class RandomOGRN extends OGRN implements IRuDataGenerator {

        @Override
        public String generate() {
            return generate(
                    RegistrationNumbers.getSign(),
                    RegistrationNumbers.getRandomRange(11111, 88888),
                    CONTROL_NUMBER_ENTITY);
        }
    }

    /**
     * Ogrnip.
     */
    public static class OGRNIP extends OGRN implements IRuDataGenerator {

        @Override
        public String generate() {
            return generate(3, RegistrationNumbers.getRandomRange(1111111, 8888888), CONTROL_NUMBER_INDIVIDUAL);
        }
    }

    /**
     * Kpp.
     */
    public static class KPP implements IRuDataGenerator {
        @Override
        public String generate() {
            var region = RegistrationNumbers.getRandom(92, 2);
            var inspection = RegistrationNumbers.getRandom(99, 2);
            var numba = RegistrationNumbers.getRandom(999, 3);
            var prichina = getPrichina();
            return region + inspection + prichina + numba;
        }

        private String getPrichina() {
            switch (RegistrationNumbers.getRandom(4)) {
                case 2:
                    return "43";
                case 3:
                    return "44";
                case 4:
                    return "45";
                default:
                    return "01";
            }
        }
    }

    /**
     * Snils.
     */
    public static class SNILS implements IRuDataGenerator {

        private static final int CONTROL_NUM_DIVISOR = 101;
        private static final int SNILS_CALCULATED_VALUE_LENGTH = 9;

        @Override
        public String generate() {
            int one;
            var summ = 0;
            var result = new StringBuilder();
            for (var i = 0; i < SNILS_CALCULATED_VALUE_LENGTH; i++) {
                one = RANDOM.nextInt(10);
                result.append(one);
                summ += one * (SNILS_CALCULATED_VALUE_LENGTH - i);
            }
            if (summ >= 100) {
                if (summ == 100 || summ == CONTROL_NUM_DIVISOR) {
                    summ = 0;
                } else {
                    summ = summ % CONTROL_NUM_DIVISOR;
                }
            }
            result.append(String.format("%02d", summ));
            return result.toString();
        }
    }

    /**
     * Oktmo.
     */
    public static class OKTMO implements IRuDataGenerator {

        @Override
        public String generate() {
            var region = String.format("%02d", RANDOM.nextInt(99));
            var num = String.format("%09d", RANDOM.nextInt(99999));
            return region + num;
        }
    }
}
