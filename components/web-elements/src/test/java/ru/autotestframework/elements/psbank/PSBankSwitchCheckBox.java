package ru.autotestframework.elements.psbank;

import com.codeborne.selenide.SelenideElement;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.web_elements.elements.ClassicCheckBox;

public class PSBankSwitchCheckBox extends ClassicCheckBox {

    public PSBankSwitchCheckBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public FixState readState() {
        var boolString = Optional.ofNullable(getInput().getAttribute("checked")).orElse("false");
        return FixState.determine(boolString);
    }

    private SelenideElement getInput() {
        return getSelenideElement().$(By.xpath("./../descendant-or-self::input[@type='checkbox']"));
    }
}
