package ru.autotestframework.java_junit.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

public class TextInput extends TypifiedJavaElement
        implements ICleanable, IWritable, IReadable, IAccessible, IVerifiable {

    public TextInput(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public void clean() {
        getSelenideElement().clear();
    }

    @Override
    public String readValue() {
        return getSelenideElement().getText();
    }

    @Override
    public void write(final String value) {
        getSelenideElement().sendKeys(value);
    }

    @Override
    public boolean isEditable() {
        return isAccessed();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
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
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }
}
