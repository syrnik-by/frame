package ru.autotestframework.pages;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.desktop_elements.elements.*;
import ru.autotestframework.desktop_elements.elements.Button;
import ru.autotestframework.desktop_elements.elements.CheckBox;
import ru.autotestframework.desktop_elements.elements.MenuItem;
import ru.autotestframework.desktop_elements.elements.TitleBar;
import ru.autotestframework.elements.CustomTextBox;
import ru.autotestframework.ui_core.junit.InjectedPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;

@PageEntry(title = "Другая")
public class Other extends InjectedPage<Other> {

    @Element("Title")
    @FindBy(id = "TitleBar")
    public TitleBar titleBar;

    @Element("New")
    @FindBy(name = "New")
    public Button newButton;

    @Element("Close all")
    @FindBy(name = "Close All")
    public Button closeAllButton;

    @Element("Text")
    @FindBy(xpath = "//Pane[@ClassName='Scintilla']")
    public CustomTextBox textBox;

    @Element("Exit")
    @FindBy(name = "Закрыть")
    public Button exitButton;

    @Element("check lazy")
    @FindBy(name = "CheckLazyInit")
    public Button lazyInitButton;

    @Element("Settings")
    @FindBy(name = "Settings")
    public MenuItem settings;

    @Element("Preferences")
    @FindBy(name = "Preferences...")
    public CheckBox preferences;

    @Element("New_2")
    @FindBy(name = "New 2")
    public Button newButton2;
}
