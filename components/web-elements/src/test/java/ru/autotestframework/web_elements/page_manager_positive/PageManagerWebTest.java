package ru.autotestframework.web_elements.page_manager_positive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.Constants;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;

@Tag("@webElemElements")
class PageManagerWebTest {
    private final WebDriver mockWebDriver = Mockito.mock(WebDriver.class);
    private final DriverContainerImpl driverContainer = new DriverContainerImplBoot() {};
    private final TablesManager tablesManager = new TablesManager(new UiProperties());

    UiProperties props = new UiProperties();

    {
        props.setPagePackage(new String[] {Constants.DEFAULT_GLUE + ".web_elements.page_manager_positive"});
    }

    private final PageManager pageManager = new PageManager(driverContainer, tablesManager, props);
    final TestPage2 page = new TestPage2();
    private final Driver webDriver = new DriverWeb("TestDriver", null);

    @BeforeEach
    void before() {
        webDriver.setWebDriver(mockWebDriver);
        driverContainer.add(webDriver);
    }

    @Test
    void setCurrentByPageObject() {
        Assertions.assertEquals(
                pageManager.getPageByClass(TestPage2.class).getTitle(),
                pageManager.setCurrent(page).getTitle());
    }

    @Test
    void getCurrent() {
        assertThrows(InitializationException.class, pageManager::getCurrent);
        pageManager.setCurrent(page);
        Assertions.assertDoesNotThrow(pageManager::getCurrent);
    }

    @Test
    void getPageCollection() {
        assertEquals(2, pageManager.getRegisteredPageClasses().size());
    }

    @Test
    void getPageByTitle() {
        Assertions.assertEquals(
                page.getTitle(), pageManager.getPageByTitle(page.getTitle()).getTitle());
    }

    @Test
    void getPageByClass() {
        Assertions.assertEquals(
                TestPage1.class, pageManager.getPageByClass(TestPage1.class).getClass());
    }

    @PageEntry(title = "Страница первая")
    public static class TestPage1 extends AbstractPage {}

    @PageEntry(title = "Страница вторая")
    public static class TestPage2 extends AbstractPage {}
}
