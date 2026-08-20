package ru.autotestframework.elements.psbank;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.conditions.WebEditable;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

public class PSBankInput extends TypifiedWebElement implements ICleanable, IWritable, IReadable, IAccessible {

    public PSBankInput(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    @Override
    public String readValue() {
        final WebElement input = getInput();
        return "input".equals(input.getTagName()) ? input.getAttribute("value") : input.getText();
    }

    @Override
    public boolean isEditable() {
        CheckResult check = new WebEditable().check(WebDriverRunner.driver(), this);
        return check.verdict.equals(CheckResult.Verdict.ACCEPT);
    }

    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }

    @Override
    public void write(final String value) {
        getInput().sendKeys(value);
    }

    @Override
    public void clean() {
        final SelenideElement input = getInput();
        input.sendKeys(Keys.CONTROL + "A");
        input.sendKeys(Keys.BACK_SPACE);
    }

    @Override
    public boolean isAccessed() {
        return getInput().isEnabled();
    }

    @Override
    public boolean isVisible() {
        return getInput().isDisplayed();
    }

    @Override
    public void append(final String value) {
        getInput().append(value);
    }

    private SelenideElement getInput() {
        return super.getSelenideElement().$(By.xpath("descendant-or-self::input | descendant-or-self::textarea"));
    }
}
