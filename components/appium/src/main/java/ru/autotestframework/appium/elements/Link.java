package ru.autotestframework.appium.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISourceable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

public class Link extends TypifiedMobileElement implements IReadable, IVerifiable, ISourceable, IAccessible {

    public Link(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns Link value
     * @return
     */
    @Override
    public String readValue() {
        return getLink().innerText().trim();
    }

    /**
     * verifies Link
     * @param expected value to verifie
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
     * returns Link's source
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
