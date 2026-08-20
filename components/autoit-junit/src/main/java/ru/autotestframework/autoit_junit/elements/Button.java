package ru.autotestframework.autoit_junit.elements;

import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

/**
 * Button.
 */
public class Button extends TypifiedAutoItElement implements IAccessible, IReadable, IVerifiable {

    /**
     * Instantiates a new Button.
     *
     * @param winTitle the win title
     * @param title    the title
     * @param control  the control
     */
    public Button(String winTitle, String title, String control) {
        super(winTitle, title, control);
    }

    @Override
    public boolean isAccessed() {
        return isEnabled();
    }

    @Override
    public boolean isVisible() {
        return isDisplayed();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public String readValue() {
        waitWinActive();
        return autoItX.controlGetText(winTitle, "", control);
    }

    @Override
    public Verifier verify(String expected) {
        waitWinActive();
        return Verifier.of(this, expected);
    }
}
