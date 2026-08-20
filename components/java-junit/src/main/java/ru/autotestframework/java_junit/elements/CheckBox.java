package ru.autotestframework.java_junit.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;

public class CheckBox extends TypifiedJavaElement implements IAccessible, IReadable {

    public CheckBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isAccessed() {
        return getSelenideElement().isEnabled();
    }

    @Override
    public boolean isVisible() {
        return getSelenideElement().isDisplayed();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public String readValue() {
        return getSelenideElement().getText();
    }
}
