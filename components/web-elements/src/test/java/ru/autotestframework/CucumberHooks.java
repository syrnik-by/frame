package ru.autotestframework;

import com.codeborne.selenide.Configuration;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openqa.selenium.chrome.ChromeOptions;

@RequiredArgsConstructor
public class CucumberHooks {

    @SneakyThrows
    @Before(order = Integer.MIN_VALUE)
    public void setupSelenide() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments("--disable-gpu")
                .addArguments("--start-maximized")
                .addArguments("--disable-blink-features")
                .addArguments("--disable-blink-features=AutomationControlled")
                .addArguments("--use-gl=egl")
                .addArguments("--disable-dev-shm-usage");
        chromeOptions.setAcceptInsecureCerts(true);
        Configuration.browserCapabilities = chromeOptions.merge(Configuration.browserCapabilities);
        Configuration.holdBrowserOpen = false;
        // change driver provider
        // Configuration.browser = PlaywrightDefaultDriverProvider.class.getName();
    }
}
