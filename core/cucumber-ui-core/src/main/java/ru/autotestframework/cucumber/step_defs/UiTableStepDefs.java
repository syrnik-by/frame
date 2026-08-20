package ru.autotestframework.cucumber.step_defs;

import static ru.autotestframework.cucumber.type.CucumberTypesDefinition.TABLE_CONVERTER;

import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.type.resolvable.ResolvableDataTable;
import ru.autotestframework.cucumber.utils.TableUtil;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;

/**
 * Ui table step defs.
 */
@Slf4j
@RequiredArgsConstructor
@Description("UI Table Steps")
public class UiTableStepDefs {

    private final Context context;
    private final TablesManager tablesManager;

    /**
     * Check table.
     *
     * @param tableName the table name
     * @param dataTable the data table
     */
    @When("проверить поля таблицы {resolvable_string}:")
    @Sample("Проверить корректность заполнения таблицы")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = ":", name = "значения")
    @Example(
            example = "проверить поля таблицы 'Таблица':"
                    + "| Фамилия | Имя    | Теги       |"
                    + "| Иванов  | Петр   | тег1, тег2 |")
    public void checkTable(final String tableName, final ResolvableDataTable dataTable) {
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable.getValue(), String.class, String.class);
        tablesManager.getTable(tableName).checkTable(data, true);
    }

    /**
     * Check table row or column.
     *
     * @param tableName    the table name
     * @param tableElement the table element
     * @param key          the key
     */
    @When("проверить, что в таблице {resolvable_string} содержится {table_element} из переменной {resolvable_string}")
    @Sample("Проверить, что таблица содержит столбец или строку из контекста")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "table_element", name = "столбец или строка")
    @Parameter(type = "resolvable_string", name = "столбец или строка из контекста")
    @Example(example = "проверить, что в таблице 'Таблица' содержится столбец из переменной 'key'")
    public void checkTableRowOrColumn(final String tableName, final String tableElement, final String key) {

        tablesManager
                .getTable(tableName)
                .checkTable(
                        TableUtil.getDataTableFromPairsList(TableUtil.getCellsAsListFromContext(context.get(key))),
                        true);
    }

    /**
     * Is table empty.
     *
     * @param tableName the table name
     * @param isFulled  the is fulled
     */
    @When("проверить, что таблица {resolvable_string} {fulledTable}")
    @Sample("Проверить в таблице ячейки на пустоту")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "fulledTable", name = "наполнение таблицы")
    @Example(example = "проверить, что таблица 'Таблица' не пустая" + "проверить, что таблица 'Таблица' пустая")
    public void isTableEmpty(final String tableName, final Boolean isFulled) {
        if (isFulled ^ tablesManager.getTable(tableName).isEmpty()) {
            throw new ElementInteractionException("Table '{}' is not expected status empty", tableName);
        }
    }

    /**
     * Table columns quantity.
     *
     * @param tableName the table name
     * @param quantity  the quantity
     */
    @When("проверить, что число столбцов в таблице {resolvable_string} равно {int}")
    @Sample("Проверить что число столбцов в таблице равно заданному")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "int", name = "значение")
    @Example(example = "проверить, что число столбцов в таблице 'Таблица' равно 3")
    public void tableColumnsQuantity(final String tableName, final int quantity) {
        int columnsQuantity = tablesManager.getTable(tableName).getColumnsQuantity();
        if (columnsQuantity != quantity) {
            throw new ElementInteractionException(
                    "Table '{}' expected columns '{}' but found '{}'", tableName, quantity, columnsQuantity);
        }
    }

    /**
     * Table rows quantity.
     *
     * @param tableName the table name
     * @param quantity  the quantity
     */
    @When("проверить, что число строк в таблице {resolvable_string} равно {int}")
    @Sample("Проверить что число строк в таблице равно заданному")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "int", name = "значение")
    @Example(example = "проверить, что число строк в таблице 'Таблица' равно 3")
    public void tableRowsQuantity(final String tableName, final int quantity) {
        int rowsQuantity = tablesManager.getTable(tableName).getRowsQuantity();
        if (rowsQuantity != quantity) {
            throw new ElementInteractionException(
                    "Table '{}' expected rows '{}' but found '{}'", tableName, quantity, rowsQuantity);
        }
    }

    /**
     * Save column.
     *
     * @param tableName  the table name
     * @param columnName the column name
     * @param key        the key
     */
    @When("сохранить в таблице {resolvable_string} столбец {resolvable_string} по ключу {resolvable_string}")
    @Sample("Сохранить столбец по ключу")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "заголовок")
    @Parameter(type = "resolvable_string", name = "ключ")
    @Example(example = "сохранить в таблице 'Таблица' столбец 'Имя' по ключу 'name'")
    public void saveColumn(final String tableName, final String columnName, final String key) {
        context.set(key, tablesManager.getTable(tableName).getColumnValue(columnName));
    }

    /**
     * Save row.
     *
     * @param tableName the table name
     * @param rowNumber the row number
     * @param key       the key
     */
    @When("сохранить в таблице {resolvable_string} строку {resolvable_string} по ключу {resolvable_string}")
    @Sample("Сохранить cтроку по ключу")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "строка")
    @Parameter(type = "resolvable_string", name = "ключ")
    @Example(example = "сохранить в таблице 'Таблица' строку '5' по ключу 'row'")
    public void saveRow(final String tableName, final String rowNumber, final String key) {
        context.set(key, tablesManager.getTable(tableName).getRowValue(rowNumber));
    }

    /**
     * Save cell.
     *
     * @param tableName  the table name
     * @param rowNumber  the row number
     * @param columnName the column name
     * @param key        the key
     */
    @When("сохранить в таблице {resolvable_string} ячейку в строке {resolvable_string} и столбце {resolvable_string} "
            + "по ключу {resolvable_string}")
    @Sample("Сохранить ячейку по ключу")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "строка")
    @Parameter(type = "resolvable_string", name = "заголовок")
    @Parameter(type = "resolvable_string", name = "ключ")
    @Example(example = "сохранить в таблице 'Таблица' ячейку в строке '3' и столбце 'Фамилия' по ключу 'cell'")
    public void saveCell(final String tableName, final String rowNumber, final String columnName, final String key) {
        context.set(key, tablesManager.getTable(tableName).getCellValue(rowNumber, columnName));
    }

    /**
     * Click cell.
     *
     * @param tableName     the table name
     * @param isDoubleClick the is double click
     * @param rowNumber     the row number
     * @param columnName    the column name
     */
    @When("в таблице {resolvable_string} {clickType} по ячейке "
            + "в строке {resolvable_string} и столбце {resolvable_string}")
    @Sample("Кликнуть по ячейке в таблице")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "clickType", name = "тип клика")
    @Parameter(type = "resolvable_string", name = "строка")
    @Parameter(type = "resolvable_string", name = "столбец")
    @Example(example = "в таблице 'Таблица' кликнуть по ячейке в строке '3' и столбце 'Фамилия'")
    public void clickCell(
            final String tableName, final Boolean isDoubleClick, final String rowNumber, final String columnName) {
        SelenideElement cell = tablesManager.getTable(tableName).getCell(rowNumber, columnName);
        tablesManager.getTable(tableName).clickByClickType(cell, isDoubleClick);
    }

    /**
     * Click row.
     *
     * @param tableName the table name
     * @param rowNumber the row number
     */
    @When("выбрать в таблице {resolvable_string} строку {resolvable_string}")
    @Sample("Выбрать строку в таблице")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "строка")
    @Example(example = "выбрать в таблице 'Таблица' строку '3'")
    public void clickRow(final String tableName, final String rowNumber) {
        tablesManager
                .getTable(tableName)
                .clickByClickType(tablesManager.getTable(tableName).getFirstCellInRow(rowNumber), false);
    }

    /**
     * Click column.
     *
     * @param tableName  the table name
     * @param columnName the column name
     */
    @When("выбрать в таблице {resolvable_string} столбец {resolvable_string}")
    @Sample("Выбрать столбец в таблице")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "заголовок")
    @Example(example = "выбрать в таблице 'Таблица' столбец 'Тэг'")
    public void clickColumn(final String tableName, final String columnName) {
        tablesManager
                .getTable(tableName)
                .clickByClickType(tablesManager.getTable(tableName).getHeaderByColumn(columnName), false);
    }

    /**
     * Click all rows.
     *
     * @param tableName the table name
     */
    @When("выбрать все строки таблицы {resolvable_string}")
    @Sample("Выбрать все строки в таблице")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Example(example = "выбрать все строки таблицы 'Таблица'")
    public void clickAllRows(final String tableName) {
        tablesManager.getTable(tableName).clickAllRows();
    }

    /**
     * Not contains.
     *
     * @param tableName the table name
     * @param dataTable the data table
     */
    @When("проверить, что таблица {resolvable_string} не содержит:")
    @Sample("Проверить что таблица не содержит")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = ":", name = "значения")
    @Example(
            example = "проверить, что таблица 'Таблица' не содержит:"
                    + "| Фамилия | Имя    | Теги       |"
                    + "| Иванов  | Петр   | тег1, тег2 |")
    public void notContains(final String tableName, final ResolvableDataTable dataTable) {
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable.getValue(), String.class, String.class);
        tablesManager.getTable(tableName).checkTable(data, false);
    }

    /**
     * Click on content.
     *
     * @param tableName     the table name
     * @param isDoubleClick the is double click
     * @param columnName    the column name
     * @param dataTable     the data table
     */
    @When("в таблице {resolvable_string} {clickType} на элемент в столбце {resolvable_string} с данными:")
    @Sample("Кликнуть на элемент в строке, найденной по содержимому")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "clickType", name = "тип клика")
    @Parameter(type = "resolvable_string", name = "имя столбца")
    @Parameter(type = ":", name = "значения")
    @Example(
            example = " В таблице 'Таблица' кликнуть на элемент в столбце 'Теги' с данными:"
                    + "| Фамилия |"
                    + "| Иванов  |")
    public void clickOnContent(
            final String tableName,
            final Boolean isDoubleClick,
            final String columnName,
            final ResolvableDataTable dataTable) {
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable.getValue(), String.class, String.class);

        SelenideElement cell = tablesManager.getTable(tableName).getCellOnContent(columnName, data);
        tablesManager.getTable(tableName).clickByClickType(cell, isDoubleClick);
    }

    /**
     * Click row by content.
     *
     * @param tableName the table name
     * @param dataTable the data table
     */
    @When("в таблице {resolvable_string} выделить строку с данными:")
    @Sample("Выделить строку, найденную по содержимому")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = ":", name = "значения")
    @Example(example = "в таблице 'Таблица' выделить строку с данными:" + "| Фамилия |" + "| Иванов  |")
    public void clickRowByContent(final String tableName, final ResolvableDataTable dataTable) {
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable.getValue(), String.class, String.class);
        tablesManager.getTable(tableName).clickRowByContains(data);
    }

    /**
     * Save cell by content.
     *
     * @param tableName  the table name
     * @param columnName the column name
     * @param key        the key
     * @param dataTable  the data table
     */
    @When("из таблицы {resolvable_string} сохранить значение из столбца {resolvable_string} в переменную "
            + "{resolvable_string} из строки с данными:")
    @Sample("Сохранить значение по ключу из строки, найденной по содержимому")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "ключ")
    @Parameter(type = "resolvable_string", name = "имя столбца")
    @Parameter(type = ":", name = "значения")
    @Example(
            example =
                    "из таблицы 'Таблица' сохранить значение из столбца 'Фамилия' в переменную 'LastName' из строки с данными:"
                            + "| Фамилия |"
                            + "| Иванов  |")
    public void saveCellByContent(
            final String tableName, final String columnName, final String key, final ResolvableDataTable dataTable) {
        List<Map<String, String>> data = TABLE_CONVERTER.toMaps(dataTable.getValue(), String.class, String.class);

        context.set(
                key,
                tablesManager
                        .getTable(tableName)
                        .getCellOnContent(columnName, data)
                        .getText());
    }

    /**
     * Check column by name.
     *
     * @param tableName  the table name
     * @param columnName the column name
     */
    @When("проверить, что таблица {resolvable_string} содержит столбец {resolvable_string}")
    @Sample("Проверить наличие столбца в таблице по наименованию")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Parameter(type = "resolvable_string", name = "название столбца")
    @Example(example = "И проверить, что таблица 'Таблица' содержит столбец 'Столбец'")
    public void checkColumnByName(final String tableName, final String columnName) {
        tablesManager.getTable(tableName).checkColumnByName(columnName);
    }

    /**
     * Check doubles.
     *
     * @param tableName the table name
     */
    @When("проверить, что таблица {resolvable_string} не содержит повторений")
    @Sample("Проверить что таблица содержит только уникальные строки")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Example(example = " И проверить, что таблица 'Таблица' не содержит повторений")
    public void checkDoubles(final String tableName) {
        tablesManager.getTable(tableName).checkDoubles();
    }

    /**
     * Clean table cache.
     *
     * @param tableName the table name
     */
    @When("очистить кэш таблицы {resolvable_string}")
    @Sample("Очистить сохраненные данные о таблицах")
    @Parameter(type = "resolvable_string", name = "название таблицы")
    @Example(example = "И очистить табличный кэш 'Таблица'")
    public void cleanTableCache(final String tableName) {
        tablesManager.cleanTableCache(tableName);
    }

    /**
     * Clean table cache.
     */
    @When("очистить табличный кэш")
    @Sample("Очистить сохраненные данные о таблицах")
    @Example(example = "И очистить табличный кэш")
    public void cleanTableCache() {
        tablesManager.cleanTableCache();
    }
}
