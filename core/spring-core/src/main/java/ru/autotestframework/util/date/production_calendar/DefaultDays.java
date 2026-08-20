package ru.autotestframework.util.date.production_calendar;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Default Production calendar
 */
public class DefaultDays implements SstdCalendarDaysInterface {

    /**
     * Storage for a production calendar with a key search
     */
    private Map<LocalDate, DateInfo> days = new HashMap<>();

    /**
     * The constructor
     */
    public DefaultDays() {
        init();
    }

    /**
     * Initializing an empty calendar
     */
    public void init() {
        // Инициализируем пустой календаоь
    }

    /**
     * Add date to calendar
     *
     * @param date - date
     * @param type - type {@link DayType}
     */
    public void add(String date, DayType type) {
        add(date, type, null);
    }

    /**
     * Add date to calendar
     *
     * @param date - date
     * @param type - type {@link DayType}
     * @param title - name of the holiday
     */
    public void add(String date, DayType type, String title) {
        var localDate = LocalDate.parse(date, ofPattern("yyyy-dd-MM"));
        days.put(localDate, new DateInfo(localDate, type, title));
    }

    /**
     * Returns the production calendar as a Map
     * Key - date, value - {@link DateInfo}
     *
     * @return Map of LocalDate,DateInfo
     */
    public Map<LocalDate, DateInfo> getDays() {
        return days;
    }
}
