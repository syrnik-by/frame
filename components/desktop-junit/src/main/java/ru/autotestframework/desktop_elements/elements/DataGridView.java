package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DataGridView extends TypifiedDesktopElement {

    private static final String DATA_GRID_VIEW_HAS_ADD_ROW = "dataGridViewHasAddRow";
    private static final String DATA_GRID_VIEW_GET_HEADER = "dataGridViewGetHeader";
    public static final String SCROLL_BAR_TAG = "ScrollBar";

    public DataGridView(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Flag to indicate if the grid has the "Add New Item" row or not.
     *
     * @return The possibility to add new item to the row
     */
    public boolean hasAddRow() {
        var response = callVoidCommand(DATA_GRID_VIEW_HAS_ADD_ROW);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * Gets the header element or null if the header is disabled.
     *
     * @return Header element
     */
    public DataGridViewHeader getHeader() {
        var response = callVoidCommand(DATA_GRID_VIEW_GET_HEADER);
        if (response == null) {
            return null;
        }
        return new DataGridViewHeader(createWebElementFromResponse(response));
    }

    /**
     * Gets all the data rows.
     *
     * @return List of the data rows
     */
    public List<DataGridViewRow> getRows() {
        return getSelenideElement().$$x("*").stream()
                .filter(x -> !x.getTagName().equals(SCROLL_BAR_TAG))
                .map(DataGridViewRow::new)
                .collect(Collectors.toList());
    }
}
