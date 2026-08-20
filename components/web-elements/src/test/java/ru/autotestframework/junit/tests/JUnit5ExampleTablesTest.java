package ru.autotestframework.junit.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.pages.local.DynamicTablePageForUnits;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;

// TODO move to seed (cause testIT)

@Tag("@webElemJunit")
@Execution(ExecutionMode.CONCURRENT)
class JUnit5ExampleTablesTest extends JUnitUIBaseExampleTest {

    @Autowired
    PageManager pageManager;

    @Autowired
    FrameworkDefaultVariables defaultVariables;

    @Autowired
    DriverContainer driverContainer;

    @Autowired
    TablesManager tablesManager;

    @Test
    void exampleTable() throws InterruptedException {

        var pageUrl = defaultVariables.getVariables().get("url.tablePage");
        Selenide.open(pageUrl);
        pageManager.getPageByTitle(new DynamicTablePageForUnits().getTitle());

        var table = tablesManager.getTable("Table");
        step1();

        Assertions.assertEquals(20, Integer.parseInt(table.getCellValue("1", "age")));
        table.checkDoubles();
        Assertions.assertFalse(table.isEmpty());
        Assertions.assertEquals(2, table.getRowsQuantity());
    }

    @Test
    void exampleTableWithoutManagers() throws InterruptedException {
        var pageUrl = defaultVariables.getVariables().get("url.tablePage");
        Selenide.open(pageUrl);

        var page = new DynamicTablePageForUnits();

        page.table.init();
        page.table.getCell(1, 1);

        Assertions.assertEquals(2, page.table.getRowsQuantity());
    }

    @Step
    public void step1() {}
}
