package ru.autotestframework.pages.local;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.junit.InjectedPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.table_service.FindCellsBy;
import ru.autotestframework.ui_core.services.table_service.FindHeadersBy;
import ru.autotestframework.web_elements.elements.Button;
import ru.autotestframework.web_elements.elements.TextInput;
import ru.autotestframework.web_elements.elements.WebTable;

@Slf4j
@PageEntry(title = "Dynamic HTML TABLE Tag")
public class DynamicTablePageForUnits extends InjectedPage<DynamicTablePageForUnits> {

    @Element("Table Data")
    @FindBy(xpath = "//summary")
    private Button tableData;

    @Element("Text Area")
    @FindBy(xpath = "//textarea[@id='jsondata']")
    private TextInput textArea;

    @Element("Caption")
    @FindBy(xpath = "//input[@id='caption']")
    private TextInput caption;

    @Element("Refresh Table")
    @FindBy(xpath = "//button[@id='refreshtable']")
    private Button refreshTable;

    @Element("Table")
    @FindBy(xpath = "//table")
    @FindHeadersBy(xpath = ".//th")
    @FindCellsBy(xpath = ".//td")
    public WebTable table;

    @Override
    public void checkAcceptor() {}
}
