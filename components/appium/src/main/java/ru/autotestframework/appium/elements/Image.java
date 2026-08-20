package ru.autotestframework.appium.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;

public class Image extends TypifiedMobileElement implements ISourceable {
    public Image(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns Image's source
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
