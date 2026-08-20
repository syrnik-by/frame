package ru.autotestframework.web_elements.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * Веб-элемент используется только для html тегов &lt;button&gt;&lt;/button&gt; и &lt;input type="button"&gt;.
 */
public class Button extends TypifiedWebElement implements IReadable, IVerifiable, ISourceable, IAccessible {

    public Button(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns value of Button element
     * @return
     */
    @Override
    public String readValue() {
        final SelenideElement button = getButton();
        return "input".equals(button.getTagName()) ? button.getAttribute("value") : button.getText();
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
     * returns element's source
     * @return
     */
    @Override
    public String getSource() {
        return getButton().getAttribute("formaction");
    }

    private SelenideElement getButton() {
        return getSelenideElement()
                .$(By.xpath("descendant-or-self::button | " + "descendant-or-self::input[@type = 'button']"));
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return getButton().isEnabled();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return getButton().isDisplayed();
    }
}
