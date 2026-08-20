package ru.autotestframework.java_junit.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;

public class Tab extends TypifiedJavaElement implements IAccessible, ISelectable {

    public Tab(final WebElement wrappedElement, final String title) {
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
    public void select(final String value) {
        getSelenideElement()
                .$(By.cssSelector(".::all-tabs[text='" + value + "']"))
                .click();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
