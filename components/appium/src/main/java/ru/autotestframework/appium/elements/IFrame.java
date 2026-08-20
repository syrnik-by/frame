package ru.autotestframework.appium.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;

public class IFrame extends TypifiedMobileElement implements ISourceable {
    public IFrame(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * returns IFrame's source
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
