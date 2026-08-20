package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class GridCell extends TypifiedDesktopElement {

    private static final String GRID_CELL_CONTAINING_GRID = "gridCellContainingGrid";
    private static final String GRID_CELL_CONTAINING_ROW = "gridCellContainingRow";

    public GridCell(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * Gets the grid that contains this cell.
     *
     * @return grid which contains this cell
     */
    public Grid containingGrid() {
        var response = callVoidCommand(GRID_CELL_CONTAINING_GRID);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * Gets the row that contains this cell.
     *
     * @return row which contains this cell
     */
    public GridRow containingRow() {
        var response = callVoidCommand(GRID_CELL_CONTAINING_ROW);
        if (response == null) {
            return null;
        }
        return new GridRow(createWebElementFromResponse(response));
    }
}
