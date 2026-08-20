package ru.autotestframework.web_elements.elements;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.web_elements.services.JSCollectionEvaluator;

public class WebTable extends BaseWebTable {

    protected ElementsCollection elements;
    protected List<SelenideElement> headersElements;

    public WebTable(final WebElement element, final String title) {
        super(element, title);
    }

    public WebTable(final WebElement element, final String title, final String headersPath, final String cellsPath) {
        super(element, title);
        this.headersPath = headersPath;
        this.cellsPath = cellsPath;
    }

    /**
     * Initialisation table data
     */
    public void init() {
        elements = $(this).$$x(cellsPath);

        List<String> texts = getTexts(elements);

        long startLoadingTime = System.currentTimeMillis();
        while ((elements.size() != texts.size() || texts.isEmpty())
                && (System.currentTimeMillis() - startLoadingTime < 10000)) {
            elements = $(this).$$x(cellsPath);
            texts = getTexts(elements);
        }

        headersElements = $(this).$$x(headersPath);
        headers = $(this).$$x(headersPath).texts();
        columns = headers.size();
        rows = elements.size() / columns;

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
    }

    private List<String> getTexts(ElementsCollection cellsCollection) {
        String rootXpath = Arrays.stream(getAnnotations())
                .filter(x -> x instanceof FindBy)
                .findFirst()
                .map(x -> ((FindBy) x).xpath())
                .orElse("");

        List<String> texts;
        long startLoadingTime = System.currentTimeMillis();

        if (!rootXpath.isEmpty()) {
            texts = JSCollectionEvaluator.texts(rootXpath + "/" + cellsPath);
        } else {
            texts = cellsCollection.texts();
            while (cellsCollection.size() != texts.size() && (System.currentTimeMillis() - startLoadingTime < 10000)) {
                texts = JSCollectionEvaluator.texts(cellsPath);
            }
        }

        List<String> clearTexts = new ArrayList<>();
        for (String text : texts) {
            clearTexts.add(text.replaceAll("\u00A0", " ").replaceAll("&nbsp;", " "));
        }

        return clearTexts;
    }

    /**
     * @param rowNumber  row.
     * @param columnName column.
     * @return text value of Table with given parameters.
     */
    public String getCellValue(final String rowNumber, final String columnName) {
        return getCell(rowNumber, columnName).getText();
    }

    /**
     * returns cell as an element
     * @param rowNumber
     * @param columnName
     * @return
     */
    public SelenideElement getCell(final String rowNumber, final String columnName) {
        return getCell(Integer.parseInt(rowNumber), getHeaderIdByName(columnName));
    }

    /**
     * returns cell as an element
     * @param rowNumber
     * @param columnNumber
     * @return
     */
    public SelenideElement getCell(final int rowNumber, final int columnNumber) {
        return elements.get((rowNumber - 1) * columns + columnNumber - 1);
    }

    /**
     * returns first cell in given row
     * @param rowNumber
     * @return
     */
    public SelenideElement getFirstCellInRow(final String rowNumber) {
        return getCell(Integer.parseInt(rowNumber), 1);
    }

    /**
     * @param columnName column name on UI interface of application.
     * @return header Selenide element on given name.
     */
    public SelenideElement getHeaderByColumn(final String columnName) {
        return headersElements.stream()
                .filter(x -> x.getText().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new ElementInteractionException("No such column in table '{}'", this.getTitle()));
    }

    /**
     * clicks on first element in each row
     */
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
    public void clickByClickType(final SelenideElement cell, final Boolean clickType) {
        if (Boolean.TRUE.equals(clickType)) {
            cell.doubleClick();
        } else {
            cell.click();
        }
    }
}
