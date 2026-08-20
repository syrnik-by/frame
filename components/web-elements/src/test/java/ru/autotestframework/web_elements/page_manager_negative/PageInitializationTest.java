package ru.autotestframework.web_elements.page_manager_negative;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.Constants;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;
import ru.autotestframework.web_elements.elements.TextInput;

@Tag("@webElemElements")
class PageInitializationTest {
    final TestPageWithAcceptor pageWithAcceptor = new TestPageWithAcceptor();
    final TestPageWithDuplicatedNames doubles = new TestPageWithDuplicatedNames();

    private final WebDriver mockWebDriver = Mockito.mock(WebDriver.class);
    private final DriverContainerImpl driverContainer = new DriverContainerImplBoot() {
        @Override
        public Driver getActiveDriver() {
            return super.getActiveDriver();
        }
    };
    private final TablesManager tablesManager = new TablesManager(new UiProperties());

    UiProperties pops = new UiProperties();

    {
        pops.setPagePackage(new String[] {Constants.DEFAULT_GLUE + ".web_elements.page_manager_negative"});
    }

    private final PageManager pageManager = new PageManager(driverContainer, tablesManager, pops);
    private final Driver webDriver = new DriverWeb("TestDriver", null);

    @BeforeEach
    void before() {
        webDriver.setWebDriver(mockWebDriver);
        driverContainer.add(webDriver);
    }

    @Test
    void checkAcceptorNegative() {
        String title = pageWithAcceptor.getTitle();
        assertThrows(InitializationException.class, () -> pageManager.getPageByTitle(title));
    }

    @Test
    void checkDoublesNegative() {
        String title = doubles.getTitle();
        ReflectionTestUtils.setField(pageManager, "needThrowOnDoubles", true);
        assertThrows(ConfigurationException.class, () -> pageManager.getPageByTitle(title));
    }

    @Test
    void checkDoublesPositive() {
        String title = doubles.getTitle();
        ReflectionTestUtils.setField(pageManager, "needThrowOnDoubles", false);
        assertDoesNotThrow(() -> pageManager.getPageByTitle(title));
    }

    @PageEntry(title = "Страница c кастомным ожиданием")
    public static class TestPageWithAcceptor extends AbstractPage {

        @Override
        public void checkAcceptor() {
            throw new InitializationException("Any Exception");
        }
    }

    @PageEntry(title = "Страница c дубликатами имени Элемента")
    public static class TestPageWithDuplicatedNames extends AbstractPage {

        @Element("Логин")
        public TextInput username;

        @Element("Логин")
        public TextInput username2;
    }
}
