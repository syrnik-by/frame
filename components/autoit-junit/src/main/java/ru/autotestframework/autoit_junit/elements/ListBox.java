package ru.autotestframework.autoit_junit.elements;

import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;

/**
 * List box.
 */
public class ListBox extends TypifiedAutoItElement implements IAccessible, ISelectable {

    /**
     * Instantiates a new List box.
     *
     * @param winTitle the win title
     * @param title    the title
     * @param control  the control
     */
    public ListBox(String winTitle, String title, String control) {
        super(winTitle, title, control);
    }

    @Override
    public void select(String value) {
        waitWinActive();
        autoItX.controlCommandSelectString(winTitle, "", control, value);
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
}
