package ru.autotestframework.desktop_elements.elements;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

public class ToggleButton extends TypifiedDesktopElement implements IWritable, IReadable, IVerifiable, ISelectable {

    private static final String TOGGLE_BUTTON_SET_TOGGLE_STATE = "toggleButtonSetToggleState";

    public ToggleButton(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return true;
    }

    /**
     * Toggles the toggle button.
     * Note: In some WPF scenarios, the bounded command might not be fired.
     * Use "AutomationElement.Click" instead in that case.
     */
    public void toggle() {
        getSelenideElement().click();
    }

    /**
     * Gets the current toggle state.
     *
     * @return current toggle state
     */
    @Override
    public FixState readState() {
        return FixState.determine(toggleState().toString());
    }

    /**
     * checks if ToggleButton is checked
     * @return
     */
    public Boolean toggleState() {
        return getSelenideElement().is(Condition.checked);
    }

    /**
     * Sets the current toggle state.
     *
     * @param fixState current toggle state
     */
    private void setToggleState(final FixState fixState) {
        callValueCommand(TOGGLE_BUTTON_SET_TOGGLE_STATE, fixState.getValue());
    }

    /**
     * sets ToggleButton fix state
     * @param value
     * @throws ElementInteractionException
     */
    @Override
    public void writeFixState(final FixState value) throws ElementInteractionException {
        setToggleState(value);
    }

    /**
     * checks if element is editable
     * @return
     */
    @Override
    public boolean isEditable() {
        return isAccessed() && isEnabled();
    }

    /**
     * verifies ToggleButton fix state
     * @param expected
     * @return
     */
    @Override
    public Verifier verifyFixState(final FixState expected) {
        return Verifier.of(this, expected);
    }
}
