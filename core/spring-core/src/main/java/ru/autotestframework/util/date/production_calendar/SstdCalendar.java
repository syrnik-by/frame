package ru.autotestframework.util.date.production_calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

/**
 * Sstd calendar.
 */
public class SstdCalendar {

    private Map<LocalDate, DateInfo> days;

    /**
     * Instantiates a new Sstd calendar.
     *
     * @param sstdCalendarDays SstdCalendarDaysInterface. Наследованный от {@link DefaultDays} объект
     */
    public SstdCalendar(SstdCalendarDaysInterface sstdCalendarDays) {
        this.days = sstdCalendarDays.getDays();
    }

    /**
     * The default constructor. Without a local production calendar.
     */
    public SstdCalendar() {
        var defaultDays = new DefaultDays();
        this.days = defaultDays.getDays();
    }

    /**
     * Returns true if the date falls on a weekend (Saturday or Sunday)
     *
     * @param date LocalDate is the date to check
     * @return boolean boolean
     */
    public boolean isWeekEnd(LocalDate date) {
        var dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Returns true if the date does NOT fall on a weekend (Work Week)
     *
     * @param date LocalDate - Date to check
     * @return boolean boolean
     */
    public boolean isWorkWeek(LocalDate date) {
        var dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * Returns true if the date is marked in the production calendar
     * as a Shortened working day (DayType.SHORTDAY)
     *
     * @param date LocalDate - Date for verification
     * @return boolean boolean
     */
    public boolean isShortDay(LocalDate date) {
        return days.containsKey(date) && days.get(date).getType() == DayType.SHORTDAY;
    }

    /**
     * Returns true if the date is marked in the production calendar
     * how is the Working Day (DayType.WORKDAY) or date in the working week
     * and NOT marked as a day off (DayType.HOLIDAY)
     *
     * @param date LocalDate - Date to check
     * @return boolean boolean
     */
    public boolean isWorkDay(LocalDate date) {
        if (days.containsKey(date)) {
            if (days.get(date).getType().equals(DayType.WORKDAY)) return true;
            if (days.get(date).getType().equals(DayType.HOLIDAY)) return false;
        }
        return isWorkWeek(date);
    }

    /**
     * Returns true if the date is marked in the production calendar
     * as a Day Off (DayType.HOLIDAY) or date day off (Saturday or Sunday)
     * and NOT marked as a Working Day (DayType.WORKDAY) or Shortened Working Day (DayType.SHORTDAY)
     *
     * @param date LocalDate - Date to check
     * @return boolean boolean
     */
    public boolean isHoliday(LocalDate date) {
        if (days.containsKey(date)) {
            if (days.get(date).getType().equals(DayType.HOLIDAY)) return true;
            if (days.get(date).getType().equals(DayType.WORKDAY)
                    || days.get(date).getType().equals(DayType.SHORTDAY)) return false;
        }
        return isWeekEnd(date);
    }

    /**
     * Returns the date one day after the end of the calendar day from the date
     *
     * @param date LocalDate parameter - the date of the countdown
     * @param dayInterval int parameter - date in a calendar month
     * @return the LocalDate date after a certain interval
     */
    public LocalDate getDateAfterInterval(LocalDate date, int dayInterval) {
        return date.plusDays(dayInterval);
    }

    /**
     * Returns the date in dayInterval calendar days from date
     *
     * @param date LocalDate - the date of the countdown
     * @param dayInterval int - interval in calendar days
     * @param includeFirstDay boolean - include the current day in the calculation
     * @return LocalDate date after interval
     */
    public LocalDate getDateAfterInterval(LocalDate date, int dayInterval, boolean includeFirstDay) {
        if (includeFirstDay && dayInterval > 0) dayInterval--;
        return date.plusDays(dayInterval);
    }

    /**
     * Returns the nearest business day in dayInterval calendar days from date
     *
     * @param date LocalDate - the date of the countdown
     * @param dayInterval int - interval in BUSINESS days
     * @param includeFirstDay boolean - include the current day in the calculation
     * @return LocalDate work date after interval
     */
    public LocalDate getWorkDateAfterInterval(LocalDate date, int dayInterval, boolean includeFirstDay) {
        if (includeFirstDay && dayInterval > 0) dayInterval--;
        var result = date.plusDays(dayInterval);
        while (isHoliday(result)) result = result.plusDays(1);
        return result;
    }

    /**
     * Returns the nearest business day after the dayInterval of BUSINESS days from date
     *
     * @param date LocalDate - the date of the countdown
     * @param workDayInterval int - interval in WORKING days
     * @param includeFirstDay boolean - include the current day in the calculation
     * @return LocalDate date after work days interval
     */
    public LocalDate getDateAfterWorkDaysInterval(LocalDate date, int workDayInterval, boolean includeFirstDay) {
        if (includeFirstDay && workDayInterval > 0) workDayInterval--;
        for (var i = 0; i < workDayInterval; i++) {
            date = getWorkDateAfterInterval(date, 1, false);
        }
        return date;
    }

    /**
     * Returns information about the specified date (date, type, name of the holiday)
     *
     * @param date LocalDate - date of verification
     * @return DateInfo - information about the specified date {@link DateInfo}
     */
    public DateInfo getDateInfo(LocalDate date) {
        if (days.containsKey(date)) return days.get(date);
        else if (isWorkDay(date)) return new DateInfo(date, DayType.WORKDAY);
        else if (isHoliday(date)) return new DateInfo(date, DayType.HOLIDAY);
        else return null;
    }
}
