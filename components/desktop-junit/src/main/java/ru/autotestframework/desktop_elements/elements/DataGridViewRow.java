package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class DataGridViewRow extends TypifiedDesktopElement {

    public DataGridViewRow(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * +
     * Gets all cells.
     *
     * @return List of the cells
     */
    public List<DataGridViewCell> getCells() {
        return getSelenideElement().$$x("*").stream().map(DataGridViewCell::new).collect(Collectors.toList());
    }
}
