package ru.autotestframework.util.date.production_calendar;

import java.time.LocalDate;

/**
 * Date Information
 */
public class DateInfo {

    /**
     * Date
     */
    private LocalDate date;

    /**
     * Enum {@link DayType} - date type
     * Weekend/holiday, shortened working day, working day
     */
    private DayType type;

    /**
     * Name of the holiday
     */
    private String title;

    /**
     * Constructor
     *
     * @param date LocalDate - date
     * @param type Enum {@link DayType} - type
     */
    public DateInfo(LocalDate date, DayType type) {
        this.date = date;
        this.type = type;
    }

    /**
     * Constructor
     *
     * @param date LocalDate - date
     * @param type Enum {@link DayType} - type
     * @param title String - name of the holiday
     */
    public DateInfo(LocalDate date, DayType type, String title) {
        this.date = date;
        this.type = type;
        this.title = title;
    }

    // getters and setters

    /**
     * Gets date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public DayType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(DayType type) {
        this.type = type;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
