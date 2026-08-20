package ru.autotestframework.ui_core.tests.page_manager_positive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.configuration.UiProperties;

/**
 * Spring context test.
 */
class SpringContextTest {
    @Autowired
    private UiProperties properties;

    @Autowired
    private PageManager pageManager;

    @Autowired
    private WebDriver driver;

    /**
     * Test properties.
     */
    @Disabled("актуализация")
    @Test
    void testProperties() {
        assertEquals(30, properties.getTimeout());
        assertEquals(2, properties.getPagePackage().length);
    }

    /**
     * Test page manager.
     */
    @Disabled("актуализация")
    @Test
    void testPageManager() {
        assertEquals(3, pageManager.getRegisteredPageClasses().size());
    }
}
