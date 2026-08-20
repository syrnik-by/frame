package ru.autotestframework.pages;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.desktop_elements.elements.Button;
import ru.autotestframework.desktop_elements.elements.CheckBox;
import ru.autotestframework.desktop_elements.elements.MenuItem;
import ru.autotestframework.desktop_elements.elements.TitleBar;
import ru.autotestframework.screen_elements.elements.TextBox;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.element_manager.FindByScreen;

/**
 * Notepad.
 */
@PageEntry(title = "Другая")
public class Notepad extends AbstractPage {

    /**
     * The Title bar.
     */
    @Element("Title")
    @FindBy(id = "TitleBar")
    public TitleBar titleBar;

    /**
     * The Text box 2.
     */
    @Element("Text2")
    @FindByScreen(location = "screenshots/textTextBox.png")
    public TextBox textBox2;

    /**
     * The New button.
     */
    @Element("New")
    @FindBy(name = "New")
    public Button newButton;

    /**
     * The Close all button.
     */
    @Element("Close all")
    @FindBy(name = "Close All")
    public Button closeAllButton;

    /**
     * The Text box.
     */
    @Element("Text")
    @FindBy(xpath = "//Pane[@ClassName='Scintilla']")
    public ru.autotestframework.desktop_elements.elements.TextBox textBox;

    /**
     * The Exit button.
     */
    @Element("Exit")
    @FindBy(name = "Закрыть")
    public Button exitButton;

    /**
     * The Lazy init button.
     */
    @Element("check lazy")
    @FindBy(name = "CheckLazyInit")
    public Button lazyInitButton;

    /**
     * The Settings.
     */
    @Element("Settings")
    @FindBy(name = "Settings")
    public MenuItem settings;

    /**
     * The Preferences.
     */
    @Element("Preferences")
    @FindBy(name = "Preferences...")
    public CheckBox preferences;

    /**
     * The New button 2.
     */
    @Element("New_2")
    @FindBy(name = "New 2")
    public Button newButton2;
}
