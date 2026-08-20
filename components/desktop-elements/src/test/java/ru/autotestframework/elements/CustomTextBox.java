package ru.autotestframework.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.TextBox;

public class CustomTextBox extends TextBox {

    public CustomTextBox(WebElement wrappedElement, String title) {
        super(wrappedElement, title);
    }
}
