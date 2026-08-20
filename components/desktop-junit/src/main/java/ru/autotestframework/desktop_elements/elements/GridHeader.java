package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class GridHeader extends TypifiedDesktopElement {

    private static final String GRID_HEADER_COLUMNS = "gridHeaderColumns";

    public GridHeader(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * Gets all header items from the grid header.
     *
     * @return list of headers from the grid header
     */
    public List<GridHeaderItem> columns() {
        var response = callVoidCommand(GRID_HEADER_COLUMNS);
        return createWebElementsFromResponse(response).stream()
                .map(GridHeaderItem::new)
                .collect(Collectors.toList());
    }
}
