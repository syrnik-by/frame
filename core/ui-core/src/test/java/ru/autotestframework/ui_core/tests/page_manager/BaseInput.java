package ru.autotestframework.ui_core.tests.page_manager;

import com.codeborne.selenide.SelenideElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * Base input.
 */
public abstract class BaseInput {

    /**
     * The Input element.
     */
    protected final SelenideElement inputElement;

    private final String title;

    /**
     * Instantiates a new Base input.
     *
     * @param inputElement the input element
     */
    public BaseInput(SelenideElement inputElement) {
        this(inputElement, null);
    }

    /**
     * Instantiates a new Base input.
     *
     * @param inputElement the input element
     * @param title        the title
     */
    public BaseInput(SelenideElement inputElement, String title) {
        this.inputElement = inputElement;
        this.title = title;
    }

    /**
     * Is read only boolean.
     *
     * @return the boolean
     */
    protected abstract boolean isReadOnly();

    /**
     * Click.
     */
    public void click() {
        throw new ElementInteractionException("Метод click() не реализован в родительском классе");
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
}
