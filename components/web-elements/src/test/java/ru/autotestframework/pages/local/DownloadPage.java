package ru.autotestframework.pages.local;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.page_manager.URL;
import ru.autotestframework.web_elements.elements.Button;

@PageEntry(title = "Форма скачивания")
public class DownloadPage extends AbstractPage {

    @URL(url = "file://${{user.dir}}/src/test/resources/local_pages/downloads.html")
    public String url;

    @Element("direct-download-window")
    @FindBy(xpath = "//*[@id='direct-download-window']")
    public Button directDownLoad;
}
