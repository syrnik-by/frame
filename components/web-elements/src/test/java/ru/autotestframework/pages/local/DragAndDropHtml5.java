package ru.autotestframework.pages.local;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.web_elements.elements.TextBlock;

@Slf4j
@PageEntry(title = "Drag and Drop Examples")
public class DragAndDropHtml5 extends AbstractPage {

    @Element("Drag A")
    @FindBy(id = "column-a")
    public TextBlock draggableLeft;

    @Element("Drag B")
    @FindBy(id = "column-b")
    public TextBlock draggableRight;

    @Element("Drag Elements")
    @FindBy(xpath = "//*[@class='column']")
    public List<TextBlock> draggableList;

    @Override
    public void checkAcceptor() {
        log.info(title());
        $("title").shouldHave(attribute("text", "The Internet"));
    }
}
