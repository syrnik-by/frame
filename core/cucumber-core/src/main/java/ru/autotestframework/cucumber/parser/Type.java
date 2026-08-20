package ru.autotestframework.cucumber.parser;

import static ru.autotestframework.Constants.BOOLEAN_REGEX;
import static ru.autotestframework.Constants.FLOAT_ARRAY_REGEX;
import static ru.autotestframework.Constants.FLOAT_REGEX;
import static ru.autotestframework.Constants.INT_ARRAY_REGEX;
import static ru.autotestframework.Constants.INT_REGEX;
import static ru.autotestframework.Constants.NOT_USED;
import static ru.autotestframework.Constants.STRING_ARRAY_REGEX;
import static ru.autotestframework.Constants.STRING_REGEX;
import static ru.autotestframework.Constants.TIMESTAMP_REGEX;
import static ru.autotestframework.util.Validator.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Type.
 */
@RequiredArgsConstructor
public enum Type {
    /**
     * String type.
     */
    STRING("string", STRING_REGEX),
    /**
     * Big decimal type.
     */
    BIG_DECIMAL("bigdecimal", FLOAT_REGEX),
    /**
     * Timestamp type.
     */
    TIMESTAMP("timestamp", TIMESTAMP_REGEX),
    /**
     * Boolean type.
     */
    BOOLEAN("boolean", BOOLEAN_REGEX),
    /**
     * Int type.
     */
    INT("int", INT_REGEX),
    /**
     * Float type.
     */
    FLOAT("float", FLOAT_REGEX),
    /**
     * Double type.
     */
    DOUBLE("double", FLOAT_REGEX),
    /**
     * String array type.
     */
    STRING_ARRAY(NOT_USED, STRING_ARRAY_REGEX),
    /**
     * Int array type.
     */
    INT_ARRAY(NOT_USED, INT_ARRAY_REGEX),
    /**
     * Float array type.
     */
    FLOAT_ARRAY(NOT_USED, FLOAT_ARRAY_REGEX),
    /**
     * Unknown type.
     */
    UNKNOWN("unknown", NOT_USED);

    @Getter
    private final String typeName;

    private final String operandPattern;

    /**
     * return Type on given Name.
     *
     * @param typeName type
     * @return return Type
     */
    public static Type getByName(final String typeName) {
        for (var type : values()) {
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw exception("Ошибка определения типа по имени: {}", typeName);
    }

    /**
     * return Type on given Pattern.
     *
     * @param operand pattern
     * @return return Type
     */
    public static Type getByOperand(final String operand) {
        for (var type : values()) {
            if (operand == null) {
                return UNKNOWN;
            } else if (operand.matches(type.operandPattern)) {
                return type;
            }
        }
        return STRING;
    }
}
