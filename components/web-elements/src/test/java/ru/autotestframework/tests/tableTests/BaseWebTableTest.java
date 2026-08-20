package ru.autotestframework.tests.tableTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.autotestframework.cucumber.type.CucumberTypesDefinition.TABLE_CONVERTER;

import io.cucumber.datatable.DataTable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.web_elements.elements.BaseWebTable;

@Tag("@webElemElements")
class BaseWebTableTest {

    private static BaseWebTable table;
    private static BaseWebTable mockTable;
    private static BaseWebTable spyTable;

    private static final String NAME = "Name";
    private static final String NAME1 = NAME + "1";
    private static final String NAME2 = NAME + "2";
    private static final String AGE = "Age";
    private static final String AGE1 = AGE + "1";
    private static final String AGE2 = AGE + "2";

    @BeforeAll
    public static void initTable() {
        initTableForTest();
        setTestData();
    }

    @AfterEach
    public void afterEach() {
        initTableForTest();
        setTestData();
    }

    @Test
    void checkTableTestContainsTrue() {
        List<String> row0 = Arrays.asList(NAME, AGE);
        List<String> row1 = Arrays.asList(NAME1, AGE1);
        List<String> row2 = Arrays.asList(NAME2, AGE2);
        List<List<String>> tableList = Arrays.asList(row0, row1, row2);
        DataTable dataTable = DataTable.create(tableList);
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable, String.class, String.class);

        doCallRealMethod().when(spyTable).checkTable(data, true);
        spyTable.checkTable(data, true);
    }

    @Test
    void checkTableTestContainsFalse() {
        List<String> row0 = Arrays.asList(NAME, AGE);
        List<String> row1 = Arrays.asList(NAME1, AGE2);
        List<List<String>> tableList = Arrays.asList(row0, row1);
        DataTable dataTable = DataTable.create(tableList);
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable, String.class, String.class);
        doCallRealMethod().when(table).checkTable(data, false);
        table.checkTable(data, false);
    }

    @Test
    void checkTableTestThrowContainsFalse() {
        List<String> row0 = Arrays.asList(NAME, AGE);
        List<String> row1 = Arrays.asList(NAME1, AGE1);
        List<String> row2 = Arrays.asList(NAME2, AGE2);
        List<List<String>> tableList = Arrays.asList(row0, row1, row2);
        DataTable dataTable = DataTable.create(tableList);
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable, String.class, String.class);

        doCallRealMethod().when(spyTable).checkTable(data, false);
        Assert.assertThrows(ElementInteractionException.class, () -> spyTable.checkTable(data, false));
        ReflectionTestUtils.setField(spyTable, "errorMessage", new StringBuilder());
    }

    @Test
    void checkTableTestThrowContainsTrue() {
        List<String> row0 = Arrays.asList(NAME, AGE);
        List<String> row1 = Arrays.asList(NAME1, AGE2);
        List<String> row2 = Arrays.asList(NAME2, AGE2);
        List<List<String>> tableList = Arrays.asList(row0, row1, row2);
        DataTable dataTable = DataTable.create(tableList);
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable, String.class, String.class);

        doCallRealMethod().when(table).checkTable(data, true);
        Assert.assertThrows(ElementInteractionException.class, () -> table.checkTable(data, true));
        ReflectionTestUtils.setField(table, "errorMessage", new StringBuilder());
    }

    @Test
    void getColumnsQuantityTest() {
        doCallRealMethod().when(table).getColumnsQuantity();
        assertEquals(2, table.getColumnsQuantity());
    }

    @Test
    void getRowsQuantityTest() {
        doCallRealMethod().when(table).getRowsQuantity();
        assertEquals(2, table.getRowsQuantity());
    }

    @Test
    void isEmptyTest() {
        doCallRealMethod().when(table).isEmpty();
        Assertions.assertFalse(table.isEmpty());
    }

    @Disabled("актуализация")
    @Test
    void getColumnValueTest() {
        doCallRealMethod().when(spyTable).getColumnValue(NAME);
        assertEquals("{cells}Name:Name1; Name:Name2", spyTable.getColumnValue("Name"));
    }

    @Disabled("актуализация")
    @Test
    void getRowValueTest() {
        doCallRealMethod().when(table).getRowValue("1");
        assertEquals("{cells}Name:Name1; Age:Age1", spyTable.getRowValue("1"));
    }

    @Test
    void checkDoublesPositiveTest() {
        doCallRealMethod().when(table).checkDoubles();
        table.checkDoubles();
    }

    @Test
    void checkDoublesNegativeTest() {
        BaseWebTable table = spy(mockTable);
        String[][] cellsValues = {{NAME, AGE}, {NAME1, AGE1}, {NAME1, AGE1}};
        ReflectionTestUtils.setField(table, "headers", Arrays.asList(NAME, AGE));
        ReflectionTestUtils.setField(table, "columns", 2);
        ReflectionTestUtils.setField(table, "rows", 2);
        ReflectionTestUtils.setField(table, "errorMessage", new StringBuilder());
        ReflectionTestUtils.setField(table, "cellsValues", cellsValues);
        doCallRealMethod().when(table).checkDoubles();
        Assert.assertThrows(AssertionError.class, table::checkDoubles);
    }

    private static void initTableForTest() {
        table = mock(BaseWebTable.class);
        mockTable = new BaseWebTable(mock(WebElement.class), "testTable");
        spyTable = spy(mockTable);
    }

    private static void setTestData() {
        String[][] cellsValues = {{"0", NAME, AGE}, {"1", NAME1, AGE1}, {"2", NAME2, AGE2}};
        ReflectionTestUtils.setField(table, "headers", Arrays.asList(NAME, AGE));
        ReflectionTestUtils.setField(table, "columns", 2);
        ReflectionTestUtils.setField(table, "rows", 2);
        ReflectionTestUtils.setField(table, "errorMessage", new StringBuilder());
        ReflectionTestUtils.setField(table, "cellsValues", cellsValues);

        ReflectionTestUtils.setField(spyTable, "headers", Arrays.asList(NAME, AGE));
        ReflectionTestUtils.setField(spyTable, "columns", 2);
        ReflectionTestUtils.setField(spyTable, "rows", 2);
        ReflectionTestUtils.setField(spyTable, "errorMessage", new StringBuilder());
        ReflectionTestUtils.setField(spyTable, "cellsValues", cellsValues);
        System.setProperty("framework.array.string.delimiter", "; ");
    }
}
