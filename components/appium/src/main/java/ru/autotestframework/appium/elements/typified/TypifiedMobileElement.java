package ru.autotestframework.appium.elements.typified;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.util.Validator;

public class TypifiedMobileElement extends BaseElement implements IElement, IAccessible {

    private final String TypifiedMobileElementTitle;

    @Getter
    private final SelenideElement selenideElement;

    public TypifiedMobileElement(final WebElement element, final String TypifiedMobileElementTitle) {
        this.TypifiedMobileElementTitle = TypifiedMobileElementTitle;
        this.selenideElement = $(element);
    }

    /**
     * doubleclicks element
     */
    @Override
    public void doubleClick() {
        selenideElement.doubleClick();
    }

    /**
     * rightclicks element
     */
    @Override
    public void rightClick() {
        selenideElement.contextClick();
    }

    /**
     * returns element's title
     * @return
     */
    @Override
    public String getTitle() {
        return TypifiedMobileElementTitle;
    }

    /**
     * checks if element is displayed
     * @return
     */
    @Override
    public boolean isDisplayed() {
        return selenideElement.isDisplayed();
    }

    /**
     * returns element's location
     * @return
     */
    @Override
    public Point getLocation() {
        return getSelenideElement().getLocation();
    }

    /**
     * returns element's size
     * @return
     */
    @Override
    public Dimension getSize() {
        return getSelenideElement().getSize();
    }

    /**
     * returns element's size and location
     * @return
     */
    @Override
    public Rectangle getRect() {
        return getSelenideElement().getRect();
    }

    /**
     * returns element's css value by property name
     * @param propertyName
     * @return
     */
    @Override
    public String getCssValue(final String propertyName) {
        return selenideElement.getCssValue(propertyName);
    }

    /**
     * clicks element
     */
    @Override
    public void click() {
        selenideElement.click();
    }

    /**
     * hovers element
     */
    public void hover() {
        selenideElement.hover();
    }

    /**
     * submits element
     */
    @Override
    public void submit() {
        getSelenideElement().submit();
    }

    /**
     * sends char sequence to element
     * @param keysToSend
     */
    @Override
    public void sendKeys(final CharSequence... keysToSend) {
        getSelenideElement().sendKeys(keysToSend);
    }

    /**
     * clears element
     */
    @Override
    public void clear() {
        getSelenideElement().clear();
    }

    /**
     * returns element's tag name
     * @return
     */
    @Override
    public String getTagName() {
        return getSelenideElement().getTagName();
    }

    /**
     * returns element's attribute by name
     * @param name
     * @return
     */
    @Override
    public String getAttribute(final String name) {
        return getSelenideElement().getAttribute(name);
    }

    /**
     * checks if element is selected
     * @return
     */
    @Override
    public boolean isSelected() {
        return getSelenideElement().isSelected();
    }

    /**
     * checks if element is enabled
     * @return
     */
    @Override
    public boolean isEnabled() {
        return getSelenideElement().isEnabled();
    }

    /**
     * returns element's text
     * @return
     */
    @Override
    public String getText() {
        return getSelenideElement().getText();
    }

    /**
     * returns found elements by
     * @param by
     * @return
     */
    @Override
    public List<WebElement> findElements(final By by) {
        return getSelenideElement().findElements(by);
    }

    /**
     * returns found element by
     * @param by
     * @return
     */
    @Override
    public WebElement findElement(final By by) {
        return getSelenideElement().findElement(by);
    }

    /**
     * returns screenshot of an element
     * @param target
     * @return
     * @param <X>
     * @throws WebDriverException
     */
    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        return getSelenideElement().getScreenshotAs(target);
    }

    /**
     * returns element's coordinates
     * @return
     */
    @Override
    public Coordinates getCoordinates() {
        return selenideElement.getCoordinates();
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return selenideElement.isEnabled();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return selenideElement.isDisplayed();
    }

    /**
     * checks if element has attribute
     * @param attribute
     */
    @Override
    public void hasAttribute(String attribute) {
        Validator.assertThat(
                selenideElement.has(Condition.attribute(attribute)),
                "The element '{}' doesn't have attribute '{}'",
                getTitle(),
                attribute);
    }

    /**
     * returns wrapped element
     * @return
     */
    @Override
    public WebElement getWrappedElement() {
        return selenideElement.getWrappedElement();
    }
}
