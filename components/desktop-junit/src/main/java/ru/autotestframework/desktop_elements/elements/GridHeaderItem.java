package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class GridHeaderItem extends TypifiedDesktopElement {

    public GridHeaderItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }
}
