package ru.autotestframework.desktop_elements.driver_manager.drivers;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Condition;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.desktop_elements.actions.MouseActions;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;
import ru.autotestframework.desktop_elements.driver_builder.DesktopDriverBuilder;
import ru.autotestframework.desktop_elements.driver_builder.PropertiesBuilder;
import ru.autotestframework.desktop_elements.elements.typified.BaseDesktopElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.BasePoint;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.util.Validator;

@Slf4j
public class DriverDesktop extends Driver {
    public static String staticPath;
    /**
     * returns class name of an element
     * @return
     */
    @Override
    public String getTypifiedElementClassName() {
        return TypifiedDesktopElement.class.getName();
    }

    public DriverDesktop(final String path, final String propertyPath) {
        super(path, propertyPath);
    }

    /**
     * returns element's size and location
     * @param element
     * @return
     */
    public static Rectangle getElementRect(final BaseDesktopElement element) {
        return element.withTimeout(0).getRect();
    }

    /**
     * builds driver with properties and capabilites
     * @return
     */
    @Override
    public WebDriver build() {
        staticPath = super.getPath();
        var propertiesBuilder = new PropertiesBuilder().withProperties(getPropertyPath());
        var desktopDriverConfiguration =
                propertiesBuilder.withDesiredCapabilities().build();
        return new DesktopDriverBuilder(desktopDriverConfiguration).build();
    }

    /**
     * returns screenshot of a window as file
     * @return
     */
    @SneakyThrows
    @Override
    public File takeScreenshot() {
        File screen = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            var window = ((DesktopDriver) getDriver()).getActiveWindow();
            Rectangle rect = getElementRect(window);
            ImageIO.write(ImageIO.read(screen).getSubimage(rect.x, rect.y, rect.width, rect.height), "png", screen);
        } catch (Exception e) {
            log.warn("Unable to get subImage, returning full screen");
        }
        return screen;
    }

    /**
     * clicks on element by text
     * @param text
     */
    @Override
    public void clickByText(String text) {
        getDriver().findElement(By.name(text)).click();
    }

    /**
     * drags and drops given element to another element
     * @param elementFrom
     * @param elementTo
     */
    @Override
    public void dragAndDrop(IElement elementFrom, IElement elementTo) {
        final Point elementFromPos = elementFrom.getLocation();
        final Point elementToPos = elementTo.getLocation();
        new MouseActions((TypifiedDesktopElement) elementFrom)
                .dragAndDrop(
                        BasePoint.CENTER, 0, 0, elementToPos.x - elementFromPos.x, elementToPos.y - elementFromPos.y);
    }

    /**
     * checks if text is visible
     * @param textContent
     */
    @Override
    public void visibleMultilineText(String textContent) {
        checkVisibleText(textContent, true);
    }

    /**
     * waits for text to disappear
     * @param seconds
     * @param text
     */
    @Override
    public void waitForElementByTextToDisappear(int seconds, String text) {
        final var element = getDriver().findElement(By.name(text));
        isVisible(seconds, element, text);
    }

    /**
     * waits for element to disapear
     * @param seconds
     * @param element
     */
    @Override
    public void waitForElementToDisappear(int seconds, IElement element) {
        isVisible(seconds, element, element.getTitle());
    }

    /**
     * checks if text is visible
     * @param text
     * @param isDisplayed
     */
    @Override
    public void checkVisibleText(final String text, final Boolean isDisplayed) {
        Condition visibleCondition = isDisplayed ? Condition.visible : Condition.visible.negate();
        Validator.tryOrAssertion(
                () -> $x("//*[contains(@Name,'" + text + "')]").shouldBe(visibleCondition),
                "Element with text '{}' doesn't match the expected displayed status (expected '{}')",
                text,
                isDisplayed);
    }

    private void isVisible(int seconds, final WebElement element, final String elInfo) {
        long startTime = System.currentTimeMillis();
        var elementVisible = true;
        while (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) <= seconds) {
            try {
                if (!element.isDisplayed()) {
                    elementVisible = false;
                    break;
                }
            } catch (Exception e) {
                elementVisible = false;
                break;
            }
        }
        if (elementVisible) {
            throw new AutotestException("Element '{}' not disappear in {} seconds", elInfo, seconds);
        }
    }
}
