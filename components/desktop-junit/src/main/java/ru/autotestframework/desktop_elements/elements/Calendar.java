package ru.autotestframework.desktop_elements.elements;

import java.time.LocalDateTime;
import java.util.List;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class Calendar extends TypifiedDesktopElement {

    private static final String CALENDAR_SELECTED_DATES = "calendarSelectedDates";
    private static final String CALENDAR_SELECT_DATE = "calendarSelectDate";
    private static final String CALENDAR_ADD_TO_SELECTION = "calendarAddToSelection";

    public Calendar(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the selected dates in the calendar. For Win32 multiple selection calendar the returned array has two
     * dates, the first date and the last date of the selected range. For WPF calendar the returned array contains
     * all selected dates.
     *
     * @return all selected dates
     */
    public List<LocalDateTime> selectedDates() {
        var response = callVoidCommand(CALENDAR_SELECTED_DATES);
        return createLocalDateTimeFromResponse(response);
    }

    /**
     * Deselects other selected dates and selects the specified date.
     *
     * @param dateTime dateTime
     */
    public void selectDate(final LocalDateTime dateTime) {
        callValueCommand(CALENDAR_SELECT_DATE, dateTime);
    }

    /**
     * For WPF calendar with SelectionMode="MultipleRange" this method adds the specified date to current selection.
     * For any other type of SelectionMode it deselects other selected dates and selects the specified date.
     * This method is supported only for WPF calendar.
     *
     * @param dateTime dateTime
     */
    public void addToSelection(final LocalDateTime dateTime) {
        callValueCommand(CALENDAR_ADD_TO_SELECTION, dateTime);
    }
}
