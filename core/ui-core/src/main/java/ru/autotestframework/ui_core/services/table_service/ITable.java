package ru.autotestframework.ui_core.services.table_service;

import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * Table.
 */
public interface ITable {

    /**
     * Sets table path.
     *
     * @param tableElement the table element
     */
    void setTablePath(Field tableElement);

    /**
     * Initialisation table (cache data, load text etc. from Browser, if needed)
     */
    void init();

    /**
     * Clear table cache (memory, that used to increase capacity to not making multiple heavy requests to browser)
     */
    void clearCache();

    /**
     * check if is any cached data stored
     * @return the boolean
     */
    boolean isInit();

    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Put value to table.
     *
     * @param row    the row
     * @param column the column
     * @param value  the value
     */
    void putValueToTable(final int row, final int column, final String value);

    /**
     * Put void value to table.
     *
     * @param row    the row
     * @param column the column
     * @param value  the value
     */
    void putVoidValueToTable(final int row, final int column, final String value);

    /**
     * Check if table validates against given condition.
     *
     * @param dataTable data to contain
     * @param contains  data should be contained or absent.
     * @throws ElementInteractionException on wrong result.
     */
    void checkTable(final List<Map<String, String>> dataTable, final boolean contains);

    /**
     * Gets columns quantity.
     *
     * @return the columns quantity
     */
    int getColumnsQuantity();

    /**
     * Gets rows quantity.
     *
     * @return the rows quantity
     */
    int getRowsQuantity();

    /**
     * Gets cell value.
     *
     * @param rowNumber  the row number
     * @param columnName the column name
     * @return the cell value
     */
    String getCellValue(final String rowNumber, final String columnName);

    /**
     * Gets row value.
     *
     * @param rowNumber the row number
     * @return the row value
     */
    String getRowValue(final String rowNumber);

    /**
     * Gets column value.
     *
     * @param columnName the column name
     * @return the column value
     */
    String getColumnValue(final String columnName);

    /**
     * Gets cell.
     *
     * @param rowNumber  the row number
     * @param columnName the column name
     * @return the cell
     */
    SelenideElement getCell(final String rowNumber, final String columnName);

    /**
     * Gets cell.
     *
     * @param rowNumber    the row number
     * @param columnNumber the column number
     * @return the cell
     */
    SelenideElement getCell(final int rowNumber, final int columnNumber);

    /**
     * Gets first cell in row.
     *
     * @param rowNumber the row number
     * @return the first cell in row
     */
    SelenideElement getFirstCellInRow(final String rowNumber);

    /**
     * Gets header by column.
     *
     * @param columnName column name on UI interface of application.
     * @return header Selenide element on given name.
     */
    SelenideElement getHeaderByColumn(final String columnName);

    /**
     * e.g. - select them
     */
    void clickAllRows();

    /**
     * Gets cell on content.
     *
     * @param columnName - columnName of needed row' Cell
     * @param dataTable  - data with row info (pair of columnNames and its value to filter)
     * @return Selenide element of concrete cell
     */
    SelenideElement getCellOnContent(final String columnName, final List<Map<String, String>> dataTable);

    /**
     * Click row by contains.
     *
     * @param dataTable the data table
     */
    void clickRowByContains(final List<Map<String, String>> dataTable);

    /**
     * Check column by name.
     *
     * @param columnName name of column of Table of UI Application.
     * @throws ElementInteractionException if no column with given name.
     */
    void checkColumnByName(final String columnName);

    /**
     * Is empty boolean.
     *
     * @return is table contains nonempty values or not.
     */
    boolean isEmpty();

    /**
     * Check if table has rows with doubled values.
     *
     * @throws AssertionError on having.
     */
    void checkDoubles();

    /**
     * Click by click type.
     *
     * @param cell      the cell
     * @param clickType the click type
     */
    void clickByClickType(final SelenideElement cell, final Boolean clickType);
}
