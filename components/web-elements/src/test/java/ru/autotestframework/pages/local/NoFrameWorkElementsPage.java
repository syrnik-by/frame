package ru.autotestframework.pages.local;

import com.codeborne.selenide.ElementsCollection;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.junit.tests.ProjectAbstractPage;

public class NoFrameWorkElementsPage extends ProjectAbstractPage<NoFrameWorkElementsPage> {

    @FindBy(xpath = "//*[@multiple='multiple']/option")
    private ElementsCollection selenideElements;

    @FindBy(xpath = "//*[@multiple='multiple']/option")
    private List<WebElement> webElList;

    @FindBy(xpath = "//*[@class='search_form']")
    private WebElement webElement;

    public NoFrameWorkElementsPage listsCheck() {
        webElement.click();
        webElement.getTagName();
        selenideElements.get(0).click();
        webElList.get(1).click();
        return this;
    }
}
