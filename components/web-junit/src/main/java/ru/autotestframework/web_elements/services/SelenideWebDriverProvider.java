package ru.autotestframework.web_elements.services;

import com.codeborne.selenide.WebDriverProvider;
import javax.annotation.Nonnull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class SelenideWebDriverProvider implements WebDriverProvider {

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        WebDriver webDriver = new ChromeDriver(capabilities);
        if (Boolean.getBoolean("HIGHLIGHT")) {
            webDriver = new EventFiringDecorator(new HighlightAspectService()).decorate(webDriver);
        }
        return webDriver;
    }
}
