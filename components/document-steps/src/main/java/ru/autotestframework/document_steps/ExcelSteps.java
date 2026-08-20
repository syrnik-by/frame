package ru.autotestframework.document_steps;

import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.document_steps.document_service.ExcelService;

/**
 * Excel steps.
 */
@Slf4j
@RequiredArgsConstructor
public class ExcelSteps {

    private final Context context;
    private ExcelService excel;

    /**
     * Sets cell data to context.
     *
     * @param pathToExcel  the path to excel
     * @param listName     the list name
     * @param excelRow     the excel row
     * @param excelColumn  the excel column
     * @param variableName the variable name
     */
    @When(
            "считать значение Excel {resolvable_string} листа {resolvable_string} в строке {int} и колонке {int} и записать в переменную {resolvable_string}")
    @Sample("уставливает связь с указанным по пути листом Excel файла для возможности считывания данных")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "название листа в Excel файле")
    @Parameter(type = "int", name = "номер строки")
    @Parameter(type = "int", name = "номер колонки")
    @Parameter(type = "resolvable_string", name = "название переменной/ключа")
    @Example(
            example =
                    "И считать значение Excel '${{ExcelFilePath}}' листа 'Кредиты' в строке 1 и колонке 1 и записать в переменную 'ExcelDate'")
    public void setCellDataToContext(
            String pathToExcel, String listName, int excelRow, int excelColumn, String variableName) {
        readExcelWithList(pathToExcel, listName);
        var date = excel.readAllFormatCellInCurrentSheet(excelRow, excelColumn - 1);
        context.set(variableName, date);
        log.info("Значение ячейки " + date);
    }

    /**
     * Sets number data to context.
     *
     * @param excelRow     the excel row
     * @param excelColumn  the excel column
     * @param numbers      the numbers
     * @param pathToExcel  the path to excel
     * @param listName     the list name
     * @param variableName the variable name
     */
    @When(
            "записать числовое значение Excel ячейки в строке {int} и колонке {int} с цифрами после запятой {int} из пути {resolvable_string} листа {resolvable_string}  и записать в переменную {resolvable_string}")
    @Sample("уставливает связь с указанным по пути листом Excel файла для возможности считывания данных")
    @Parameter(type = "int", name = "номер строки")
    @Parameter(type = "int", name = "номер колонки")
    @Parameter(type = "int", name = "количество цифр после запятой")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "название листа в Excel файле")
    @Parameter(type = "resolvable_string", name = "название переменной/ключа")
    @Example(
            example =
                    "И записать числовое значение Excel ячейки в строке 1 и колонке 10 с цифрами после запятой 2 из пути '${{ExcelTasksFilePath}}' листа 'Worksheet'  и записать в переменную 'ExcelTasksSumCell'")
    public void setNumberDataToContext(
            int excelRow, int excelColumn, int numbers, String pathToExcel, String listName, String variableName) {
        readExcelWithList(pathToExcel, listName);
        var date = excel.readNumericCellInCurrentSheet(excelRow, excelColumn - 1, numbers);
        context.set(variableName, date);
        log.info("Значение ячейки " + date);
    }

    /**
     * Sets quantity of entries to context.
     *
     * @param pathToExcel  the path to excel
     * @param listName     the list name
     * @param variableName the variable name
     */
    @When(
            "записать количество записей файла из пути {resolvable_string} листа {resolvable_string} в/по переменную/ключу {resolvable_string}")
    @Sample("записать количество записей в переменную из указанного файла")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "название листа в Excel файле")
    @Parameter(type = "resolvable_string", name = "название переменной/ключа")
    @Example(
            example =
                    "И записать количество записей файла из пути '${{ExcelTasksFilePath}}' листа 'Worksheet' в переменную 'QuantityOfEntriesExcel'")
    public void setQuantityOfEntriesToContext(String pathToExcel, String listName, String variableName) {
        readExcelWithList(pathToExcel, listName);
        var numberRows = excel.readQuantityOfEntries();
        context.set(variableName, numberRows);
        log.info("Количество записей " + numberRows);
    }

    /**
     * Check list sorted.
     *
     * @param pathToExcel the path to excel
     * @param listName    the list name
     * @param columnIndex the column index
     * @param sortOrder   the sort order
     */
    @SneakyThrows
    @When(
            "проверить что в файле по пути {resolvable_string} листа {resolvable_string} столбец под номером {int} отсортирован по {resolvable_string}")
    @Sample("Проверить заданное направление сортировки")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "название листа в Excel файле")
    @Parameter(type = "int", name = "номер столбца")
    @Parameter(type = "resolvable_string", name = "направление сортировки по возрастанию/убыванию")
    @Example(
            example =
                    "И проверить что в файле по пути '${{ExcelTasksFilePath}}' листа 'Worksheet' столбец под номером 1 отсортирован по 'возрастанию'")
    public void checkListSorted(String pathToExcel, String listName, Integer columnIndex, String sortOrder) {
        readExcelWithList(pathToExcel, listName);
        final String header = excel.readAllCellsValuesFromColumn(columnIndex).keySet().stream()
                .findFirst()
                .orElseThrow();
        final List<String> cells =
                excel.readAllCellsValuesFromColumn(columnIndex).get(header);
        boolean ascending = true, descending = true;
        for (var i = 1; i < cells.size() && (ascending || descending); i++) {
            final var nextValue = Long.parseLong(cells.get(i));
            final var previousValue = Long.parseLong(cells.get(i - 1));
            ascending = ascending && nextValue >= previousValue;
            descending = descending && nextValue <= previousValue;
        }
        Assert.assertTrue(
                String.format("Направление сортировки неверное! столбец: %s,%n содержимое списка: %s", header, cells),
                sortOrder.equals("возрастанию") ? ascending : descending);
    }

    private void readExcelWithList(String pathToExcel, String listName) {
        try {
            excel = new ExcelService(pathToExcel);
        } catch (IOException e) {
            throw new AutotestException("Отсуствует файл по пути '{}':", e, pathToExcel);
        }
        excel.setSheet(listName);
        log.info("Имя листа " + listName);
    }
}
