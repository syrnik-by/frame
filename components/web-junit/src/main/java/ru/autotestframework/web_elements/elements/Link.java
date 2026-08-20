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
 * Веб-элемент используется для HTML тега &lt;a&gt;&lt;/a&gt;.
 */
public class Link extends TypifiedWebElement implements IReadable, IVerifiable, ISourceable, IAccessible {

    public Link(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns value of Link element
     * @return
     */
    @Override
    public String readValue() {
        return getLink().innerText().trim();
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

    private SelenideElement getLink() {
        return getSelenideElement().$(By.xpath("descendant-or-self::a"));
    }

    /**
     * returns element's source
     * @return
     */
    @Override
    public String getSource() {
        return getLink().getAttribute("href");
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return getLink().isEnabled();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return getLink().isDisplayed();
    }
}
