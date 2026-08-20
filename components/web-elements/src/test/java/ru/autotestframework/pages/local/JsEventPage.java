package ru.autotestframework.pages.local;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.page_manager.URL;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.web_elements.elements.Button;
import ru.autotestframework.web_elements.elements.TextBlock;

@Slf4j
@PageEntry(title = "Форма с кнопками")
public class JsEventPage extends AbstractPage {

    @URL(url = "file://${{user.dir}}/src/test/resources/local_pages/buttons.html")
    public String url;

    @Element("OnFocusStatus")
    @FindBy(xpath = "//*[@id='onfocusstatus']")
    public TextBlock onFocusStatus;

    @Element("OnClickStatus")
    @FindBy(xpath = "//*[@id='onclickstatus']")
    public TextBlock onClickStatus;

    @Element("OnContextMenuStatus")
    @FindBy(xpath = "//*[@id='oncontextmenustatus']")
    public TextBlock onContextMenuStatus;

    @Element("OnFocus")
    @FindBy(xpath = "//*[@id='onfocus']")
    public Button onFocus;

    @Element("OnMouseDown")
    @FindBy(xpath = "//*[@id='onmousedown']")
    public Button onMouseDown;

    @Element("OnBlur")
    @FindBy(xpath = "//*[@id='onblur']")
    public Button onBlur;

    @Element("OnMouseOver")
    @FindBy(xpath = "//*[@id='onmouseover']")
    public Button onMouseOver;

    @Element("OnClick")
    @FindBy(xpath = "//*[@id='onclick']")
    public Button onClick;

    @Element("OnContextMenu")
    @FindBy(xpath = "//*[@id='oncontextmenu']")
    public Button onContextMenu;

    @Element("Кнопки")
    @FindBy(xpath = "//button")
    public List<Button> buttons;

    @Element("Кнопки IElement")
    @FindBy(xpath = "//button")
    public List<IElement> iButtons;

    @Element("Кнопки WebElement")
    @FindBy(xpath = "//button")
    public List<WebElement> webButtons;

    @Element("Статусы")
    @FindBy(xpath = "//*[contains(@id,'status')]")
    public List<TextBlock> statuses;

    @Override
    public void checkAcceptor() {
        log.info(title());
        $("title").shouldHave(attribute("text", "JavaScript Events"));
    }
}
