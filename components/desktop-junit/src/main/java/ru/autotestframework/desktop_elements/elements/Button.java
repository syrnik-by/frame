package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

public class Button extends TypifiedDesktopElement implements IReadable, IVerifiable {

    public Button(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns Button's value
     * @return
     */
    @Override
    public String readValue() {
        return getAttribute("Name");
    }

    /**
     * verifies Button
     * @param value to verify
     * @return
     */
    @Override
    public Verifier verify(final String value) {
        return Verifier.of(this, value);
    }

    /**
     * Invokes the element.
     */
    public void invoke() {
        click();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
