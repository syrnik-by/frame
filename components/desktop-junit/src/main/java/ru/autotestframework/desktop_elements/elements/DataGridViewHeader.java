package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DataGridViewHeader extends TypifiedDesktopElement {

    private static final String DATA_GRID_VIEW_HEADER_GET_COLUMNS = "dataGridViewHeaderGetColumns";

    DataGridViewHeader(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public DataGridViewHeader(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * +
     * Gets the header items.
     *
     * @return List of the headers
     */
    public List<DataGridViewHeaderItem> getColumns() {
        var response = callVoidCommand(DATA_GRID_VIEW_HEADER_GET_COLUMNS);
        return createWebElementsFromResponse(response).stream()
                .map(DataGridViewHeaderItem::new)
                .collect(Collectors.toList());
    }
}
