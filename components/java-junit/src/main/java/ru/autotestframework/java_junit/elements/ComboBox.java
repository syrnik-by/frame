package ru.autotestframework.java_junit.elements;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;

public class ComboBox extends TypifiedJavaElement implements IAccessible, IReadable, ISelectable {
    public static final String LIST_LIST_ITEM_XPATH = "./List/ListItem";

    public ComboBox(WebElement wrappedElement, String title) {
        super(wrappedElement, title);
    }

    public boolean isAccessed() {
        return this.getSelenideElement().isEnabled();
    }

    public boolean isVisible() {
        return this.getSelenideElement().isDisplayed();
    }

    public boolean isFixStateValue() {
        return false;
    }

    public String readValue() {
        return this.getSelenideElement().getText();
    }

    public void select(String value) throws ElementInteractionException {
        this.expand();
        this.sendKeys(new CharSequence[] {value});
        this.sendKeys(new CharSequence[] {Keys.ENTER});
    }

    public void expand() {
        this.click();
    }
}
