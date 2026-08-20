package ru.autotestframework.desktop_elements.elements.typified;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.conditions.Visible;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.enums.ImageFormat;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.util.Validator;

/**
 * Class is a wrapper over typified element and implement methods to work with Driver for Windows Desktop Applications.
 * All elements of those applications should extend this Class or subclasses.
 */
@Slf4j
public class TypifiedDesktopElement extends BaseDesktopElement implements IElement, IAccessible {

    public TypifiedDesktopElement(final WebElement element, final String title) {
        super(element, title);
    }

    /**
     * returns element's title
     * @return
     */
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    /**
     * doubleclicks element
     */
    @Override
    public void doubleClick() {
        getSelenideElement().doubleClick();
    }

    /**
     * rightclicks element
     */
    @Override
    public void rightClick() {
        getSelenideElement().contextClick();
    }

    /**
     * hovers element
     */
    @Override
    public void hover() {
        getSelenideElement().hover();
    }

    /**
     * checks if element has given attribute
     * @param attribute
     */
    @Override
    public void hasAttribute(String attribute) {
        Validator.assertThat(
                getSelenideElement().has(Condition.attribute(attribute)),
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
        return $(getSelenideElement()).getWrappedElement();
    }

    /**
     * clicks element
     */
    @Override
    public void click() {
        getSelenideElement().click();
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
    public String getAttribute(final String name) {
        return getSelenideElement().getAttribute(name);
    }

    /**
     * checks if element is selected
     * @return
     */
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
     * checks the condition of an element
     * @param condition
     * @param notReversed
     */
    @Override
    public void shouldBe(Condition condition, boolean notReversed) {

        Condition conditionToCheck = notReversed ? condition : condition.negate();
        var haveError = Validator.hasIgnoreWhenTry(() -> $(this).shouldBe(conditionToCheck));

        var visibleCondition = condition instanceof Visible;

        Validator.assertThat(
                !haveError || !notReversed && visibleCondition,
                "Element '{}' doesn't match the expected {} status (expected '{}')",
                getTitle(),
                conditionToCheck.getName(),
                notReversed);
    }

    /**
     * returns element's text
     * @return
     */
    @Override
    public String getText() {
        return getSelenideElement().getAttribute("Name");
    }

    /**
     * returns found elements by
     * @param by
     * @return
     */
    @Override
    public List<WebElement> findElements(final By by) {
        return getParent().findElements(by);
    }

    /**
     * returns found element by
     * @param by
     * @return
     */
    @Override
    public WebElement findElement(final By by) {
        return getParent().findElement(by);
    }

    /**
     * checks if element is displayed
     * @return
     */
    @Override
    public boolean isDisplayed() {
        return getSelenideElement().isDisplayed();
    }

    /**
     * returns element's location
     * @return
     */
    @Override
    public Point getLocation() {
        Rectangle rect = getRect();
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
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
        var rectString = getSelenideElement().getAttribute("BoundingRectangle");
        assert rectString != null;
        String[] rect = rectString.split(",");
        if (Integer.parseInt(rect[2].trim()) < 1 || Integer.parseInt(rect[3].trim()) < 1) {
            throw new AutotestException("Element have zero size");
        }
        return new Rectangle(
                Integer.parseInt(rect[0].trim()),
                Integer.parseInt(rect[1].trim()),
                Integer.parseInt(rect[3].trim()),
                Integer.parseInt(rect[2].trim()));
    }

    /**
     * returns element's css value by property name
     * @param propertyName
     * @return
     */
    @Override
    public String getCssValue(final String propertyName) {
        return getSelenideElement().getCssValue(propertyName);
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
        return super.getScreenshot(target, ImageFormat.PNG, true);
    }

    /**
     * returns coordinates of an element
     * @return
     */
    @Override
    public Coordinates getCoordinates() {
        return new Coordinates() {

            @Override
            public Point onScreen() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            /**
             * scrolls element into view
             * @return
             */
            @Override
            public Point inViewPort() {
                getSelenideElement().scrollIntoView(true);
                return getSelenideElement().getLocation();
            }

            /**
             * returns element's location
             * @return
             */
            @Override
            public Point onPage() {
                return getLocation();
            }

            @Override
            public Object getAuxiliary() {
                throw new ElementInteractionException(
                        "You should try to use 'TypifiedDesktopElement.getSelenideElement()' instead TypifiedDesktopElement in your methods");
            }
        };
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return getSelenideElement().isEnabled();
    }

    /**
     * check if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return getSelenideElement().isDisplayed();
    }
}
