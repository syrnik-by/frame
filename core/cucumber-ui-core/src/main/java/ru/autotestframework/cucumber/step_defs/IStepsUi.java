package ru.autotestframework.cucumber.step_defs;

import java.util.List;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * A basic set of steps, the implementation of which is the same for web, desktop and java drivers.
 */
public interface IStepsUi {
    /**
     * Close app.
     */
    void closeApp();

    /**
     * Sets current page.
     *
     * @param pageTitle the page title
     */
    void setCurrentPage(String pageTitle);

    /**
     * Click.
     *
     * @param elementTitle the element title
     */
    void click(String elementTitle);

    /**
     * Click.
     *
     * @param index        the index
     * @param elementTitle the element title
     */
    void click(int index, String elementTitle);

    /**
     * Double click.
     *
     * @param elementTitle the element title
     */
    void doubleClick(String elementTitle);

    /**
     * Double click.
     *
     * @param index        the index
     * @param elementTitle the element title
     */
    void doubleClick(int index, String elementTitle);

    /**
     * Right click.
     *
     * @param elementTitle the element title
     */
    void rightClick(String elementTitle);

    /**
     * Right click.
     *
     * @param index        the index
     * @param elementTitle the element title
     */
    void rightClick(int index, String elementTitle);

    /**
     * Hover.
     *
     * @param elementTitle the element title
     */
    void hover(String elementTitle);

    /**
     * Hover.
     *
     * @param index        the index
     * @param elementTitle the element title
     */
    void hover(int index, String elementTitle);

    /**
     * Clear field.
     *
     * @param elementTitle the element title
     */
    void clearField(String elementTitle);

    /**
     * Clear field.
     *
     * @param index        the index
     * @param elementTitle the element title
     */
    void clearField(int index, String elementTitle);

    /**
     * Clear fields.
     *
     * @param elementTitlesList the element titles list
     */
    void clearFields(List<String> elementTitlesList);

    /**
     * Fill field.
     *
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void fillField(String elementTitle, String value) throws ElementInteractionException;

    /**
     * Fill field.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void fillField(int index, String elementTitle, String value) throws ElementInteractionException;

    /**
     * Fill fields.
     *
     * @param data the data
     * @throws ElementInteractionException the element interaction exception
     */
    void fillFields(ResolvableMap data) throws ElementInteractionException;

    /**
     * Select value.
     *
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void selectValue(String elementTitle, String value) throws ElementInteractionException;

    /**
     * Select value.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void selectValue(int index, String elementTitle, String value) throws ElementInteractionException;

    /**
     * Append field.
     *
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void appendField(String elementTitle, String value) throws ElementInteractionException;

    /**
     * Append field.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void appendField(int index, String elementTitle, String value) throws ElementInteractionException;

    /**
     * Append fields.
     *
     * @param data the data
     * @throws ElementInteractionException the element interaction exception
     */
    void appendFields(ResolvableMap data) throws ElementInteractionException;

    /**
     * Strong verify field.
     *
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void strongVerifyField(String elementTitle, String value) throws ElementInteractionException;

    /**
     * Strong verify field.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void strongVerifyField(int index, String elementTitle, String value) throws ElementInteractionException;

    /**
     * Verify field.
     *
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void verifyField(String elementTitle, String value) throws ElementInteractionException;

    /**
     * Verify field.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param value        the value
     * @throws ElementInteractionException the element interaction exception
     */
    void verifyField(int index, String elementTitle, String value) throws ElementInteractionException;

    /**
     * Strong verify fields.
     *
     * @param data the data
     * @throws ElementInteractionException the element interaction exception
     */
    void strongVerifyFields(ResolvableMap data) throws ElementInteractionException;

    /**
     * Verify fields.
     *
     * @param data the data
     * @throws ElementInteractionException the element interaction exception
     */
    void verifyFields(ResolvableMap data) throws ElementInteractionException;

    /**
     * Verify fields.
     *
     * @param elementTitle the element title
     * @param values       the values
     * @throws ElementInteractionException the element interaction exception
     */
    void verifyFields(String elementTitle, ResolvableList values) throws ElementInteractionException;

    /**
     * Strong verify fields.
     *
     * @param elementTitle the element title
     * @param values       the values
     * @throws ElementInteractionException the element interaction exception
     */
    void strongVerifyFields(String elementTitle, ResolvableList values) throws ElementInteractionException;

    /**
     * Sets window.
     *
     * @param windowName the window name
     */
    void setWindow(String windowName);

    /**
     * Verify displayed elements.
     *
     * @param isDisplayed       the is displayed
     * @param elementTitlesList the element titles list
     */
    void verifyDisplayedElements(Boolean isDisplayed, List<String> elementTitlesList);

    /**
     * Verify displayed element.
     *
     * @param isDisplayed  the is displayed
     * @param elementTitle the element title
     */
    void verifyDisplayedElement(Boolean isDisplayed, String elementTitle);

    /**
     * Verify displayed element.
     *
     * @param isDisplayed  the is displayed
     * @param index        the index
     * @param elementTitle the element title
     */
    void verifyDisplayedElement(Boolean isDisplayed, int index, String elementTitle);

    /**
     * Verify active element.
     *
     * @param elementTitle the element title
     * @param isActive     the is active
     */
    void verifyActiveElement(String elementTitle, Boolean isActive);

    /**
     * Verify editable element.
     *
     * @param elementTitle the element title
     * @param isEditable   the is editable
     */
    void verifyEditableElement(String elementTitle, Boolean isEditable);

    /**
     * Verify editable elements.
     *
     * @param isEditable        the is editable
     * @param elementTitlesList the element titles list
     */
    void verifyEditableElements(Boolean isEditable, List<String> elementTitlesList);

    /**
     * Verify active element.
     *
     * @param index        the index
     * @param elementTitle the element title
     * @param isActive     the is active
     */
    void verifyActiveElement(int index, String elementTitle, Boolean isActive);

    /**
     * Verify active elements.
     *
     * @param isActive          the is active
     * @param elementTitlesList the element titles list
     */
    void verifyActiveElements(Boolean isActive, List<String> elementTitlesList);

    /**
     * Equals numbers of elements.
     *
     * @param elementTitle the element title
     * @param numbers      the numbers
     */
    void equalsNumbersOfElements(String elementTitle, Integer numbers);

    /**
     * Read values.
     *
     * @param data the data
     */
    void readValues(ResolvableMap data);

    /**
     * Read value.
     *
     * @param variableName the variable name
     * @param elementTitle the element title
     */
    void readValue(String variableName, String elementTitle);

    /**
     * Press on key board.
     *
     * @param keysCombination the keys combination
     */
    void pressOnKeyBoard(String keysCombination);

    /**
     * Take screenshot and save.
     *
     * @param path the path
     */
    void takeScreenshotAndSave(String path);

    /**
     * Compare current window with screenshot.
     *
     * @param resultPath the result path
     * @param path       the path
     */
    void compareCurrentWindowWithScreenshot(String resultPath, String path);

    /**
     * Compare screenshots.
     *
     * @param resultPath the result path
     * @param path1      the path 1
     * @param path2      the path 2
     */
    void compareScreenshots(String resultPath, String path1, String path2);

    /**
     * Has attribute.
     *
     * @param elementTitle the element title
     * @param attribute    the attribute
     */
    void hasAttribute(String elementTitle, String attribute);

    /**
     * Check attributes.
     *
     * @param elementTitle the element title
     * @param rows         the rows
     */
    void checkAttributes(String elementTitle, List<Triple> rows);
}
