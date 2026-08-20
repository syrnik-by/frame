package ru.autotestframework.web_elements.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * Веб-элемент используется для HTML тега &lt;iframe&gt;&lt;/iframe&gt;.
 */
public class IFrame extends TypifiedWebElement implements ISourceable {
    public IFrame(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns element's source
     * @return
     */
    @Override
    public String getSource() {
        return getFrame().getAttribute("src");
    }

    private SelenideElement getFrame() {
        return getSelenideElement().$(By.xpath("descendant-or-self::iframe"));
    }
}
