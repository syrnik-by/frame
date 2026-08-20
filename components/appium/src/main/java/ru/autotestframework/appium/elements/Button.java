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

public class Button extends TypifiedMobileElement implements IReadable, IVerifiable, ISourceable, IAccessible {

    public Button(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns Button value
     * @return
     */
    @Override
    public String readValue() {
        final SelenideElement button = getButton();
        return "input".equals(button.getTagName()) ? button.getAttribute("value") : button.getText();
    }

    /**
     * verifies Button
     * @param expected value to verifie
     * @return
     */
    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }

    /**
     * returns Button's source
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
