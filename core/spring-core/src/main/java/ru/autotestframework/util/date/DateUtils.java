package ru.autotestframework.util.date;

import static ru.autotestframework.Constants.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import jdk.jfr.Description;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Date utils.
 */
@UtilityClass
@Slf4j
@Description("Вспомогательный класс для взаимодействия с датами")
public class DateUtils {

    private static final String FIRST_DAY_OF_THE_MONTH = "первый";
    private static final String LAST_DAY_OF_THE_MONTH = "последний";
    private final String MASK_DATE_DD_MM_YYYY = "dd.MM.yyyy";
    private final String MASK_DATE_YYYY_MM_DD = "yyyy.MM.dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(MASK_DATE_DD_MM_YYYY);

    /**
     * Returns the date of the first||last day of the selected month in the original format
     *
     * @param date permanent dates
     * @param position selectable day of the month (first||last)
     * @return returns the expected date in the original format
     */
    public static String getFirstOrLastDayOfMonth(final String date, final String position) {
        var maskDate = "";
        LocalDate newDate = stringToDate(date, MASK_DATE_DD_MM_YYYY);
        if (date.matches(DATE_DD_MM_YYYY)) {
            maskDate = MASK_DATE_DD_MM_YYYY;
        } else {
            maskDate = MASK_DATE_YYYY_MM_DD;
        }
        if (position.equals(FIRST_DAY_OF_THE_MONTH)) {
            newDate = newDate.with(TemporalAdjusters.firstDayOfMonth());
        } else if (position.equals(LAST_DAY_OF_THE_MONTH)) {
            newDate = newDate.with(TemporalAdjusters.lastDayOfMonth());
        } else {
            throw new AutotestException("Некорректное значение дня месяца:" + position);
        }
        return newDate.format(DateTimeFormatter.ofPattern(maskDate));
    }

    /**
     * Returns the date in LocalDate format using the yyyy-MM-dd mask
     *
     * @param variable date value in dd. MM.yyyy format and as: today today + 3 months 11.04.2001 11.04.2001 + 2 days context.get(date) + "+ 1 year"
     * @return the local date from var
     */
    public static LocalDate getLocalDateFromVar(String variable) {
        String date = variable.startsWith("сегодня")
                ? variable.replace("сегодня", LocalDate.now().format(DATE_FORMATTER))
                : variable;
        String[] args = date.split(" ");
        var localDate = formatStringToDate(args[0]);
        if (args.length > 1 && args[2] != null && args[3] != null) {
            var period = applyPeriod(Integer.parseInt(args[2]), args[3]);
            if (args[1].equalsIgnoreCase("+")) {
                localDate = localDate.plus(period);
            } else {
                localDate = localDate.minus(period);
            }
        }
        return localDate;
    }

    /**
     * Returns the period
     *
     * @param count numeric value of the period
     * @param arg values day/week/month/year
     */
    private static Period applyPeriod(int count, String arg) {
        Period result = null;
        switch (Date.determine(arg.toLowerCase())) {
            case DAYS:
                result = Period.ofDays(count);
                break;
            case WEEKS:
                result = Period.ofWeeks(count);
                break;
            case MONTHS:
                result = Period.ofMonths(count);
                break;
            case YEARS:
                result = Period.ofYears(count);
                break;
            default:
                break;
        }
        if (result == null) throw new AutotestException("Период: " + arg + " не найден");
        return result;
    }

    /**
     * Returns a String date type variable using the mask format "dd.MM.yyyy"
     *
     * @param date date
     * @return the local date
     */
    public static LocalDate formatStringToDate(String date) {
        return stringToDate(date, MASK_DATE_DD_MM_YYYY);
    }

    /**
     * Returns a variable of the String date type according to the mask format in the String type
     *
     * @param date date
     * @param mask date format mask
     */
    private static LocalDate stringToDate(String date, String mask) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(mask));
    }

    /**
     * Returns a variable of type LocalDate date in the format of the mask mask in the String type
     *
     * @param date date
     * @param mask date format mask
     * @return the string
     */
    public static String dateToString(LocalDate date, String mask) {
        var dtf = DateTimeFormatter.ofPattern(mask);
        return dtf.format(date);
    }

    /**
     * Returns a variable of type LocalDateTime date in the format of the mask mask in the String type
     *
     * @param date date
     * @param mask date format mask
     * @return the string
     */
    public static String dateToString(LocalDateTime date, String mask) {
        var dtf = DateTimeFormatter.ofPattern(mask);
        return dtf.format(date);
    }

    /**
     * Converts the date string to a different format (according to the pattern).
     *
     * @param dateAsText String containing the date
     * @param oldPattern Mutable string format
     * @param newPattern Format of the modified string
     * @return Date string in modified format
     */
    public static String changeDateTimeFormatForString(
            final String dateAsText, final String oldPattern, final String newPattern) {
        return DateTimeFormatter.ofPattern(newPattern, Locale.ENGLISH)
                .format(LocalDate.parse(dateAsText, DateTimeFormatter.ofPattern(oldPattern, Locale.ENGLISH)));
    }
}
