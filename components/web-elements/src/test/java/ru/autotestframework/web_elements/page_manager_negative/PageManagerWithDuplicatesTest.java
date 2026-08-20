package ru.autotestframework.web_elements.page_manager_negative;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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

@Tag("@webElemElements")
class PageManagerWithDuplicatesTest {

    @Test
    void duplicateName() {
        DriverContainerImpl driverContainer = new DriverContainerImplBoot() {
            @Override
            public Driver getActiveDriver() {
                return super.getActiveDriver();
            }
        };
        UiProperties props = new UiProperties();
        {
            props.setPagePackage(new String[] {Constants.DEFAULT_GLUE + ".web_elements"});
        }
        assertThrows(
                InitializationException.class, () -> new PageManager(driverContainer, new TablesManager(props), props));
    }

    @PageEntry(title = "Страница первая")
    public static class TestPage3 extends AbstractPage {}

    @PageEntry(title = "Страница четвертая")
    public static class TestPage1 extends AbstractPage {}
}
