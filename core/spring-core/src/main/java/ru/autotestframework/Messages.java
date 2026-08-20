package ru.autotestframework;

import lombok.experimental.UtilityClass;

/**
 * Messages.
 */
@UtilityClass
public class Messages {

    /**
     * The constant BODY_VALIDATOR_EXAMPLE.
     */
    public static final String BODY_VALIDATOR_EXAMPLE = "\nСледует использовать формат '| GPath selector "
            + "| matcher | operand |'\n\n"
            + "Например: \n"
            + "| Envelope.Body.DivideResponse.DivideResult.text() | != | 1 |";

    /**
     * The constant SUPPORTED_SQL_VALIDATORS.
     */
    public static final String SUPPORTED_SQL_VALIDATORS =
            "\nПоддерживаемые форматы: 'value'" + " либо 'type::matcher::value'";

    /**
     * The constant RETRYABLE_STEPS_EXAMPLE.
     */
    public static final String RETRYABLE_STEPS_EXAMPLE = "\nВ сценарии обязательно должен"
            + " присутствовать шаг 'И конец цепочки шагов'.\n\n"
            + "Например:\n"
            + "    И начало цепочки шагов в которой 2 шага должен быть успешными\n"
            + "    ... цепочка шагов любой длины\n"
            + "    И конец цепочки шагов\n";
}
