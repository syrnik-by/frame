package ru.autotestframework.pages.local;

import static com.codeborne.selenide.Selenide.title;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.junit.tests.ProjectAbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.page_manager.URL;
import ru.autotestframework.web_elements.elements.ClassicCheckBox;
import ru.autotestframework.web_elements.elements.TextBlock;
import ru.autotestframework.web_elements.elements.TextInput;

@Slf4j
@PageEntry(title = "Тестовая Форма")
public class TestFormPage extends ProjectAbstractPage<TestFormPage> {

    @URL(url = "file://${{user.dir}}/src/test/resources/local_pages/form.html")
    private String url;

    @Element("Чекбокс 1")
    @FindBy(xpath = "//*[@value='cb1']")
    private ClassicCheckBox checkBox1;

    @Element("Логин")
    @FindBy(xpath = "//*[@name='username']")
    private TextInput username;

    @Element("Комментарий")
    @FindBy(xpath = "//*[@name='comments']")
    // public для примера теста
    public TextInput comments;

    @Element("Отмена")
    @FindBy(xpath = "//*[@value='cancel']")
    private TextBlock cancel;

    @Element("Отправка")
    @FindBy(xpath = "//*[@value='submit']")
    private TextBlock submit;

    @Element("Отсутствующий элемент")
    @FindBy(xpath = "//*[@value='nonexisted']")
    private TextBlock missed;

    @Element("Пояснение")
    @FindBy(xpath = "//*[@class='explanation']")
    private TextBlock explanation;

    @Element("Заголовок")
    @FindBy(xpath = "//h1")
    private TextBlock pageHeader;

    @Element("Поле-обертка")
    @FindBy(xpath = "//*[@class='search_form']")
    private TextInput wrappedInput;

    @Element("Поля списка")
    @FindBy(xpath = "//*[@class='lists']")
    private List<TextInput> fields;

    @FindBy(xpath = "//*[@multiple='multiple']/option")
    private ElementsCollection selenideElements;

    @FindBy(xpath = "//*[@multiple='multiple']/option")
    private List<WebElement> webElList;

    @FindBy(xpath = "//*[@class='search_form']")
    private WebElement webElement;

    @Element("Кнопка поиска")
    @FindBy(xpath = "//*[@class='search_form']")
    private SelenideElement search;

    public TestFormPage listsCheck() {
        search.shouldBe(Condition.visible);
        checkCondition("Кнопка поиска", Condition.visible);
        get("Кнопка поиска").shouldBe(Condition.visible);
        webElement.click();
        webElement.getTagName();
        selenideElements.get(0).click();
        webElList.get(1).click();
        return this;
    }

    public TestFormPage fillFormAndSend() {
        listsCheck();
        submit.click();
        return this;
    }

    @Override
    public void checkAcceptor() {
        log.info(title());
    }
}
