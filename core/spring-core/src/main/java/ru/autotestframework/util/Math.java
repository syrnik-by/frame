package ru.autotestframework.util;

import static ru.autotestframework.util.Validator.exception;

import java.util.function.BinaryOperator;

/**
 * The enum Math.
 */
public enum Math {
    /**
     * Add math.
     */
    ADD("Сложить", Integer::sum),
    /**
     * Subtract math.
     */
    SUBTRACT("Вычесть", (a, b) -> a - b),
    /**
     * Multiply math.
     */
    MULTIPLY("Умножить", (a, b) -> a * b),
    /**
     * Divide math.
     */
    DIVIDE("Разделить", (a, b) -> a / b);

    private String name;
    private final BinaryOperator<Integer> operator;

    Math(String name, BinaryOperator<Integer> operator) {
        this.name = name;
        this.operator = operator;
    }

    /**
     * Gets operation.
     *
     * @param name the name
     * @return the operation
     */
    public static BinaryOperator<Integer> getOperation(String name) {
        for (var operator : values()) {
            if (operator.name.equalsIgnoreCase(name)) {
                return operator.operator;
            }
        }
        throw exception("Ошибка определения оператора по имени: {}", name);
    }
}
