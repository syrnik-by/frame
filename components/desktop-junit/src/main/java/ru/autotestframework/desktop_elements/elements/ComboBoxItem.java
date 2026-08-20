package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

public class ComboBoxItem extends TypifiedDesktopElement implements IReadable, IVerifiable {

    public ComboBoxItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public String readValue() {
        return getText();
    }

    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }
}
