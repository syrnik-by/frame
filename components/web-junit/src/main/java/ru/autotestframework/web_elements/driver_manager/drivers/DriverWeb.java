package ru.autotestframework.web_elements.driver_manager.drivers;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.*;
import com.codeborne.selenide.ex.UIAssertionError;
import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import ru.autotestframework.Constants;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.util.Validator;
import ru.autotestframework.web_elements.driver_builder.PropertiesBuilder;
import ru.autotestframework.web_elements.driver_builder.WebDriverBuilder;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;
import ru.autotestframework.web_elements.services.HighlightAspectService;
import ru.autotestframework.web_elements.services.SelenideWebDriverProvider;

@Slf4j
public class DriverWeb extends Driver {

    @Override
    public String getTypifiedElementClassName() {
        return TypifiedWebElement.class.getName();
    }

    public DriverWeb(final String path, final String propertyPath) {
        super(path, propertyPath);
    }

    /**
     * Builds web driver with properties
     *
     * @return
     */
    @Override
    public WebDriver build() {
        var propertiesBuilder = new PropertiesBuilder().withProperties(getPropertyPath());
        var webDriverConfiguration = propertiesBuilder.withChromeOptions().build();
        webDriverConfiguration.getChromeOptions().getCapability("headless");
        com.codeborne.selenide.Configuration.browserCapabilities = webDriverConfiguration
                .getChromeOptions()
                .merge(com.codeborne.selenide.Configuration.browserCapabilities);
        com.codeborne.selenide.Configuration.baseUrl = webDriverConfiguration
                .getProperties()
                .getProperty("path", com.codeborne.selenide.Configuration.baseUrl);
        com.codeborne.selenide.Configuration.browserBinary = webDriverConfiguration
                .getProperties()
                .getProperty("browser.path", com.codeborne.selenide.Configuration.browserBinary);
        com.codeborne.selenide.Configuration.pageLoadStrategy = webDriverConfiguration
                .getProperties()
                .getProperty("pageLoadStrategy", com.codeborne.selenide.Configuration.pageLoadStrategy);

        com.codeborne.selenide.Configuration.downloadsFolder = Constants.TEMP_UI_FOLDER;

        com.codeborne.selenide.Configuration.holdBrowserOpen = !Boolean.parseBoolean(webDriverConfiguration
                .getProperties()
                .getProperty(
                        "framework.ui.closeOnFail",
                        String.valueOf(!com.codeborne.selenide.Configuration.holdBrowserOpen)));

        com.codeborne.selenide.Configuration.clickViaJs = Boolean.parseBoolean(webDriverConfiguration
                .getProperties()
                .getProperty("clickViaJs", String.valueOf(com.codeborne.selenide.Configuration.clickViaJs)));
        com.codeborne.selenide.Configuration.driverManagerEnabled = false;
        com.codeborne.selenide.Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = false;

        boolean highlightEnabled = Boolean.parseBoolean(
                webDriverConfiguration.getProperties().getProperty("framework.ui.aspects.highlight.enabled"));

        if (highlightEnabled) {
            com.codeborne.selenide.Configuration.browser = HighlightWebDriverProvider.class.getName();
        } else {
            com.codeborne.selenide.Configuration.browser = SelenideWebDriverProvider.class.getName();
        }

        return new WebDriverBuilder(webDriverConfiguration).build();
    }

    /**
     * If reusing browser is disabled quits web driver or restores default and quits
     */
    @Override
    public void release() {
        if (!Boolean.getBoolean(Constants.ENABLE_BROWSER_REUSE)) {
            quitWebDriver();
            WebDriverManager.getInstance().remove(this);
        } else {
            WebDriver driver = getDriver();
            if (driver != null) {
                restoreDefault(driver);
            }
        }
    }

    /**
     * Restores driver to default configuration
     *
     * @param driver
     */
    public void restoreDefault(WebDriver driver) {
        driver.manage().deleteAllCookies();
        try {
            ((WebStorage) driver).getLocalStorage().clear();
            ((WebStorage) driver).getSessionStorage().clear();
        } catch (WebDriverException e) {
            log.error("Unable clear storage due Error!", e);
        }
    }

    /**
     * Checks visibility of text
     *
     * @param text
     * @param isDisplayed
     */
    @Override
    public void checkVisibleText(String text, final Boolean isDisplayed) {
        var by = withText(text);
        Condition visibleCondition = isDisplayed ? Condition.visible : Condition.visible.negate();
        Validator.tryOrAssertion(
                () -> $(by).shouldBe(visibleCondition),
                "Element with text '{}' doesn't match the expected displayed status (expected '{}')",
                text,
                isDisplayed);
    }

    /**
     * Clicks on element found by text
     *
     * @param text
     */
    @Override
    public void clickByText(String text) {
        WebDriverRunner.getWebDriver()
                .findElement(By.xpath("//*[contains(text(), '" + text + "')]"))
                .click();
    }

    private boolean isHtml5Draggable(final WebElement element) {
        return Optional.ofNullable(element.getAttribute("draggable"))
                .orElse("false")
                .equalsIgnoreCase("true");
    }

    /**
     * Drag and drops element
     *
     * @param elementFrom
     * @param elementTo
     */
    @Override
    public void dragAndDrop(IElement elementFrom, IElement elementTo) {

        if (isHtml5Draggable(elementFrom)) {
            log.warn("Element '{}' have 'draggable' HTML5 attribute : trying with JS", elementFrom.getTitle());
            $(elementFrom).dragAndDropTo($(elementTo));
        } else {
            log.warn("Element '{}' not have 'draggable' HTML5 attribute, trying with Actions", elementFrom.getTitle());
            var actions = new Actions(WebDriverRunner.getWebDriver());
            actions.dragAndDrop(elementFrom, elementTo).build().perform();
        }
    }

    /**
     * Checks visibility of text content
     *
     * @param textContent
     */
    @Override
    public void visibleMultilineText(String textContent) {
        checkVisibleText(textContent, true);
    }

    /**
     * Waits for element with text to disappear
     *
     * @param seconds
     * @param text
     */
    @Override
    public void waitForElementByTextToDisappear(int seconds, String text) {
        final SelenideElement element = Selenide.$(withText(text));
        try {
            element.should(Condition.disappear, Duration.ofSeconds(seconds));
        } catch (UIAssertionError e) {
            throw new AssertionError(
                    StringUtil.format("Element with text '{}' not disappear in {} seconds", text, seconds), e);
        }
    }

    /**
     * Waits for element to disappear
     *
     * @param seconds
     * @param element
     */
    @Override
    public void waitForElementToDisappear(int seconds, IElement element) {
        try {
            $(element).should(Condition.disappear, Duration.ofSeconds(seconds));
        } catch (UIAssertionError e) {
            throw new AssertionError(
                    StringUtil.format("Element '{}' not disappear in {} seconds", element.getTitle(), seconds), e);
        }
    }
}

class HighlightWebDriverProvider extends SelenideWebDriverProvider {

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        WebDriver webDriver = new ChromeDriver(capabilities);
        return new EventFiringDecorator(new HighlightAspectService()).decorate(webDriver);
    }
}
