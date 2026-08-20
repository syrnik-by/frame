package ru.autotestframework.java_junit.elements.typified;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.util.Validator;

public class TypifiedJavaElement extends BaseElement implements IElement, IAccessible {

    public static final long MILLIS_IN_SECOND = 1000L;
    private final String title;

    @Getter
    private final SelenideElement selenideElement;

    public TypifiedJavaElement(final WebElement element, final String title) {
        this.title = title;
        this.selenideElement = Selenide.$(element);
    }

    @Override
    public void doubleClick() {
        selenideElement.doubleClick();
    }

    @Override
    public void rightClick() {
        selenideElement.contextClick();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isDisplayed() {
        return selenideElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return selenideElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return selenideElement.getSize();
    }

    @Override
    public Rectangle getRect() {
        return selenideElement.getRect();
    }

    @Override
    public String getCssValue(final String propertyName) {
        return selenideElement.getCssValue(propertyName);
    }

    @Override
    public void click() {
        selenideElement.click();
    }

    public void hover() {
        selenideElement.hover();
    }

    @Override
    public void hasAttribute(String attribute) {
        String attr = selenideElement.getAttribute(attribute);
        Validator.assertThat(
                attr != null && !attr.contains("status=0"),
                "The element '{}' doesn't have attribute '{}'",
                getTitle(),
                attribute);
    }

    @Override
    public void submit() {
        selenideElement.submit();
    }

    @Override
    public void sendKeys(final CharSequence... keysToSend) {
        selenideElement.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        selenideElement.clear();
    }

    @Override
    public String getTagName() {
        return selenideElement.getTagName();
    }

    @Override
    public String getAttribute(final String name) {
        return selenideElement.getAttribute(name);
    }

    // TODO should return false on default and be overridden in concrete elements
    // because javadriver + selenide methods relies to specific field types (ex JTextField not support isSelected)
    @Override
    public boolean isSelected() {
        return selenideElement.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return selenideElement.isEnabled();
    }

    @Override
    public String getText() {
        return selenideElement.getText();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return selenideElement.findElements(by);
    }

    @Override
    public WebElement findElement(final By by) {
        return selenideElement.findElement(by);
    }

    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        return selenideElement.getScreenshotAs(target);
    }

    @Override
    public WebElement getWrappedElement() {
        return selenideElement.getWrappedElement();
    }

    @Override
    public Coordinates getCoordinates() {
        return selenideElement.getCoordinates();
    }

    public void waitElement() {
        long waitingTime = 0;
        long startLoadingTime = System.currentTimeMillis();
        while (!selenideElement.isEnabled()) {
            if (waitingTime <= Integer.parseInt(System.getProperty("framework.ui.timeout")) * MILLIS_IN_SECOND) {
                waitingTime = System.currentTimeMillis() - startLoadingTime;
            } else {
                break;
            }
        }
    }

    @Override
    public boolean isAccessed() {
        return selenideElement.isEnabled();
    }

    @Override
    public boolean isVisible() {
        return selenideElement.isDisplayed();
    }
}
