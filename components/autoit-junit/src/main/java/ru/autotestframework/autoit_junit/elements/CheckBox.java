package ru.autotestframework.autoit_junit.elements;

import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Check box.
 */
public class CheckBox extends TypifiedAutoItElement implements IAccessible, IReadable, IWritable, IVerifiable {

    /**
     * Instantiates a new Check box.
     *
     * @param winTitle the win title
     * @param title    the title
     * @param control  the control
     */
    public CheckBox(String winTitle, String title, String control) {
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
    public FixState readState() {
        waitWinActive();
        return FixState.determine(String.valueOf(autoItX.controlCommandIsChecked(winTitle, "", control)));
    }

    @Override
    public boolean isFixStateValue() {
        return true;
    }

    @Override
    public Verifier verifyFixState(FixState expected) {
        waitWinActive();
        return Verifier.of(this, expected);
    }
}
