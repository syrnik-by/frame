package ru.autotestframework.tests.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.configuration.WebElementsConfig;

@Tag("@webElemElements")
class WebElementsConfigTest {

    @Test
    void configureOnStartUpTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        String enable_browser_reuse = System.getProperty("ENABLE_BROWSER_REUSE");
        Assertions.assertNull(enable_browser_reuse);
        WebElementsConfig webElementsConfig = new WebElementsConfig(webDriversProperties);
        webElementsConfig.configureOnStartUp();
        enable_browser_reuse = System.getProperty("ENABLE_BROWSER_REUSE");
        Assertions.assertEquals(enable_browser_reuse, String.valueOf(webDriversProperties.isReuseBrowserEnabled()));
    }
}
