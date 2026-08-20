package ru.autotestframework.pages.local;

import static com.codeborne.selenide.Selenide.title;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.web_elements.elements.TextBlock;

@Slf4j
@PageEntry(title = "Предупреждения")
public class Alerts extends AbstractPage {

    @Element("Show prompt")
    @FindBy(xpath = "//*[@id='promptexample']")
    public TextBlock table;

    @Element("Ответ")
    @FindBy(xpath = "//*[@id='promptexplanation']")
    public TextBlock promptExplanation;

    @Override
    public void checkAcceptor() {
        log.info(title());
    }
}
