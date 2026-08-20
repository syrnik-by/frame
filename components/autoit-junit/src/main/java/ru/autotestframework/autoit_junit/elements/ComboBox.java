package ru.autotestframework.autoit_junit.elements;

import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Combo box.
 */
public class ComboBox extends TypifiedAutoItElement
        implements IAccessible, IReadable, IWritable, IVerifiable, ICleanable, ISelectable {

    /**
     * Instantiates a new Combo box.
     *
     * @param winTitle the win title
     * @param title    the title
     * @param control  the control
     */
    public ComboBox(String winTitle, String title, String control) {
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
    public String readValue() {
        waitWinActive();
        return autoItX.controlGetText(winTitle, "", control);
    }

    @Override
    public Verifier verify(String expected) {
        waitWinActive();
        return Verifier.of(this, expected);
    }

    @Override
    public void write(String value) {
        waitWinActive();
        autoItX.ControlSetText(winTitle, "", control, value);
    }

    @Override
    public void select(String value) {
        waitWinActive();
        autoItX.controlCommandSelectString(winTitle, "", control, value);
    }

    @Override
    public void clean() {
        clear();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
