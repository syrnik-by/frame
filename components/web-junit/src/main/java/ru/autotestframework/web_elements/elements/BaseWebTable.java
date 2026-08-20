package ru.autotestframework.web_elements.elements;

import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.Constants;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.services.table_service.FindCellsBy;
import ru.autotestframework.ui_core.services.table_service.FindHeadersBy;
import ru.autotestframework.ui_core.services.table_service.ITable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * class to extend objects to work with webTables
 */
public class BaseWebTable extends TypifiedWebElement implements ITable {
    @Setter
    @Getter
    protected String headersPath;

    @Setter
    @Getter
    protected String cellsPath;

    protected String[][] cellsValues;
    protected List<String> headers;
    protected int rows;
    protected int columns;
    protected StringBuilder errorMessage = new StringBuilder();

    public BaseWebTable(final WebElement element, final String title) {
        super(element, title);
    }

    public BaseWebTable(
            final WebElement element, final String title, final String headersPath, final String cellsPath) {
        super(element, title);
        this.headersPath = headersPath;
        this.cellsPath = cellsPath;
    }

    /**
     * sets header's and table's xpathes
     * @param tableElement
     */
    public void setTablePath(Field tableElement) {
        setHeadersPath(tableElement.getAnnotation(FindHeadersBy.class).xpath());
        setCellsPath(tableElement.getAnnotation(FindCellsBy.class).xpath());
    }

    public void init() {
        List<String> texts = new ArrayList<>();
        this.findElements(By.xpath(cellsPath)).forEach(element -> texts.add(element.getText()));
        this.findElements(By.xpath(headersPath)).forEach(element -> headers.add(element.getText()));
        columns = headers.size();
        rows = texts.size() / columns;

        cellsValues = new String[rows + 1][columns + 1];

        for (var column = 0; column < columns; column++) {
            this.putValueToTable(0, column + 1, headers.get(column));
        }
        for (var row = 0; row < rows + 1; row++) {
            this.putVoidValueToTable(row, 0, String.valueOf(row));
        }

        Iterator<String> textI = texts.iterator();
        for (var row = 0; row < rows; row++) {
            for (var column = 0; column < columns; column++) {
                this.putValueToTable(row + 1, column + 1, textI.next());
            }
        }
    }

    /**
     * clears table cache
     */
    public void clearCache() {
        cellsValues = null;
    }

    /**
     * checks if table is initialised
     * @return
     */
    public boolean isInit() {
        return cellsValues == null;
    }

    /**
     * puts value to table cell
     * @param row
     * @param column
     * @param value
     */
    public void putValueToTable(final int row, final int column, final String value) {
        cellsValues[row][column] = value;
    }

    /**
     * puts void value to table cell
     * @param row
     * @param column
     * @param value
     */
    public void putVoidValueToTable(final int row, final int column, final String value) {
        cellsValues[row][column] = value;
    }

    public void checkTable(final List<Map<String, String>> data, final boolean contains) {
        List<String> checkedColumns = getCheckedColumns(data.get(0).keySet());
        List<String> checkedRows = getCheckedRows(data);
        if (checkedRows.isEmpty()) {
            if (contains) {
                throw new ElementInteractionException("There's no such rows in table with data \n {}", data);
            } else {
                errorMessage = new StringBuilder();
                return;
            }
        }
        boolean equalStatus;
        for (var i = 0; i < checkedRows.size(); i++) {
            equalStatus = true;
            for (var j = 0; j < checkedColumns.size(); j++) {
                String expected = data.get(i).get(checkedColumns.get(j));
                String found = Objects.requireNonNull(
                        cellsValues[Integer.parseInt(checkedRows.get(i))][getHeaderIdByName(checkedColumns.get(j))]);
                if (!expected.equals(found)) {
                    if (contains) {
                        errorMessage
                                .append("ошибка в строке ")
                                .append(i + 1)
                                .append(" столбце ")
                                .append(j + 1)
                                .append(" ожидаемое значение ")
                                .append(expected)
                                .append(" полученное значение ")
                                .append(found)
                                .append("\n");
                    } else {
                        equalStatus = false;
                        break;
                    }
                }
            }
            if (!contains && equalStatus) {
                errorMessage
                        .append("Таблица содержит строку:")
                        .append("\n")
                        .append(Arrays.toString(cellsValues[Integer.parseInt(checkedRows.get(i))]))
                        .append("\n");
            }
        }
        processErrors();
    }

    /**
     * @return table' quantity of columns.
     */
    public int getColumnsQuantity() {
        return columns;
    }

    /**
     * @return table' quantity of rows.
     */
    public int getRowsQuantity() {
        return rows;
    }

    /**
     * @param rowNumber  row.
     * @param columnName column.
     * @return text value of Table with given parameters.
     */
    public String getCellValue(final String rowNumber, final String columnName) {
        return cellsValues[Integer.parseInt(rowNumber)][getHeaderIdByName(columnName)];
    }

    /**
     * returns value of table's row
     * @param rowNumber
     * @return
     */
    public String getRowValue(final String rowNumber) {
        var rowValue = new StringBuilder();
        var rowNum = Integer.parseInt(rowNumber);
        for (var column = 1; column < columns; column++) {
            rowValue.append(headers.get(column - 1))
                    .append(":")
                    .append(cellsValues[rowNum][column])
                    .append(Constants.ARRAY_STRING_DELIMETER);
        }
        return Constants.CELLS_VALUES
                + rowValue.append(headers.get(columns - 1)).append(":").append(cellsValues[rowNum][columns]);
    }

    /**
     * returns value of table's column
     * @param columnName
     * @return
     */
    public String getColumnValue(final String columnName) {
        return Constants.CELLS_VALUES
                + getColumnByName(columnName).stream()
                        .filter(pair -> !pair.getValue().equals("0"))
                        .map(pair -> columnName + ":" + pair.getKey())
                        .collect(Collectors.joining(Constants.ARRAY_STRING_DELIMETER));
    }

    public SelenideElement getCell(final String rowNumber, final String columnName) {
        throw new NotImplementedException();
    }

    public SelenideElement getCell(final int rowNumber, final int columnNumber) {
        throw new NotImplementedException();
    }

    public SelenideElement getFirstCellInRow(final String rowNumber) {
        throw new NotImplementedException();
    }

    public SelenideElement getHeaderByColumn(final String columnName) {
        throw new NotImplementedException();
    }

    public void clickAllRows() {
        throw new NotImplementedException();
    }

    /**
     * returns cell by given content
     * @param columnName
     * @param data
     * @return
     */
    public SelenideElement getCellOnContent(final String columnName, final List<Map<String, String>> data) {
        List<String> checkedRows = getCheckedRows(data);
        if (checkedRows.isEmpty()) {
            throw new ElementInteractionException("There's no such row in table with data \n {}", data);
        }
        return getCell(checkedRows.get(0), columnName);
    }

    /**
     * clicks on first cell in row which contains data
     * @param data
     */
    public void clickRowByContains(final List<Map<String, String>> data) {
        List<String> checkedRows = getCheckedRows(data);
        getFirstCellInRow(checkedRows.get(0)).scrollIntoView(true).click();
        if (checkedRows.isEmpty()) {
            throw new ElementInteractionException("There's no such row in table with data \n {}", data);
        }
        getFirstCellInRow(checkedRows.get(0)).click();
    }

    public void checkColumnByName(final String columnName) {
        if (headers.stream().noneMatch(h -> h.equals(columnName))) {
            throw new ElementInteractionException("There's no such column in table as '{}'", columnName);
        }
    }

    public boolean isEmpty() {
        for (var row = 1; row < rows + 1; row++) {
            for (var column = 1; column < columns + 1; column++) {
                if (!cellsValues[row][column].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkDoubles() {
        List<String> rowsValues = new ArrayList<>();
        for (var row = 1; row < rows + 1; row++) {
            rowsValues.add(Arrays.toString(cellsValues[row]));
        }
        rowsValues.stream().distinct().collect(Collectors.toList()).forEach(rowsValues::remove);
        Assertions.assertThat(rowsValues).isEmpty();
    }

    public void clickByClickType(final SelenideElement cell, final Boolean clickType) {
        throw new NotImplementedException();
    }

    /**
     * returns column by name
     * @param name
     * @return
     */
    public List<Pair<String, String>> getColumnByName(final String name) {
        for (var header = 0; header < columns; header++) {
            if (headers.get(header).equals(name)) {
                int finalHeader = header + 1;
                return Arrays.stream(cellsValues)
                        .map(array -> Pair.of(array[finalHeader], array[0]))
                        .collect(Collectors.toList());
            }
        }
        throw new AutotestException("Cannot find column by name : {}", name);
    }

    /**
     * processes errors in table
     */
    public void processErrors() {
        if (StringUtils.isNotEmpty(errorMessage.toString())) {
            throw new ElementInteractionException(errorMessage.toString());
        }
    }

    private List<String> getCheckedColumns(final Set<String> keys) {
        return headers.stream().filter(keys::contains).collect(Collectors.toList());
    }

    /**
     * returns rows of found subtable
     * @param data
     * @return
     */
    private List<String> getCheckedRows(final List<Map<String, String>> data) {
        List<String> collect =
                IntStream.range(1, rows + 1).mapToObj(String::valueOf).collect(Collectors.toList());
        List<String> sorted = new ArrayList<>();
        for (Map<String, String> map : data) {
            List<String> value = collect;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                try {
                    value = getRowIdsInColumnByValue(entry.getValue(), entry.getKey(), value);
                } catch (AutotestException ae) {
                    errorMessage
                            .append("Could not find column ")
                            .append(entry.getKey())
                            .append("\n");
                    break;
                }
                if (value.size() == 1) {
                    sorted.addAll(value);
                    collect.remove(value.get(0));
                    break;
                } else if (value.isEmpty()) {
                    errorMessage.append("Could not find row ").append(entry).append("\n");
                }
            }
            if (value.size() > 1) {
                errorMessage.append("Table contains ").append(value.size()).append(" identical rows\n");
            }
        }
        return sorted;
    }

    /**
     * returns row's ids in column by value
     * @param value
     * @param column
     * @param rows
     * @return
     */
    public List<String> getRowIdsInColumnByValue(final String value, final String column, final List<String> rows) {
        return getColumnByName(column).stream()
                .filter(e -> e.getKey().equals(value) && rows.contains(e.getValue()))
                .map(Pair::getValue)
                .collect(Collectors.toList());
    }

    // TODO powermockito

    /**
     * returns header's id by name
     * @param name
     * @return
     */
    public int getHeaderIdByName(final String name) {
        var id = 1;
        for (String header : headers) {
            if (header.equals(name)) {
                return id;
            }
            id++;
        }
        throw new ElementInteractionException("There's no such column in table as '{}'", name);
    }
}
