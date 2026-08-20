package ru.autotestframework.java_elements.pages;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.java_junit.elements.Button;
import ru.autotestframework.java_junit.elements.Label;
import ru.autotestframework.java_junit.pages.AbstractJavaPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;

@PageEntry(title = "Login Success")
public class LoginSuccessPage extends AbstractJavaPage {

    @Element("Label")
    @FindBy(css = "label")
    public Label successLabel;

    @Element("OK")
    @FindBy(css = "button[text='OK']")
    public Button okButton;
}
