package ru.autotestframework.web_elements.elements.typified;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.services.element_locator.IElementLocator;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.util.Validator;

/**
 * Base Class for WebElement
 */
public class TypifiedWebElement extends BaseElement implements IElement, WrapsElement, IAccessible {

    private final String typifiedWebElementTitle;

    @Getter
    private final SelenideElement selenideElement;

    private final WebElement wrappedElement;

    public TypifiedWebElement(final WebElement element, final String typifiedWebElementTitle) {
        this.typifiedWebElementTitle = typifiedWebElementTitle;
        this.selenideElement = $(element).as(typifiedWebElementTitle);
        wrappedElement = element;
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
     *
     * @return
     */
    @Override
    public String getTitle() {
        return typifiedWebElementTitle;
    }

    /**
     * checks if element is displayed
     *
     * @return
     */
    @Override
    public boolean isDisplayed() {
        return selenideElement.isDisplayed();
    }

    /**
     * returns element's location
     *
     * @return
     */
    @Override
    public Point getLocation() {
        return getSelenideElement().getLocation();
    }

    /**
     * returns element's size
     *
     * @return
     */
    @Override
    public Dimension getSize() {
        return getSelenideElement().getSize();
    }

    /**
     * returns element's location and size
     *
     * @return
     */
    @Override
    public Rectangle getRect() {
        return getSelenideElement().getRect();
    }

    /**
     * returns elements css value by property name
     *
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
     * sends charsequence to element
     *
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
     *
     * @return
     */
    @Override
    public String getTagName() {
        return getWrappedElement().getTagName();
    }

    /**
     * returns element's attribute
     *
     * @param name
     * @return
     */
    @Override
    public String getAttribute(final String name) {
        return getSelenideElement().getAttribute(name);
    }

    /**
     * checks if element is selected
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return getSelenideElement().isSelected();
    }

    /**
     * checks if element is enabled
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return getSelenideElement().isEnabled();
    }

    /**
     * returns element's text
     *
     * @return
     */
    @Override
    public String getText() {
        return getSelenideElement().getText();
    }

    /**
     * returns found elements by
     *
     * @param by
     * @return
     */
    @Override
    public List<WebElement> findElements(final By by) {
        return getSelenideElement().findElements(by);
    }

    /**
     * returns found element by
     *
     * @param by
     * @return
     */
    @Override
    public WebElement findElement(final By by) {
        return getSelenideElement().findElement(by);
    }

    /**
     * returns screenshot of the element
     *
     * @param target
     * @param <X>
     * @return
     * @throws WebDriverException
     */
    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        return getSelenideElement().getScreenshotAs(target);
    }

    @Override
    public WebElement getWrappedElement() {
        return selenideElement.getWrappedElement();
    }

    /**
     * returns element's coordinates
     *
     * @return
     */
    @Override
    public Coordinates getCoordinates() {
        return selenideElement.getCoordinates();
    }

    /**
     * checks if element is accessed
     *
     * @return
     */
    @Override
    public boolean isAccessed() {
        return selenideElement.isEnabled();
    }

    /**
     * checks if element is visible
     *
     * @return
     */
    @Override
    public boolean isVisible() {
        return selenideElement.isDisplayed();
    }

    /**
     * checks condition of an element
     *
     * @param condition
     * @param notReversed
     * @param time
     */
    @Override
    public void shouldBe(Condition condition, boolean notReversed, Duration time) {
        Condition conditionToCheck = notReversed ? condition : condition.negate();
        $(this).shouldBe(conditionToCheck, time);
        try {
            $(getBy(this.getWrappedElement())).shouldBe(conditionToCheck, time);
        } catch (Exception ignored) {
            $(this).shouldBe(conditionToCheck);
        }
    }

    /**
     * checks condition of an element
     *
     * @param condition
     * @param notReversed
     */
    @Override
    public void shouldBe(Condition condition, boolean notReversed) {
        Condition conditionToCheck = notReversed ? condition : condition.negate();
        $(this).shouldBe(conditionToCheck);
        try {
            $(getBy(this.getWrappedElement())).shouldBe(conditionToCheck);
        } catch (Exception ignored) {
            $(this).shouldBe(conditionToCheck);
        }
    }

    /**
     * checks if element has attribute
     *
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

    private By getBy(WebElement wrappedElement) {
        LocatingElementHandler invocationHandler = (LocatingElementHandler) Proxy.getInvocationHandler(wrappedElement);
        IElementLocator locator = (IElementLocator) ReflectionTestUtils.getField(invocationHandler, "locator");
        return (By) ReflectionTestUtils.getField(locator, "by");
    }
}
