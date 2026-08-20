package ru.autotestframework.desktop_elements.elements;

import java.time.LocalDateTime;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DateTimePicker extends TypifiedDesktopElement {

    private static final String DATE_TIME_PICKER_GET_DATE = "dateTimePickerGetDate";
    private static final String DATE_TIME_PICKER_SET_DATE = "dateTimePickerSetDate";

    public DateTimePicker(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the selected date in the DateTimePicker.
     * For Win32, setting SelectedDate to null will uncheck the DateTimePicker control and disable it.
     * Also for Win32, if the control is unchecked then SelectedDate will return null.
     *
     * @return Current date
     */
    public LocalDateTime getDate() {
        var response = callVoidCommand(DATE_TIME_PICKER_GET_DATE);
        return parseDateTime(response.getValue().toString());
    }

    /**
     * Sets the selected date in the DateTimePicker.
     * For Win32, setting SelectedDate to null will uncheck the DateTimePicker control and disable it.
     * Also for Win32, if the control is unchecked then SelectedDate will return null.
     *
     * @param dateTime date that will be set
     */
    public void setDate(final LocalDateTime dateTime) {
        callValueCommand(DATE_TIME_PICKER_SET_DATE, dateTime);
    }
}
