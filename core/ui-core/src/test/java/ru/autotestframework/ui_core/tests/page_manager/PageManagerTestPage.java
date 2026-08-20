package ru.autotestframework.ui_core.tests.page_manager;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.table_service.ITable;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Page manager test page.
 */
@PageEntry(title = "Предупреждения")
public class PageManagerTestPage extends AbstractPage {

    /**
     * The Show.
     */
    @Element("Show prompt")
    @FindBy(xpath = "//*[@id='promptexample']")
    public IElement show;

    /**
     * The Table.
     */
    @Element("iTable")
    @FindBy(xpath = "//*[@id='promptexplanation']")
    public ITable iTable;
}
