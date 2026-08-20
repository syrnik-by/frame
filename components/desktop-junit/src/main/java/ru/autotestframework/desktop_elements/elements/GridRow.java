package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class GridRow extends TypifiedDesktopElement {

    private static final String GRID_ROW_CELLS = "gridRowCells";
    private static final String GRID_ROW_HEADER = "gridRowHeader";
    private static final String GRID_ROW_FIND_CELL_BY_TEXT = "gridRowFindCellByText";
    private static final String GRID_ROW_SCROLL_INTO_VIEW = "gridRowScrollIntoView";

    public GridRow(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * Gets all the cells from the row.
     *
     * @return list of the cell from this row
     */
    public List<GridCell> cells() {
        var response = callVoidCommand(GRID_ROW_CELLS);
        return createWebElementsFromResponse(response).stream()
                .map(GridCell::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets the header item of the row.
     *
     * @return header item of this row
     */
    public GridHeaderItem header() {
        var response = callVoidCommand(GRID_ROW_HEADER);
        if (response == null) {
            return null;
        }
        return new GridHeaderItem(createWebElementFromResponse(response));
    }

    /**
     * Find a cell by a given text.
     *
     * @param textToFind specified text
     * @return cell with specified text
     */
    public GridCell findCellByText(final String textToFind) {
        var response = callValueCommand(GRID_ROW_FIND_CELL_BY_TEXT, textToFind);
        if (response == null) {
            return null;
        }
        return new GridCell(createWebElementFromResponse(response));
    }

    /**
     * Scrolls the row into view.
     *
     * @return visible row
     */
    public GridRow scrollIntoView() {
        var response = callVoidCommand(GRID_ROW_SCROLL_INTO_VIEW);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }
}
