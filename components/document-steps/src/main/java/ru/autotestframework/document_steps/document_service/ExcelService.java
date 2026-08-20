package ru.autotestframework.document_steps.document_service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel service.
 */
public class ExcelService {

    private final String path;
    private Workbook workbook;
    private Sheet currentSheet;
    private final String FORMAT_PATTERN_DATE = "dd.MM.yyyy";

    /**
     * Gets workbook.
     *
     * @return the workbook
     */
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * Gets sheet.
     *
     * @return the sheet
     */
    public Sheet getSheet() {
        return currentSheet;
    }

    /**
     * Checks if the file exists, if so, writes information to it, if not, creates a new one.
     *
     * @param path the path to the file
     * @throws IOException the io exception
     */
    public ExcelService(String path) throws IOException {
        var file = new File(path);
        this.path = path;

        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(path)) {
                if (path.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                } else if (path.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    throw new RuntimeException("Невозможно сопоставить формат файла из пути " + path);
                }
            }
        } else {
            if (path.endsWith(".xls")) {
                workbook = new HSSFWorkbook();
            } else if (path.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook();
            } else {
                throw new RuntimeException("Невозможно сопоставить формат из пути " + path);
            }
        }
    }

    /**
     * We install the sheet we need
     *
     * @param name sheet name
     */
    public void setSheet(String name) {
        var sheet = workbook.getSheet(name);
        if (sheet == null) throw new NullPointerException("Листа не существует, необходимо создать лист");
        currentSheet = sheet;
    }

    /**
     * Write the String value and the date
     *
     * @param row    row number
     * @param column column number
     * @param value  write value is in String format
     */
    @SneakyThrows
    public void writeStringOrDateValue(int row, int column, String value) {
        var cell = createRowAndCell(row, column);
        if (value.matches("\\d\\d.\\d\\d.\\d{4}")) {
            var date = new SimpleDateFormat(FORMAT_PATTERN_DATE).parse(value);
            var format = cell.getSheet().getWorkbook().createDataFormat();
            var dateStyle = cell.getSheet().getWorkbook().createCellStyle();
            dateStyle.setDataFormat(format.getFormat(FORMAT_PATTERN_DATE));
            cell.setCellStyle(dateStyle);
            cell.setCellValue(date);
        } else {
            cell.setCellValue(value);
        }
    }

    private Cell createRowAndCell(int row, int column) {
        if (currentSheet.getRow(row) == null) currentSheet.createRow(row);
        if (currentSheet.getRow(row).getCell(column) == null) {
            currentSheet.getRow(row).createCell(column);
        }
        return currentSheet.getRow(row).getCell(column);
    }

    /**
     * Write the int value
     *
     * @param row    row number
     * @param column column number
     * @param value  write value is in int value
     */
    public void writeIntegerValue(int row, int column, int value) {
        var cell = createRowAndCell(row, column);
        cell.setCellValue(value);
        cell.getCellType();
    }

    /**
     * Write the double value
     *
     * @param row    row number
     * @param column column number
     * @param value  write value is in double format
     */
    public void writeDoubleValue(int row, int column, double value) {
        var cell = createRowAndCell(row, column);
        cell.setCellValue(value);
        cell.getCellType();
    }

    /**
     * Closes the document stored in memory and writes it to disk
     *
     * @param wb       book
     * @param fileName file name
     */
    private static void writeWorkbook(Workbook wb, String fileName) throws IOException {
        try (var fileOut = new FileOutputStream(fileName)) {
            wb.write(fileOut);
        }
    }

    /**
     * Changes the column size
     *
     * @param column column number
     */
    public void autoSize(int column) {
        currentSheet.autoSizeColumn(column);
    }

    /**
     * Write to Excel
     */
    public void finishAndWrite() {
        try {
            writeWorkbook(workbook, path);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        workbook = null;
    }

    /**
     * Reading a cell String
     *
     * @param row    row number
     * @param column column number
     * @return String returns the value of a cell with text content
     */
    public String readStringCell(int row, int column) {
        var rows = currentSheet.getRow(row);
        return rows.getCell(column).getStringCellValue();
    }

    /**
     * Reading a cell int
     *
     * @param row    row number
     * @param column column number
     * @return int returns the value of a cell with numeric contents (leaves only the integer part of the number)
     */
    public int readNumberCell(int row, int column) {
        return (int) readDoubleCell(row, column);
    }

    /**
     * Reading a cell double
     *
     * @param row    row number
     * @param column column number
     * @return Double returns the value of a cell with numeric contents (without cutting off the fractional part)
     */
    public double readDoubleCell(int row, int column) {
        var rows = currentSheet.getRow(row);
        return rows.getCell(column).getNumericCellValue();
    }

    /**
     * Reading a cell date
     *
     * @param row    row number
     * @param column column number
     * @return Date returns the value of the date-time cell
     */
    public Date readDateCell(int row, int column) {
        return currentSheet.getRow(row).getCell(column).getDateCellValue();
    }

    /**
     * Read quantity of entries int.
     *
     * @return number of records in the file
     */
    public int readQuantityOfEntries() {
        return currentSheet.getLastRowNum();
    }

    /**
     * Reading a numeric cell in a worksheet, returns the value of the cell at the intersection of row and column in String format
     *
     * @param cell    a cell with all attributes
     * @param numbers the number of digits after the decimal point
     * @return String returns the value of a cell with numeric contents and the specified number of digits after the decimal point
     */
    public String readNumericValueFromCell(Cell cell, int numbers) {
        var value = "";
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    var sdf = new SimpleDateFormat(FORMAT_PATTERN_DATE);
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = BigDecimal.valueOf(cell.getNumericCellValue())
                            .setScale(numbers, RoundingMode.FLOOR)
                            .toPlainString();
                }
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * Reading a numeric cell in a worksheet, returns the value of the cell at the intersection of row and column in String format
     *
     * @param row row number
     * @param column column number
     * @param numbers the number of digits after the decimal point
     * @return String returns the value of a numeric cell by converting it to text
     */
    public String readNumericCellInCurrentSheet(int row, int column, int numbers) {
        var cell = currentSheet.getRow(row).getCell(column, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return readNumericValueFromCell(cell, numbers);
    }

    /**
     * Reading any cell in the worksheet, returns the value of the cell at the intersection of row and column in String format
     *
     * @param row row number
     * @param column column number
     * @return String returns the value of any cell by converting it to text
     */
    public String readAllFormatCellInCurrentSheet(int row, int column) {
        var cell = currentSheet.getRow(row).getCell(column, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return readAllValueFromCell(cell);
    }

    /**
     * Reading any cell in the worksheet, returns the value of the cell at the intersection of row and column in String format
     *
     * @param cell a cell with all attributes
     * @return String
     */
    private String readAllValueFromCell(Cell cell) {
        var value = "";
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    var sdf = new SimpleDateFormat(FORMAT_PATTERN_DATE);
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = BigDecimal.valueOf(cell.getNumericCellValue())
                            .stripTrailingZeros()
                            .toPlainString();
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * Find target row in column using value int.
     *
     * @param value the value in the cell
     * @param column column number int row = -1 - in Excel, everything is read from the index starting from 0, so to work with a row in the table, and not with the column name, you need to do -1
     * @return the int
     */
    public int findTargetRowInColumnUsingValue(String value, int column) {
        List<Cell> cellsInColumn = readAllCellCurrentColumn(column);
        int row = -1;
        for (Cell cell : cellsInColumn) {
            if (value.equals(readAllValueFromCell(cell).trim())) {
                row = cell.getRowIndex();
                break;
            }
        }
        return row;
    }

    /**
     * Retrieves a list of sheets in a workbook in String format
     *
     * @return List all list name
     */
    public List<String> getAllListName() {
        List<String> listName = new ArrayList<>();
        for (var i = 0; i < workbook.getNumberOfSheets(); i++) {
            listName.add(workbook.getSheetName(i));
        }
        return listName;
    }

    /**
     * Returns all cells in the current sheet, returns a List in Cell format
     *
     * @return List list
     */
    public List<Cell> readAllCellInSheet() {
        List<Cell> listCell = new ArrayList<>();
        Iterator<Row> ri = currentSheet.rowIterator();
        while (ri.hasNext()) {
            XSSFRow row = (XSSFRow) ri.next();
            Iterator<Cell> ci = row.cellIterator();
            while (ci.hasNext()) {
                XSSFCell cell = (XSSFCell) ci.next();
                listCell.add(cell);
            }
        }
        return listCell;
    }

    /**
     * Returns all non-empty cells in the specified column
     *
     * @param column column number
     * @return List of cells
     */
    public List<Cell> readAllCellCurrentColumn(int column) {
        List<Cell> listCell = new ArrayList<>();
        Iterator<Row> ri = currentSheet.rowIterator();
        while (ri.hasNext()) {
            XSSFRow row = (XSSFRow) ri.next();
            XSSFCell cell = row.getCell(column);
            if (cell != null) {
                listCell.add(cell);
            }
        }
        return listCell;
    }

    /**
     * Returns a list containing the values of all non-empty cells of the selected column and the next column
     *
     * @param columnIndex - column number
     * @return the values of all non-empty cells of the selected column and the column name
     */
    public Map<String, List<String>> readAllCellsValuesFromColumn(int columnIndex) {
        Map<String, List<String>> columnWithHeader = new HashMap<>();
        List<String> listCell = new ArrayList<>();
        List<Cell> cellsInColumn = readAllCellCurrentColumn(columnIndex);
        for (var i = 1; i < cellsInColumn.size(); i++) {
            listCell.add(readAllValueFromCell(cellsInColumn.get(i)));
        }
        columnWithHeader.put(readAllValueFromCell(cellsInColumn.get(0)), listCell);
        return columnWithHeader;
    }

    /**
     * Returns a list containing the values of all non-empty cells in the selected row
     *
     * @param rowIndex row number
     * @return the values of all non-empty cells in the selected row
     */
    public List<String> readAllCellsValuesFromRow(int rowIndex) {
        List<Cell> cellsInRow = readAllCellCurrentRow(rowIndex);
        return cellsInRow.stream()
                .map(this::readAllValueFromCell)
                .filter(cell -> !cell.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Returns all values in the specified string in Cell format
     *
     * @param row row number
     * @return list of cells
     */
    public List<Cell> readAllCellCurrentRow(int row) {
        List<Cell> listCell = new ArrayList<>();
        var rows = currentSheet.getRow(row);
        Iterator<Cell> cellIterator = rows.cellIterator();
        while (cellIterator.hasNext()) {
            XSSFCell cell = (XSSFCell) cellIterator.next();
            listCell.add(cell);
        }
        return listCell;
    }
}
