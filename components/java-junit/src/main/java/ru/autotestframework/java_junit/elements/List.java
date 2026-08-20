package ru.autotestframework.java_junit.elements;

import java.util.Collection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueSelectable;

public class List extends TypifiedJavaElement implements IAccessible, IMultipleValueSelectable {

    public List(final WebElement wrappedElement, final String title) {
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
    public boolean isMultipleValues() {
        return true;
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public void select(final String value) {
        getSelenideElement()
                .$(By.cssSelector(".::all-items[text*='" + value + "']"))
                .click();
    }

    @Override
    public void selectMultiple(final Collection<?> values) {
        for (Object value : values) {
            select(value.toString());
        }
    }
}
