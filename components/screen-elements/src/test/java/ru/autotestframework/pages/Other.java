package ru.autotestframework.pages;

import ru.autotestframework.screen_elements.elements.Button;
import ru.autotestframework.screen_elements.elements.TextBox;
import ru.autotestframework.screen_elements.elements.TextButton;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.element_manager.FindByScreen;

/**
 * Other page.
 */
@PageEntry(title = "Другая")
public class Other extends AbstractPage {

    /**
     * The File button.
     */
    @Element("File")
    @FindByScreen(location = "screenshots/other/fileButton.png")
    public Button fileButton;

    /**
     * The Text area.
     */
    @Element("OCR element")
    @FindByScreen(location = "Language")
    public TextButton textArea;

    /**
     * The Text area 2.
     */
    @Element("OCR element offset")
    @FindByScreen(location = "Language", searchType = 2, offsetX = 50)
    public TextButton textArea2;

    /**
     * The New button.
     */
    @Element("New from context menu")
    @FindByScreen(location = "screenshots/other/newButtonFromContextMenu.png")
    public Button newButton;

    /**
     * The New button from context menu.
     */
    @Element("New")
    @FindByScreen(regionLocation = "screenshots/other/buttonsRegion.png", location = "screenshots/other/newButton.png")
    public Button newButtonFromContextMenu;

    /**
     * The Close all button.
     */
    @Element("Close all")
    @FindByScreen(
            regionLocation = "screenshots/other/buttonsRegion.png",
            location = "screenshots/other/closeAllButton.png")
    public Button closeAllButton;

    /**
     * The Text box.
     */
    @Element("Text")
    @FindByScreen(location = "screenshots/other/textTextBox.png")
    public TextBox textBox;

    /**
     * The Exit button.
     */
    @Element("Exit")
    @FindByScreen(location = "screenshots/other/exitButton.png")
    public Button exitButton;

    /**
     * The Select all button.
     */
    @Element("Select All")
    @FindByScreen(location = "screenshots/other/selectAllButton.png")
    public Button selectAllButton;
}
