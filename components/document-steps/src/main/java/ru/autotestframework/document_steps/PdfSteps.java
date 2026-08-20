package ru.autotestframework.document_steps;

import io.cucumber.java.en.When;
import java.io.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;

/**
 * Pdf steps.
 */
@RequiredArgsConstructor
public class PdfSteps {

    private final Context context;

    /**
     * The constant NOT_LETTERS_AND_DIGITS.
     */
    public static final String NOT_LETTERS_AND_DIGITS = "[^а-яА-ЯёЁA-z\\d\\s]";

    /**
     * Sets cell data to context.
     *
     * @param pathToExcel  the path to excel
     * @param page         the page
     * @param variableName the variable name
     */
    @When("считать текст PDF {resolvable_string} со страницы {int} в переменную {string}")
    @Sample("уставливает связь с указанным по пути листом Pdf файла для возможности считывания данных")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "Номер страницы")
    @Example(example = "считать текст PDF '${{PdfFilePath}}' со страницы 2 в переменную 'pdfText'")
    public void setCellDataToContext(String pathToExcel, int page, String variableName) {
        var text = getPDfText(pathToExcel, page);
        context.set(variableName, text);
    }

    /**
     * Returns a string containing the full text of the pages(s) of the PDF file located at the specified path
     *
     * @param pdfPath path to the PDF file
     * @return a line with the full text of the PDF file
     */
    @SneakyThrows
    private String getPDfText(String pdfPath, int pageNumberForGetText) {
        var stringBuilder = new StringBuilder();
        String pdfText;
        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            if (document.getNumberOfPages() == 0) {
                throw new AutotestException("Документ не содержит страниц");
            }
            var stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            if (-1 == pageNumberForGetText) {
                for (var page = 1; page <= document.getNumberOfPages(); ++page) {
                    stripper.setStartPage(page);
                    stripper.setEndPage(page);
                    stringBuilder.append(stripper.getText(document));
                }
            } else {
                var pageNumber = pageNumberForGetText;
                stripper.setStartPage(pageNumber);
                stripper.setEndPage(pageNumber);
                stringBuilder.append(stripper.getText(document));
            }

            pdfText = stringBuilder.toString();
            if (pdfText.replaceAll(NOT_LETTERS_AND_DIGITS, "").isEmpty()) {
                throw new AutotestException("PDF-файл не содержит текста");
            }
        } catch (IOException e) {
            throw new AutotestException("Нет файла по пути {}", e, pdfPath);
        }
        return pdfText;
    }
}
