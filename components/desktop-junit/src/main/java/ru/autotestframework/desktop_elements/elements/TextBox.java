package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

public class TextBox extends TypifiedDesktopElement
        implements ICleanable, IWritable, IReadable, IVerifiable, IAccessible {

    private static final String TEXT_BOX_IS_READ_ONLY = "textBoxIsReadOnly";

    public TextBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns TextBox value
     * @return
     */
    @Override
    public String readValue() {
        return getText();
    }

    /**
     * verifies TextBox
     * @param expected ожидаемое значение для проверки
     * @return
     */
    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }

    /**
     * writes in TextBox
     * @param value
     * @throws ElementInteractionException
     */
    @Override
    public void write(final String value) throws ElementInteractionException {
        setText(value);
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
     * Gets the text of the element.
     *
     * @return text of the element
     */
    @Override
    public String getText() {
        return getSelenideElement().getAttribute("Name");
    }

    /**
     * Sets the text of the element.
     *
     * @param text specified text that should be set to the element
     */
    public void setText(final String text) {
        getSelenideElement().setValue(text);
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return !isReadOnly();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return isDisplayed();
    }

    /**
     * Gets if the element is read only or not.
     *
     * @return read only status of the element
     */
    public boolean isReadOnly() {
        var response = callVoidCommand(TEXT_BOX_IS_READ_ONLY);
        var readOnlyBoxType = Boolean.parseBoolean(response.getValue().toString());
        return readOnlyBoxType && isEnabled();
    }

    /**
     * Simulate typing in text. This is slower than setting Text but raises more events.
     *
     * @param text ...
     */
    public void enter(final String text) {
        getSelenideElement().sendKeys(text);
    }

    /**
     * clears TextBox
     */
    @Override
    public void clean() {
        this.clear();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
