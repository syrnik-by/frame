package ru.autotestframework.context_functions_supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.autotestframework.cucumber.type.CucumberTypesDefinition.TABLE_CONVERTER;

import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.cucumber.utils.TableUtil;

/**
 * Table utils tests.
 */
class TableUtilsTests {

    /**
     * Test get list from context positive.
     */
    @Disabled("актуализация")
    @Test
    void testGetListFromContextPositive() {
        System.setProperty("framework.array.string.delimiter", "; ");
        List<Pair> fromContext = List.of(Pair.of("Name", "1"), Pair.of("Age", "2"), Pair.of("Number", "3"));
        assertEquals(fromContext, TableUtil.getCellsAsListFromContext("{cells}Name:1; Age:2; Number:3"));
    }

    /**
     * Test get list from context negative.
     */
    @Test
    void testGetListFromContextNegative() {
        System.setProperty("framework.array.string.delimiter", "; ");
        Assert.assertThrows(AutotestException.class, () -> TableUtil.getCellsAsListFromContext("1 2 3"));
    }

    /**
     * Test data table by row.
     */
    @Test
    void testDataTableByRow() {
        List<Pair> fromContext = List.of(Pair.of("Name", "1"), Pair.of("Age", "2"), Pair.of("Number", "3"));
        DataTable dataTable = DataTable.create(List.of(List.of("Name", "Age", "Number"), List.of("1", "2", "3")));
        List<Map<Object, Object>> mapList = TABLE_CONVERTER.toMaps(dataTable.transpose(), String.class, String.class);
        assertEquals(TableUtil.getDataTableFromPairsList(fromContext), mapList);
    }

    /**
     * Test data table by column.
     */
    @Test
    void testDataTableByColumn() {
        List<Pair> fromContext = List.of(Pair.of("Name", "1"), Pair.of("Name", "2"), Pair.of("Name", "3"));
        DataTable dataTable = DataTable.create(List.of(List.of("Name"), List.of("1"), List.of("2"), List.of("3")));
        List<Map<Object, Object>> mapList = TABLE_CONVERTER.toMaps(dataTable.transpose(), String.class, String.class);
        assertEquals(TableUtil.getDataTableFromPairsList(fromContext), mapList);
    }
}
