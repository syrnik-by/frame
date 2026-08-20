package ru.autotestframework.ui_core.tests.page_manager;

import com.codeborne.selenide.WebDriverRunner;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.Page;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;
import ru.autotestframework.ui_core.tests.PageManagerTestNegativePage;

/**
 * Page manager test.
 */
@Tag("@UiCore")
class PageManagerTest {
    /**
     * The Pops.
     */
    static UiProperties pops = new UiProperties();

    private static PageManager pageManager;

    /**
     * Sets .
     */
    @BeforeAll
    public static void setup() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        TablesManager tablesManager = Mockito.mock(TablesManager.class);
        pops.setPagePackage(new String[] {"ru.autotestframework.ui_core.tests.page_manager"});
        pageManager = new PageManager(driverContainer, tablesManager, pops);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        WebDriverRunner.setWebDriver(webDriver);
    }

    /**
     * Sets current by page test.
     */
    @Test
    void setCurrentByPageTest() {
        pageManager.setCurrent(new PageManagerTestPage());
        Page current = (Page) ReflectionTestUtils.getField(pageManager, "current");
        Assertions.assertTrue(current instanceof PageManagerTestPage);
    }

    /**
     * Sets current by page class test.
     */
    @Test
    void setCurrentByPageClassTest() {
        pageManager.setCurrent(PageManagerTestPage.class);
        Page current = (Page) ReflectionTestUtils.getField(pageManager, "current");
        Assertions.assertTrue(current instanceof PageManagerTestPage);
    }

    /**
     * Gets current not null test.
     */
    @Test
    void getCurrentNotNullTest() {
        PageManagerTestPage pageManagerTestPage = new PageManagerTestPage();
        ReflectionTestUtils.setField(pageManager, "current", pageManagerTestPage);
        Assertions.assertEquals(pageManager.getCurrent(), pageManagerTestPage);
    }

    /**
     * Gets extended page element test.
     */
    @Test
    void getExtendedPageElementTest() {
        PageManagerTestExtendPage pageManagerTestPage = new PageManagerTestExtendPage();
        ReflectionTestUtils.setField(pageManager, "current", pageManagerTestPage);
        Assertions.assertEquals(pageManager.getCurrent(), pageManagerTestPage);
    }

    /**
     * Gets current null test.
     */
    @Test
    void getCurrentNullTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        TablesManager tablesManager = Mockito.mock(TablesManager.class);
        PageManager pageManagerNull = new PageManager(driverContainer, tablesManager, pops);
        Assertions.assertThrows(InitializationException.class, pageManagerNull::getCurrent);
    }

    /**
     * Gets registered page classes test.
     */
    @Test
    void getRegisteredPageClassesTest() {
        Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
        registeredPageClasses.put("PageManagerTestPage", PageManagerTestPage.class);
        ReflectionTestUtils.setField(pageManager, "registeredPageClasses", registeredPageClasses);
        Map<String, Class<? extends Page>> pageManagerRegisteredPageClasses = pageManager.getRegisteredPageClasses();
        Assertions.assertEquals(registeredPageClasses, pageManagerRegisteredPageClasses);
    }

    /**
     * Gets page by title contains test.
     */
    @Test
    void getPageByTitleContainsTest() {
        Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
        registeredPageClasses.put("PageManagerTestPage", PageManagerTestPage.class);
        ReflectionTestUtils.setField(pageManager, "registeredPageClasses", registeredPageClasses);
        Page pageManagerTestPage = pageManager.getPageByTitle("PageManagerTestPage");
        Assertions.assertTrue(pageManagerTestPage instanceof PageManagerTestPage);
    }

    /**
     * Gets page by title not contains test.
     */
    @Test
    void getPageByTitleNotContainsTest() {
        Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
        registeredPageClasses.put("PageManagerTestPage", PageManagerTestPage.class);
        ReflectionTestUtils.setField(pageManager, "registeredPageClasses", registeredPageClasses);
        Assertions.assertThrows(InitializationException.class, () -> pageManager.getPageByTitle("wrong"));
    }

    /**
     * Gets page by class contains test.
     */
    @Test
    void getPageByClassContainsTest() {
        Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
        registeredPageClasses.put("PageManagerTestPage", PageManagerTestPage.class);
        ReflectionTestUtils.setField(pageManager, "registeredPageClasses", registeredPageClasses);
        Page pageManagerTestPage = pageManager.getPageByClass(PageManagerTestPage.class);
        Assertions.assertNotNull(pageManagerTestPage);
    }

    /**
     * Gets page by class not contains test.
     */
    @Test
    void getPageByClassNotContainsTest() {
        Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
        registeredPageClasses.put("PageManagerTestPage", PageManagerTestPage.class);
        ReflectionTestUtils.setField(pageManager, "registeredPageClasses", registeredPageClasses);
        Assertions.assertThrows(
                InitializationException.class, () -> pageManager.getPageByClass(PageManagerTestNegativePage.class));
    }
}
