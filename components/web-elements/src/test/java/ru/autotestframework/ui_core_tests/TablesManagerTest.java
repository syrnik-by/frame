package ru.autotestframework.ui_core_tests;

import com.codeborne.selenide.WebDriverRunner;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.pages.local.DynamicTablePageForUnits;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.page_manager.ElementFactory;
import ru.autotestframework.ui_core.services.table_service.ITable;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;
import ru.autotestframework.web_elements.elements.WebTable;

@Tag("@webElemElements")
class TablesManagerTest {

    @Test
    void getTableCacheTrueTest() {
        UiProperties uiProperties = new UiProperties();
        uiProperties.setTableCacheEnabled(true);
        TablesManager tablesManager = new TablesManager(uiProperties);
        Map<String, ITable> allPageTables = new HashMap<>();
        WebTable iTable = Mockito.mock(WebTable.class);
        allPageTables.put("iTable", iTable);
        ReflectionTestUtils.setField(tablesManager, "allPageTables", allPageTables);
        Assertions.assertEquals(iTable, tablesManager.getTable("iTable"));
    }

    @Test
    void getTableCacheFalseTest() {
        UiProperties uiProperties = new UiProperties();
        uiProperties.setTableCacheEnabled(false);
        TablesManager tablesManager = new TablesManager(uiProperties);
        Map<String, ITable> allPageTables = new HashMap<>();
        WebTable webTable = Mockito.mock(WebTable.class);
        allPageTables.put("table", webTable);
        ReflectionTestUtils.setField(tablesManager, "allPageTables", allPageTables);
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        ReflectionTestUtils.setField(tablesManager, "page", dynamicTablePage);
        Mockito.when(webTable.getTitle()).thenReturn("table");
        Mockito.doNothing().when(webTable).init();
        Assertions.assertEquals(webTable, tablesManager.getTable("table"));
    }

    @Test
    void setCurrentPageTablesTest() {
        UiProperties uiProperties = new UiProperties();
        uiProperties.setTableCacheEnabled(false);
        TablesManager tablesManager = new TablesManager(uiProperties);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        WebDriverRunner.setWebDriver(webDriver);
        DynamicTablePageForUnits dynamicTablePage =
                ElementFactory.initElements(Mockito.mock(DriverContainerImpl.class), DynamicTablePageForUnits.class);
        ITable iTable = Mockito.mock(ITable.class);
        Map<String, ITable> allPageTables = new HashMap<>();
        allPageTables.put("iTable", iTable);
        ReflectionTestUtils.setField(tablesManager, "allPageTables", allPageTables);
        tablesManager.setCurrentPageTables(dynamicTablePage);
        Map setCurrentPageTables = (Map) ReflectionTestUtils.getField(tablesManager, "allPageTables");
        Assertions.assertNotNull(setCurrentPageTables);
        Assertions.assertFalse(setCurrentPageTables.containsKey("iTable"));
        Assertions.assertTrue(setCurrentPageTables.containsKey("Table"));
    }

    @Test
    void cleanTableCacheTest() {
        UiProperties uiProperties = new UiProperties();
        uiProperties.setTableCacheEnabled(false);
        TablesManager tablesManager = new TablesManager(uiProperties);
        WebTable iTable = Mockito.mock(WebTable.class);
        Map<String, ITable> allPageTables = new HashMap<>();
        allPageTables.put("iTable", iTable);
        ReflectionTestUtils.setField(tablesManager, "allPageTables", allPageTables);
        Map pageTables = (Map) ReflectionTestUtils.getField(tablesManager, "allPageTables");
        Assertions.assertNull(((ITable) pageTables.get("iTable")).getCell(1, 1));

        tablesManager.cleanTableCache("iTable");

        Assertions.assertNotNull(pageTables);
        Assertions.assertEquals(0, ((ITable) pageTables.get("iTable")).getRowsQuantity());
        Assertions.assertEquals(0, ((ITable) pageTables.get("iTable")).getColumnsQuantity());
        Assertions.assertNull(((ITable) pageTables.get("iTable")).getCell(1, 1));
        Assertions.assertNull(((ITable) pageTables.get("iTable")).getCell(0, 0));
        Mockito.verify(iTable, Mockito.times(1)).clearCache();
    }
}
