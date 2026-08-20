package ru.autotestframework.web_elements.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * Веб-элемент используется для HTML тега &lt;img /&gt;.
 */
public class Image extends TypifiedWebElement implements ISourceable {
    public Image(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns element's source
     * @return
     */
    @Override
    public String getSource() {
        return getImage().getAttribute("src");
    }

    private SelenideElement getImage() {
        return getSelenideElement().$(By.xpath("descendant-or-self::img"));
    }
}
