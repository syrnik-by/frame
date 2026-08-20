package ru.autotestframework.java_junit.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

public class Label extends TypifiedJavaElement implements IAccessible, IReadable, IVerifiable {

    public Label(final WebElement wrappedElement, final String title) {
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

    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }
}
