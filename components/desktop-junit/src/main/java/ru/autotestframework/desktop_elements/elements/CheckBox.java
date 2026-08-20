package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;

public class CheckBox extends ToggleButton {

    public CheckBox(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public CheckBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
}
