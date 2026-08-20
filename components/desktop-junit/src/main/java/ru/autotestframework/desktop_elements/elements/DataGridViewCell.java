package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DataGridViewCell extends TypifiedDesktopElement {

    private static final String DATA_GRID_VIEW_CELL_SET_VALUE = "dataGridViewCellSetValue";

    DataGridViewCell(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public DataGridViewCell(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
    /**
     * Gets the value in the cell.
     *
     * @return Value of the cell
     */
    public String getValue() {
        return getSelenideElement().getText();
    }

    /**
     * Sets the value in the cell.
     *
     * @param value Sets the value of the cell
     */
    public void setValue(final String value) {
        callValueCommand(DATA_GRID_VIEW_CELL_SET_VALUE, value);
    }
}
