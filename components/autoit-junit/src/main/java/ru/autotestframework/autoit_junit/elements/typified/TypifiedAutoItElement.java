package ru.autotestframework.autoit_junit.elements.typified;

import com.codeborne.selenide.Condition;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import ru.autotestframework.autoitx.AutoItX;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.util.Validator;

/**
 * Typified auto it element.
 */
public class TypifiedAutoItElement extends BaseElement implements IElement {

    /**
     * The constant autoItX.
     */
    public static final AutoItX autoItX = new AutoItX();

    /**
     * The Win title.
     */
    public final String winTitle;
    /**
     * The Typified auto it element title.
     */
    public final String TypifiedAutoItElementTitle;
    /**
     * The Control.
     */
    public final String control;

    /**
     * Instantiates a new Typified auto it element.
     *
     * @param winTitle                   the win title
     * @param TypifiedAutoItElementTitle the typified auto it element title
     * @param control                    the control
     */
    public TypifiedAutoItElement(String winTitle, String TypifiedAutoItElementTitle, String control) {
        this.winTitle = winTitle;
        this.TypifiedAutoItElementTitle = TypifiedAutoItElementTitle;
        this.control = control;
    }

    @Override
    public String getTitle() {
        return TypifiedAutoItElementTitle;
    }

    @Override
    public void doubleClick() {
        waitWinActive();
        autoItX.controlClick(winTitle, "", control, "", 2);
    }

    @Override
    public void rightClick() {
        waitWinActive();
        autoItX.controlClick(winTitle, "", control, "right");
    }

    @Override
    public void hover() {
        waitWinActive();
        autoItX.controlFocus(winTitle, "", control);
    }

    @Override
    public void hasAttribute(String attribute) {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Override
    public void click() {
        waitWinActive();
        autoItX.controlClick(winTitle, "", control);
    }

    @Override
    public void submit() {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        click();
        autoItX.send(Arrays.toString(keysToSend));
    }

    @Override
    public void clear() {
        waitWinActive();
        autoItX.ControlSetText(winTitle, "", control, "");
    }

    @Override
    public String getTagName() {
        waitWinActive();
        return autoItX.controlGetHandle(winTitle, "", control);
    }

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        waitWinActive();
        return autoItX.controlEnable(winTitle, "", control);
    }

    @Override
    public String getText() {
        waitWinActive();
        return autoItX.controlGetText(winTitle, "", control);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return Collections.emptyList();
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public boolean isDisplayed() {
        waitWinActive();
        return autoItX.controlCommandIsVisible(winTitle, "", control);
    }

    @Override
    public Point getLocation() {
        waitWinActive();
        return new Point(autoItX.controlGetPosX(winTitle, "", control), autoItX.controlGetPosY(winTitle, "", control));
    }

    @Override
    public Dimension getSize() {
        waitWinActive();
        return new Dimension(
                autoItX.controlGetPosWidth(winTitle, "", control), autoItX.controlGetPosHeight(winTitle, "", control));
    }

    @Override
    public Rectangle getRect() {
        waitWinActive();
        return new Rectangle(getLocation(), getSize());
    }

    @Override
    public String getCssValue(String propertyName) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }

    @Override
    public WebElement getWrappedElement() {
        return null;
    }

    @Override
    public Coordinates getCoordinates() {
        return null;
    }

    /**
     * Wait win active.
     */
    public void waitWinActive() {
        autoItX.winActivate(winTitle);
        autoItX.winWaitActive(winTitle, "", Integer.parseInt(System.getProperty("framework.ui.timeout")));
    }

    @Override
    public void shouldBe(final Condition condition, final boolean notReversed) {
        boolean be;
        if (Condition.visible.equals(condition)) {
            be = this.isDisplayed();
        } else if (Condition.enabled.equals(condition)) {
            be = this.isEnabled();
        } else {
            throw new ConfigurationException("Condition '{}' doesn't support for screen elements", condition.getName());
        }
        Validator.assertThat(
                notReversed == be,
                "Element '{}' doesn't match the expected {} status (expected '{}')",
                getTitle(),
                condition.getName(),
                notReversed);
    }
}
