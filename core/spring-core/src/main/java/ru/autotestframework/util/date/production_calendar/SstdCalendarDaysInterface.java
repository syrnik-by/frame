package ru.autotestframework.util.date.production_calendar;

import java.time.LocalDate;
import java.util.Map;

/**
 * The interface for the production calendar
 */
public interface SstdCalendarDaysInterface {

    /**
     * Init.
     */
    void init();

    /**
     * Add.
     *
     * @param date the date
     * @param type the type
     */
    void add(String date, DayType type);

    /**
     * Add.
     *
     * @param date  the date
     * @param type  the type
     * @param title the title
     */
    void add(String date, DayType type, String title);

    /**
     * Gets days.
     *
     * @return the days
     */
    Map<LocalDate, DateInfo> getDays();
}
