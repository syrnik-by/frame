package ru.autotestframework.pages;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.web_elements.elements.TextInput;

/**
 * Gitlab.
 */
@PageEntry(title = "Gitlab Login Page")
public class Gitlab extends AbstractPage {

    /**
     * The User name.
     */
    @Element("Username")
    @FindBy(xpath = "//*[@id='username']")
    public TextInput userName;

    /**
     * The Password.
     */
    @Element("Password")
    @FindBy(xpath = "//*[@id='password']")
    public TextInput password;
}
