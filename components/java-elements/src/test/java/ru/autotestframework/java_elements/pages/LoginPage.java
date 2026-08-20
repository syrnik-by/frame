package ru.autotestframework.java_elements.pages;

import java.util.List;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.java_junit.elements.Button;
import ru.autotestframework.java_junit.elements.CheckBox;
import ru.autotestframework.java_junit.elements.TextInput;
import ru.autotestframework.java_junit.pages.AbstractJavaPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;

@PageEntry(title = "Login")
public class LoginPage extends AbstractJavaPage {

    @Element("UserName")
    @FindBy(css = "text-field")
    public TextInput nameInput;

    @Element("Password")
    @FindBy(css = "password-field")
    public TextInput passInput;

    @Element("Remember me")
    @FindBy(css = "check-box[name='rememberMe']")
    public CheckBox rememberCheckBox;

    @Element("Login")
    @FindBy(css = "button[text='Login']")
    public Button loginButton;

    @Element("Cancel")
    @FindBy(css = "button[text='Cancel']")
    public Button cancelButton;

    @Element("Кнопки")
    @FindBy(css = "button")
    public List<Button> buttons;

    @Element("DisabledField")
    @FindBy(name = "DisabledField")
    public TextInput disabledField;
}
