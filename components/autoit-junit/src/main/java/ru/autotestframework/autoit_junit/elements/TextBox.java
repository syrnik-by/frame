package ru.autotestframework.autoit_junit.elements;

import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Text box.
 */
public class TextBox extends TypifiedAutoItElement
        implements ICleanable, IWritable, IReadable, IVerifiable, IAccessible {

    /**
     * Instantiates a new Text box.
     *
     * @param winTitle the win title
     * @param title    the title
     * @param control  the control
     */
    public TextBox(String winTitle, String title, String control) {
        super(winTitle, title, control);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public void write(String value) throws ElementInteractionException {
        waitWinActive();
        autoItX.ControlSetText(winTitle, "", control, value);
    }

    @Override
    public void append(String value) throws ElementInteractionException {
        click();
        autoItX.send("{END}", false);
        autoItX.send(value);
    }

    @Override
    public void clean() {
        clear();
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
}
