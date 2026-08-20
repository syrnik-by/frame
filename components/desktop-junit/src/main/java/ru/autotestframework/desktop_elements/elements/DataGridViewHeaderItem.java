package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DataGridViewHeaderItem extends TypifiedDesktopElement {

    DataGridViewHeaderItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public DataGridViewHeaderItem(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
}
