package ru.autotestframework.tests.driver_builder;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.web_elements.driver_builder.PropertiesBuilder;

@Tag("@webElemElements")
class PropertiesBuilderTest {

    @Test
    void withChromeOptionsTest() {
        PropertiesBuilder propertiesBuilder = new PropertiesBuilder();

        Properties properties = new Properties();
        properties.setProperty("chromeOptions", "--start-maximized;--incognito;--headless");
        properties.setProperty("framework.ui.browser.pageLoadStrategy", "eager");
        properties.setProperty("browser.path", "PATH");
        propertiesBuilder.configuration.setProperties(properties);
        ReflectionTestUtils.setField(propertiesBuilder, "headless", null);
        propertiesBuilder.withChromeOptions();
        ChromeOptions chromeOptions = propertiesBuilder.configuration.getChromeOptions();
        Assertions.assertEquals(PageLoadStrategy.EAGER, chromeOptions.asMap().get("pageLoadStrategy"));
        Map capabilities = (Map) chromeOptions.getCapability("goog:chromeOptions");
        Assertions.assertEquals("PATH", capabilities.get("binary"));
        List args = (List) capabilities.get("args");
        Assertions.assertTrue(args.contains("--start-maximized"));
        Assertions.assertTrue(args.contains("--incognito"));
        Assertions.assertTrue(args.contains("--headless"));
    }

    @Test
    void withChromeOptionsTest2() {
        PropertiesBuilder propertiesBuilder = new PropertiesBuilder();
        Properties properties = new Properties();
        properties.setProperty("headless", "true");
        propertiesBuilder.configuration.setProperties(properties);
        propertiesBuilder.withChromeOptions();
        List args =
                (List) ((Map) propertiesBuilder.configuration.getChromeOptions().getCapability("goog:chromeOptions"))
                        .get("args");
        Assertions.assertTrue(args.contains("--headless"));
    }
}
