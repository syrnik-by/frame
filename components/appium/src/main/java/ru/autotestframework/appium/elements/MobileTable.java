package ru.autotestframework.appium.elements;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

public class MobileTable extends BaseMobileTable {

    protected ElementsCollection elements;
    protected List<SelenideElement> headersElements;

    public MobileTable(final WebElement element, final String title) {
        super(element, title);
    }

    public MobileTable(final WebElement element, final String title, final String headersPath, final String cellsPath) {
        super(element, title);
        this.headersPath = headersPath;
        this.cellsPath = cellsPath;
    }

    /**
     * Initialisation table data
     */
    @Override
    public void init() {
        ElementsCollection cellsCollection = $(this).$$x(cellsPath);
        List<String> texts = cellsCollection.texts();

        long startLoadingTime = System.currentTimeMillis();
        while (cellsCollection.size() != texts.size() && (System.currentTimeMillis() - startLoadingTime < 10000)) {

            cellsCollection = $(this).$$x(cellsPath);
            texts = cellsCollection.texts();
        }

        headersElements = $(this).$$x(headersPath);
        headers = $(this).$$x(headersPath).texts();
        columns = headers.size();
        rows = cellsCollection.size() / columns;

        cellsValues = new String[rows + 1][columns + 1];

        for (var column = 0; column < columns; column++) {
            this.putValueToTable(0, column + 1, headersElements.get(column).text());
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
        elements = cellsCollection;
    }

    /**
     * @param rowNumber  row.
     * @param columnName column.
     * @return text value of Table with given parameters.
     */
    @Override
    public String getCellValue(final String rowNumber, final String columnName) {
        return getCell(rowNumber, columnName).getText();
    }

    /**
     * returns cell as an element
     * @param rowNumber
     * @param columnName
     * @return
     */
    @Override
    public SelenideElement getCell(final String rowNumber, final String columnName) {
        return getCell(Integer.parseInt(rowNumber), getHeaderIdByName(columnName));
    }

    /**
     * returns cell as an element
     * @param rowNumber
     * @param columnNumber
     * @return
     */
    @Override
    public SelenideElement getCell(final int rowNumber, final int columnNumber) {
        return elements.get((rowNumber - 1) * columns + columnNumber - 1);
    }

    /**
     * returns first cell in given row
     * @param rowNumber
     * @return
     */
    @Override
    public SelenideElement getFirstCellInRow(final String rowNumber) {
        return getCell(Integer.parseInt(rowNumber), 1);
    }

    /**
     * @param columnName column name on UI interface of application.
     * @return header Selenide element on given name.
     */
    @Override
    public SelenideElement getHeaderByColumn(final String columnName) {
        return headersElements.stream()
                .filter(x -> x.getText().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new ElementInteractionException("No such column in table '{}'", this.getTitle()));
    }

    /**
     * clicks on first element in each row
     */
    @Override
    public void clickAllRows() {
        for (var row = 0; row < rows; row++) {
            elements.get(row * columns).click();
        }
    }

    /**
     * clicks on cell with given click type
     * @param cell
     * @param clickType
     */
    @Override
    public void clickByClickType(final SelenideElement cell, final Boolean clickType) {
        if (Boolean.TRUE.equals(clickType)) {
            cell.doubleClick();
        } else {
            cell.click();
        }
    }
}
