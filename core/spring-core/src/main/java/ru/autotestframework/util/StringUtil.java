package ru.autotestframework.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * Utility class for working with Strings
 */
@Slf4j
@UtilityClass
public final class StringUtil {

    /**
     * Parse array list.
     *
     * @param operand the operand
     * @return the list
     */
    public static List<String> parseArray(final String operand) {
        return Splitter.on(",")
                .trimResults(CharMatcher.is('[')
                        .or(CharMatcher.is(']'))
                        .or(CharMatcher.is('"'))
                        .or(CharMatcher.whitespace()))
                .splitToList(operand);
    }

    /**
     * Trim quotes string.
     *
     * @param operand string to outQuote.
     * @return purified string out of any paired quotation symbols.
     */
    public static String trimQuotes(final String operand) {
        List<Character> charactersToTrim = List.of('\'', '\"');
        if (operand == null) {
            return null;
        }
        String trimmedOperand = pairedTrimFrom(operand, charactersToTrim);
        return (CharMatcher.whitespace()).trimFrom(trimmedOperand);
    }

    /**
     * Old trim quotes string.
     *
     * @param operand the operand
     * @return the string
     */
    public static String oldTrimQuotes(final String operand) {
        if (operand == null) {
            return null;
        }
        return CharMatcher.is('"')
                .or(CharMatcher.is('\''))
                .or(CharMatcher.whitespace())
                .trimFrom(operand);
    }

    @Deprecated
    private String pairedTrimFrom(final CharSequence sequence, final List<Character> charactersToTrim) {
        int len = sequence.length();
        var first = 0;
        int last = len - 1;

        while (first < last) {
            if (sequence.charAt(first) == sequence.charAt(last) && charactersToTrim.contains(sequence.charAt(first))) {
                first++;
                last--;
            } else {
                break;
            }
        }
        return sequence.subSequence(first, last + 1).toString();
    }

    /**
     * Format string.
     *
     * @param message the message
     * @param args    the args
     * @return the string
     */
    public static String format(final String message, final Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    /**
     * Gets current date.
     *
     * @return the current date
     */
    public static String getCurrentDate() {
        var currentDate = new Date();
        var format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        return format.format(currentDate);
    }

    /**
     * Make arithmetic operation format string.
     *
     * @param args   the args
     * @param format the format
     * @return the string
     */
    @SneakyThrows
    public static String makeArithmeticOperationFormat(String args, String format) {
        var regex = "\\s+|\\u00a0";
        var sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("JavaScript");
        var replaceSpaceInString = args.replaceAll(regex, "");
        var result = "";
        if (args.contains(".") || args.contains(",")) {
            Double value = (Double) engine.eval(replaceSpaceInString);
            result = String.format(format, value).replace(",", ".");
        } else {
            result = engine.eval(replaceSpaceInString).toString();
        }
        return result;
    }
}
