package ru.autotestframework.pages.local;

import static com.codeborne.selenide.Selenide.title;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.junit.InjectedPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.page_manager.URL;
import ru.autotestframework.web_elements.elements.Button;
import ru.autotestframework.web_elements.elements.TextBlock;

@Slf4j
@PageEntry(title = "Progress Bars")
public class MultipleProgressBarsPage extends InjectedPage<MultipleProgressBarsPage> {

    @URL(url = "file://${{user.dir}}/src/test/resources/local_pages/multipleProgressBars.html")
    public String url;

    @Element("Run")
    @FindBy(xpath = "//*[text()='Running']")
    public TextBlock running;

    @Element("Stop")
    @FindBy(xpath = "//*[text()='Stopped']")
    public TextBlock stopped;

    @Element("Start")
    @FindBy(xpath = "//*[@id='onclick']")
    public Button start;

    @Override
    public void checkAcceptor() {
        log.info(title());
    }

    public MultipleProgressBarsPage checkPage() {
        // some dummy method
        return this;
    }
}
