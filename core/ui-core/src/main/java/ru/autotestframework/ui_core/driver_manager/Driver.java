package ru.autotestframework.ui_core.driver_manager;

import static java.lang.Math.max;
import static ru.autotestframework.Constants.KEYS_DELIMETER;

import com.codeborne.selenide.WebDriverRunner;
import com.google.common.base.Ascii;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.services.element_locator.IElementLocator;

/**
 * Driver.
 */
@Data
public abstract class Driver implements IModuledActions {
    private final String path;

    @Getter
    private final String propertyPath;

    private String name;

    private WebDriver webDriver;

    /**
     * Gets typified element class name.
     *
     * @return the typified element class name
     */
    public abstract String getTypifiedElementClassName();

    /**
     * Instantiates a new Driver.
     *
     * @param path         the path
     * @param propertyPath the property path
     */
    protected Driver(final String path, final String propertyPath) {
        this.name = getName(path);
        this.path = path;
        this.propertyPath = propertyPath;
    }

    /**
     * Return initialised WebDriver (Create Driver on given configuration if not).
     *
     * @return WebDriver driver
     */
    public WebDriver getDriver() {
        if (Objects.isNull(webDriver)) {
            try {
                webDriver = build();
            } catch (IllegalStateException e) {
                throw new ConfigurationException("{} driver initialization failure", e, name);
            }
        }
        if (webDriver != null) {
            WebDriverRunner.setWebDriver(webDriver);
        }

        IElementLocator.setClassNames(getTypifiedElementClassName());
        return WebDriverRunner.hasWebDriverStarted() ? WebDriverRunner.getWebDriver() : null;
    }

    /**
     * Quit web driver.
     */
    public void quitWebDriver() {
        WebDriverRunner.getWebDriver().quit();
        webDriver = null;
    }

    private String getName(final String pathToParse) {
        return pathToParse.substring(
                        pathToParse.lastIndexOf("/") + 1 == 0
                                ? pathToParse.lastIndexOf("\\") + 1
                                : pathToParse.lastIndexOf("/") + 1)
                .split("\\.")[0];
    }

    /**
     * Take screenshot file.
     *
     * @return the file
     */
    public File takeScreenshot() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.FILE);
    }

    /**
     * Release.
     */
    public void release() {
        quitWebDriver();
    }

    /**
     * Press on key board.
     *
     * @param keysCombination the keys combination
     */
    public void pressOnKeyBoard(String keysCombination) {
        List<CharSequence> sequence = Arrays.stream(keysCombination.split(KEYS_DELIMETER))
                .map(Driver::parseButton)
                .collect(Collectors.toList());

        var actions = createActionsSequence(
                sequence.subList(0, max(sequence.size() - 1, 0)), sequence.get(sequence.size() - 1));
        actions.build().perform();
    }

    private static CharSequence parseButton(final String text) {
        Optional<Keys> optKey =
                Arrays.stream(Keys.values()).filter(x -> x.name().equals(text)).findFirst();
        return optKey.map(keys -> (CharSequence) keys).orElse(text);
    }

    private Actions createActionsSequence(List<CharSequence> pressedButtons, CharSequence enteredSequence) {
        var actions = new Actions(WebDriverRunner.getWebDriver());
        for (var button : pressedButtons) {
            actions = actions.keyDown(button);
        }
        boolean isControlKeyDown = (pressedButtons.contains(Keys.CONTROL));
        CharSequence resolvedSequence = isControlKeyDown ? Ascii.toLowerCase(enteredSequence) : enteredSequence;
        for (var i = 1; i <= enteredSequence.length(); i++) {
            actions = actions.sendKeys(resolvedSequence.subSequence(i - 1, i));
        }
        for (var button : pressedButtons) {
            actions = actions.keyUp(button);
        }
        return actions;
    }

    /**
     * Build web driver.
     *
     * @return the web driver
     */
    public abstract WebDriver build();
}
