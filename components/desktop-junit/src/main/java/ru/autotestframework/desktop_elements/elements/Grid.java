package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.RowOrColumnMajor;

public class Grid extends TypifiedDesktopElement {

    private static final String GRID_ROW_COUNT = "gridRowCount";
    private static final String GRID_COLUMN_COUNT = "gridColumnCount";
    private static final String GRID_COLUMN_HEADERS = "gridColumnHeaders";
    private static final String GRID_ROW_HEADERS = "gridRowHeaders";
    private static final String GRID_ROW_OR_COLUMN_MAJOR = "gridRowOrColumnMajor";
    private static final String GRID_GET_HEADER = "gridGetHeader";
    private static final String GRID_GET_ROWS = "gridGetRows";
    private static final String GRID_SELECTED_ITEMS = "gridSelectedItems";
    private static final String GRID_SELECTED_ITEM = "gridSelectedItem";
    private static final String GRID_SELECT = "gridSelect";
    private static final String GRID_SELECT_TEXT = "gridSelectText";
    private static final String GRID_ADD_TO_SELECTION = "gridAddToSelection";
    private static final String GRID_ADD_TO_SELECTION_TEXT = "gridAddToSelectionText";
    private static final String GRID_REMOVE_FROM_SELECTION = "gridRemoveFromSelection";
    private static final String GRID_REMOVE_FROM_SELECTION_TEXT = "gridRemoveFromSelectionText";
    private static final String GRID_GET_ROW_BY_INDEX = "gridGetRowByIndex";
    private static final String GRID_GET_ROW_BY_VALUE = "gridGetRowByValue";
    private static final String GRID_GET_ROWS_BY_VALUE = "gridGetRowsByValue";

    public Grid(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the total row count.
     *
     * @return the amount of rows
     */
    public int rowCount() {
        var response = callVoidCommand(GRID_ROW_COUNT);
        return Integer.parseInt(response.getValue().toString());
    }

    /**
     * Gets the total column count.
     *
     * @return the amount of columns
     */
    public int columnCount() {
        var response = callVoidCommand(GRID_COLUMN_COUNT);
        return Integer.parseInt(response.getValue().toString());
    }

    /**
     * Gets all column header elements.
     *
     * @return all column header elements
     */
    public List<WebElement> columnHeaders() {
        var response = callVoidCommand(GRID_COLUMN_HEADERS);
        return createWebElementsFromResponse(response);
    }

    /**
     * Gets all row header elements.
     *
     * @return all row header elements
     */
    public List<WebElement> rowHeaders() {
        var response = callVoidCommand(GRID_ROW_HEADERS);
        return createWebElementsFromResponse(response);
    }

    /**
     * Gets whether the data should be read primarily by row or by column.
     *
     * @return enum which means how the data of the table should be read
     */
    public RowOrColumnMajor rowOrColumnMajor() {
        var response = callVoidCommand(GRID_ROW_OR_COLUMN_MAJOR);
        return RowOrColumnMajor.getEnum(response.getValue().toString());
    }

    /**
     * Gets the header item.
     *
     * @return header of the item
     */
    public GridHeader getHeader() {
        var response = callVoidCommand(GRID_GET_HEADER);
        if (response == null) {
            return null;
        }
        return new GridHeader(createWebElementFromResponse(response));
    }

    /**
     * Returns the rows which are currently visible to UIA. Might not be the full list (eg. in virtualized lists)!
     *
     * @return list of visible rows
     */
    public List<GridRow> getRows() {
        var response = callVoidCommand(GRID_GET_ROWS);
        return createWebElementsFromResponse(response).stream()
                .map(GridRow::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all selected items.
     *
     * @return list of selected items
     */
    public List<GridRow> selectedItems() {
        var response = callVoidCommand(GRID_SELECTED_ITEMS);
        return createWebElementsFromResponse(response).stream()
                .map(GridRow::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets the first selected item or null otherwise.
     *
     * @return first selected item
     */
    public GridRow selectedItem() {
        var response = callVoidCommand(GRID_SELECTED_ITEM);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Select a row by index.
     *
     * @param rowIndex index of the row
     * @return row with the specified index
     */
    public GridRow select(final int rowIndex) {
        var response = callValueCommand(GRID_SELECT, rowIndex);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Select the first row by text in the given column.
     *
     * @param columnIndex index of the column
     * @param textToFind  specified text
     * @return row with specified text in the specified column
     */
    public GridRow select(final int columnIndex, final String textToFind) {
        var response = callValueCommand(GRID_SELECT_TEXT, columnIndex, textToFind);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Add a row to the selection by index.
     *
     * @param rowIndex index of the row
     * @return row which was added
     */
    public GridRow addToSelection(final int rowIndex) {
        var response = callValueCommand(GRID_ADD_TO_SELECTION, rowIndex);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Add a row to the selection by text in the given column.
     *
     * @param columnIndex index of the column
     * @param textToFind  specified text
     * @return row with specified text in the specified column
     */
    public GridRow addToSelection(final int columnIndex, final String textToFind) {
        var response = callValueCommand(GRID_ADD_TO_SELECTION_TEXT, columnIndex, textToFind);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Remove a row from the selection by index.
     *
     * @param rowIndex index of the row
     * @return gridrow without specified row
     */
    public GridRow removeFromSelection(final int rowIndex) {
        var response = callValueCommand(GRID_REMOVE_FROM_SELECTION, rowIndex);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Remove a row from the selection by text in the given column.
     *
     * @param columnIndex index of the column
     * @param textToFind  specified text
     * @return gridrow without row with specified text in specified column
     */
    public GridRow removeFromSelection(final int columnIndex, final String textToFind) {
        var response = callValueCommand(GRID_REMOVE_FROM_SELECTION_TEXT, columnIndex, textToFind);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Get a row by index.
     *
     * @param rowIndex index of the row
     * @return row with specified index
     */
    public GridRow getRowByIndex(final int rowIndex) {
        var response = callValueCommand(GRID_GET_ROW_BY_INDEX, rowIndex);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Get a row by text in the given column.
     *
     * @param columnIndex index of the column
     * @param textToFind  specified text
     * @return row with specified text in the specified column
     */
    public GridRow getRowByValue(final int columnIndex, final String textToFind) {
        var response = callValueCommand(GRID_GET_ROW_BY_VALUE, columnIndex, textToFind);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }

    /**
     * Get all rows where the value of the given column matches the given value.
     *
     * @param columnIndex The column index to check.
     * @param value       The value to check.
     * @param maxItems    Maximum numbers of items to return, 0 for all.
     * @return List of found rows.
     */
    public List<GridRow> getRowsByValue(final int columnIndex, final String value, final int maxItems) {
        var response = callValueCommand(GRID_GET_ROWS_BY_VALUE, columnIndex, value, maxItems);
        return createWebElementsFromResponse(response).stream()
                .map(GridRow::new)
                .collect(Collectors.toList());
    }
}
