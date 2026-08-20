package ru.autotestframework.document_steps;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.*;
import ru.autotestframework.document_steps.document_service.ExcelService;

/**
 * Excel service test.
 */
@Tag("@DocumentSteps")
class ExcelServiceTest {

    /**
     * The Excel service.
     */
    static ExcelService excelService;

    /**
     * Sets up.
     *
     * @throws IOException the io exception
     */
    @BeforeAll
    static void setUp() throws IOException {
        excelService = new ExcelService("src/test/resources/data/files/Test table.xlsx");
        excelService.setSheet("Лист2");
    }

    /**
     * Sets sheet.
     */
    @AfterEach
    void setSheet() {
        excelService.setSheet("Лист2");
    }

    /**
     * Sets sheet positive test.
     */
    @Test
    void setSheetPositiveTest() {
        Assertions.assertEquals("Лист2", excelService.getSheet().getSheetName());
    }

    /**
     * Sets sheet negative test.
     */
    @Test
    void setSheetNegativeTest() {
        Assertions.assertThrows(NullPointerException.class, () -> excelService.setSheet("Лист3"));
    }

    /**
     * Write string or date value positive string test.
     */
    @Test
    void writeStringOrDateValuePositiveStringTest() {
        excelService.writeStringOrDateValue(5, 5, "test");
        String cell = excelService.readStringCell(5, 5);
        Assertions.assertEquals("test", cell);
    }

    /**
     * Write string or date value positive date test.
     */
    @Test
    void writeStringOrDateValuePositiveDateTest() {
        String dateString = "22.11.1963";
        excelService.writeStringOrDateValue(5, 5, dateString);
        String date = new SimpleDateFormat("dd.MM.yyyy").format(excelService.readDateCell(5, 5));
        Assertions.assertEquals(dateString, date);
    }

    /**
     * Write integer value positive test.
     */
    @Test
    void writeIntegerValuePositiveTest() {
        excelService.writeIntegerValue(7, 7, 123);
        int cell = excelService.readNumberCell(7, 7);
        Assertions.assertEquals(123, cell);
    }

    /**
     * Write double value positive test.
     */
    @Test
    void writeDoubleValuePositiveTest() {
        excelService.writeDoubleValue(8, 8, 321);
        Double cell = excelService.readDoubleCell(8, 8);
        Assertions.assertEquals(321, cell);
    }

    /**
     * Read quantity of entries.
     */
    @Test
    void readQuantityOfEntries() {
        int quantity = excelService.readQuantityOfEntries();
        Assertions.assertEquals(2, quantity);
    }

    /**
     * Find target row in column using value test.
     */
    @Test
    void findTargetRowInColumnUsingValueTest() {
        int row = excelService.findTargetRowInColumnUsingValue("55", 1);
        Assertions.assertEquals(1, row);
    }

    /**
     * Gets all list name test.
     */
    @Test
    void getAllListNameTest() {
        List<String> list = List.of("Лист1", "Лист2");
        List<String> allListName = excelService.getAllListName();
        Assertions.assertEquals(list, allListName);
    }

    /**
     * Read all cell in sheet test.
     */
    @Test
    void readAllCellInSheetTest() {
        excelService.setSheet("Лист1");
        List<Double> list = List.of(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d);
        List<Double> doubleList = excelService.readAllCellInSheet().stream()
                .map(Cell::getNumericCellValue)
                .collect(Collectors.toList());
        Assertions.assertEquals(list, doubleList);
    }

    /**
     * Read all cell current column test.
     */
    @Test
    void readAllCellCurrentColumnTest() {
        excelService.setSheet("Лист1");
        List<Double> list = List.of(2d, 5d, 8d);
        List<Double> doubleList = excelService.readAllCellCurrentColumn(1).stream()
                .map(Cell::getNumericCellValue)
                .collect(Collectors.toList());
        Assertions.assertEquals(list, doubleList);
    }

    /**
     * Read all cell current row test.
     */
    @Test
    void readAllCellCurrentRowTest() {
        excelService.setSheet("Лист1");
        List<Double> list = List.of(4d, 5d, 6d);
        List<Double> doubleList = excelService.readAllCellCurrentRow(1).stream()
                .map(Cell::getNumericCellValue)
                .collect(Collectors.toList());
        Assertions.assertEquals(list, doubleList);
    }
}
