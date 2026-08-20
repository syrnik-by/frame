package ru.autotestframework.web_elements.elements;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.conditions.WebEditable;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * Веб-элемент используется для HTML тегов &lt;textarea&gt;&lt;/textarea&gt; и &lt;input&gt; (кроме type="button").
 */
public class TextInput extends TypifiedWebElement implements ICleanable, IWritable, IReadable, IAccessible {

    public TextInput(final WebElement wrappedElement, final String name) {
        super(wrappedElement, name);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns value of TextInput element
     * @return
     */
    @Override
    public String readValue() {
        final SelenideElement input = getInput();
        return input.getAttribute("value");
    }

    /**
     * verifies element's value
     * @param expected ожидаемое значение для проверки
     * @return
     */
    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }

    /**
     * writes to element
     * @param value
     */
    @Override
    public void write(final String value) {
        getInput().sendKeys(value);
    }

    /**
     * checks if element is editable
     * @return
     */
    @Override
    public boolean isEditable() {
        CheckResult check = new WebEditable().check(WebDriverRunner.driver(), this);
        return check.verdict.equals(CheckResult.Verdict.ACCEPT);
    }

    private SelenideElement getInput() {
        return getSelenideElement().$(By.xpath("descendant-or-self::input | descendant-or-self::textarea"));
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return getInput().isEnabled();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return getInput().isDisplayed();
    }

    @Override
    public void clean() {
        getInput().clear();
    }
}
