package ru.autotestframework.util.generator.ru;

import static java.util.stream.Collectors.joining;
import static ru.autotestframework.util.generator.RegistrationNumbers.getRandomRange;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Inn.
 */
public abstract class INN {
    private static final int MAX_REGION_ID = 85;
    private static final int MAX_INSPECTION_ID = 89;
    private static final int CONTROL_NUM_11TH = 7;
    private static final int CONTROL_NUM_12TH = 3;
    private static final int INN_CONTROL_SUM_DIVISOR = 11;
    /**
     * The constant BASE_INN_CONTROL_NUMBERS.
     */
    protected static final ArrayList<Integer> BASE_INN_CONTROL_NUMBERS =
            new ArrayList<>(List.of(2, 4, 10, 3, 5, 9, 4, 6, 8));
    /**
     * The constant N1_INN_CONTROL_NUMBERS.
     */
    protected static final List<Integer> N1_INN_CONTROL_NUMBERS = Stream.concat(
                    Stream.of(CONTROL_NUM_11TH), BASE_INN_CONTROL_NUMBERS.stream())
            .collect(Collectors.toList());

    /**
     * The constant N2_INN_CONTROL_NUMBERS.
     */
    protected static final List<Integer> N2_INN_CONTROL_NUMBERS = Stream.concat(
                    Stream.of(CONTROL_NUM_12TH), N1_INN_CONTROL_NUMBERS.stream())
            .collect(Collectors.toList());

    /**
     * Random numbers linked list.
     *
     * @param number the number
     * @return the linked list
     */
    protected static LinkedList<Integer> randomNumbers(final Integer number) {
        var innGen = new StringBuilder();
        var region = getRandomRange(10, MAX_REGION_ID);
        var inspection = getRandomRange(10, MAX_INSPECTION_ID);
        for (var i : new int[] {region, inspection, number}) {
            innGen.append(i);
        }
        return innGen.chars().boxed().map(Character::getNumericValue).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Gets inn control sum.
     *
     * @param innControlType the inn control type
     * @param nums           the nums
     * @param type           the type
     * @return the inn control sum
     */
    protected static int getInnControlSum(
            final Map<String, List<Integer>> innControlType, final LinkedList<Integer> nums, final String type) {
        var n = 0;
        List<Integer> l = innControlType.get(type);
        for (var i = 0; i < l.size(); i++) {
            n += nums.get(i) * l.get(i);
        }
        return n % INN_CONTROL_SUM_DIVISOR % 10;
    }

    /**
     * Generate string.
     *
     * @param innControlType the inn control type
     * @param number         the number
     * @param types          the types
     * @return the string
     */
    public String generate(
            final Map<String, List<Integer>> innControlType, final int number, final List<String> types) {
        LinkedList<Integer> nums = randomNumbers(number);
        types.forEach(type -> nums.add(INN.getInnControlSum(innControlType, nums, type)));
        return nums.stream().map(Object::toString).collect(joining(""));
    }
}
