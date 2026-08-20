package ru.autotestframework.pages;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.desktop_elements.elements.Button;
import ru.autotestframework.desktop_elements.elements.ComboBox;
import ru.autotestframework.desktop_elements.elements.TextBox;
import ru.autotestframework.desktop_elements.elements.ToggleButton;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;

@PageEntry(title = "Логин")
public class Login extends AbstractPage {

    @Element("OK")
    @FindBy(id = "_okButton")
    public Button okButton;

    @Element("Отмена")
    @FindBy(id = "_cancelButton")
    public Button cancelButton;

    @Element("Дополнительно <<")
    @FindBy(id = "_additionalInfoButton")
    public Button additionalInfoButton;

    @Element("Запомнить сервер")
    @FindBy(id = "_saveServerCheckBox")
    public ToggleButton saveServerCheckBox;

    @Element("Сервер")
    @FindBy(id = "_coreXHostComboBoxAdv")
    public ComboBox coreXHostComboBox;

    @Element("Использовать учетную запись Windows")
    @FindBy(id = "_domainLoginCheckBox")
    public ToggleButton domainLoginCheckBox;

    @Element("Запомнить логин")
    @FindBy(id = "_saveLoginCheckBox")
    public ToggleButton saveLoginCheckBox;

    @Element("Логин")
    @FindBy(id = "_loginTextBox")
    public TextBox loginTextBox;

    @Element("Пароль")
    @FindBy(id = "_passwordTextBox")
    public TextBox passwordTextBox;
}
