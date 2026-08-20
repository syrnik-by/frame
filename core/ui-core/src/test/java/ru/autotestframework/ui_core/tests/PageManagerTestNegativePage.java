package ru.autotestframework.ui_core.tests;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Page manager test negative page.
 */
@PageEntry(title = "Предупреждения")
public class PageManagerTestNegativePage extends AbstractPage {

    /**
     * The Table.
     */
    @Element("Show prompt")
    @FindBy(xpath = "//*[@id='promptexample']")
    public IElement table;

    /**
     * The Prompt explanation.
     */
    @Element("Ответ")
    @FindBy(xpath = "//*[@id='promptexplanation']")
    public IElement promptExplanation;
}
