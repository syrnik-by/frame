package ru.autotestframework.screen_elements.elements.typified;

import com.codeborne.selenide.Condition;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import org.sikuli.script.Key;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.util.Validator;

/**
 * Typified screen element.
 */
public class TypifiedScreenElement extends BaseElement implements IElement {

    /**
     * The constant SCREEN.
     */
    public static final Screen SCREEN = new Screen();

    private final String typifiedScreenElementTitle;

    @Getter
    private final String regionLocation;

    @Getter
    private final String source;

    @Getter
    private final int searchType;

    @Getter
    private final int offsetX;

    @Getter
    private final int offsetY;

    private Pattern pattern;

    /**
     * Instantiates a new Typified screen element.
     *
     * @param typifiedScreenElementTitle the typified screen element title
     * @param regionLocation             the region location
     * @param source                     the source
     * @param offsetX                    the offset x
     * @param offsetY                    the offset y
     */
    public TypifiedScreenElement(
            final String typifiedScreenElementTitle,
            final String regionLocation,
            final String source,
            final int offsetX,
            final int offsetY) {

        this.typifiedScreenElementTitle = typifiedScreenElementTitle;
        this.regionLocation = regionLocation;
        this.source = source;
        this.searchType = -1;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Instantiates a new Typified screen element.
     *
     * @param typifiedScreenElementTitle the typified screen element title
     * @param regionLocation             the region location
     * @param source                     the source
     * @param searchType                 the search type
     * @param offsetX                    the offset x
     * @param offsetY                    the offset y
     */
    public TypifiedScreenElement(
            final String typifiedScreenElementTitle,
            final String regionLocation,
            final String source,
            final int searchType,
            final int offsetX,
            final int offsetY) {

        this.typifiedScreenElementTitle = typifiedScreenElementTitle;
        this.regionLocation = regionLocation;
        this.source = source;
        this.searchType = searchType;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Gets pattern.
     *
     * @return the pattern
     */
    public Pattern getPattern() {
        if (pattern == null) {
            pattern = getNewPattern(source);
        }
        return pattern;
    }

    private Pattern getNewPattern(String source) {
        return new Pattern(Thread.currentThread().getContextClassLoader().getResource(source));
    }

    @Override
    public String getTitle() {
        return this.typifiedScreenElementTitle;
    }

    @SneakyThrows
    @Override
    public void click() {
        waitElement().click();
    }

    @Override
    public void doubleClick() {
        waitElement().doubleClick();
    }

    @Override
    public void rightClick() {
        waitElement().rightClick();
    }

    @Override
    public void hover() {
        waitElement().hover();
    }

    @Override
    public void hasAttribute(String attribute) {
        throw new ConfigurationException("Method 'submit' doesn't support for screen elements");
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

    @Override
    public void submit() {
        throw new ConfigurationException("Method 'submit' doesn't support for screen elements");
    }

    @Override
    public void sendKeys(final CharSequence... keysToSend) {
        throw new ConfigurationException("Method 'sendKeys' doesn't support for screen elements");
    }

    @Override
    public void clear() {
        click();
        getRegion().type("a", Key.CTRL);
        getRegion().type(Key.BACKSPACE);
    }

    @Override
    public String getTagName() {
        return getPattern().getFilename();
    }

    @Override
    public String getAttribute(final String name) {
        throw new ConfigurationException("Method 'getAttribute' doesn't support for screen elements");
    }

    @Override
    public boolean isSelected() {
        throw new ConfigurationException("Method 'isSelected' doesn't support for screen elements");
    }

    @Override
    public boolean isEnabled() {
        return waitElement().isValid();
    }

    @Override
    public String getText() {
        return waitElement().text();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        throw new ConfigurationException("Method 'findElements' doesn't support for screen elements");
    }

    @Override
    public WebElement findElement(final By by) {
        throw new ConfigurationException("Method 'findElement' doesn't support for screen elements");
    }

    @Override
    public boolean isDisplayed() {
        return getRegion().has(getPattern());
    }

    @Override
    public Point getLocation() {
        return new Point(
                getPattern().getTargetOffset().getX(),
                getPattern().getTargetOffset().getY());
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(final String propertyName) {
        throw new ConfigurationException("Method 'getCssValue' doesn't support for screen elements");
    }

    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        return null;
    }

    @Override
    public WebElement getWrappedElement() {
        throw new ConfigurationException("Method 'getWrappedElement' doesn't support for screen elements");
    }

    @Override
    public Coordinates getCoordinates() {
        return null;
    }

    /**
     * Wait element region.
     *
     * @return the region
     */
    public Region waitElement() {
        try {
            return getRegion()
                    .wait(getPattern(), Float.parseFloat(System.getProperty("framework.ui.timeout")))
                    .offset(offsetX, offsetY);
        } catch (Exception e) {
            throw new InitializationException(e.getMessage(), e);
        }
    }

    /**
     * Gets region.
     *
     * @return the region
     */
    public Region getRegion() {
        try {
            return !regionLocation.equals("") ? SCREEN.find(getNewPattern(regionLocation)) : SCREEN;
        } catch (Exception e) {
            throw new InitializationException(e.getMessage(), e);
        }
    }

    /**
     * Copy to clipboard.
     *
     * @param text the text
     */
    public void copyToClipboard(final String text) {
        var stringSelection = new StringSelection(text);
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
