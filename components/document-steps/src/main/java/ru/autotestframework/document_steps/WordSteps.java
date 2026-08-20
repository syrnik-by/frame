package ru.autotestframework.document_steps;

import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.document_steps.document_service.WordService;

/**
 * Word steps.
 */
@Slf4j
@RequiredArgsConstructor
public class WordSteps {

    private final Context context;

    /**
     * Sets word file text to context.
     *
     * @param filePath     the file path
     * @param variableName the variable name
     */
    @When("записать текст документа Word из пути {resolvable_string} в переменную {resolvable_string}")
    @Sample("записывает текст документа в указанную переменную")
    @Parameter(type = "resolvable_string", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "название переменной")
    @Example(example = "И записать текст документа Word из пути '${{WordFilePath}}' в переменную 'Текст документа'")
    public void setWordFileTextToContext(String filePath, String variableName) {
        var word = new WordService(filePath);
        var wordText = word.readWordDocumentText();
        log.info("Текст документа ", wordText);
        context.set(variableName, wordText);
    }
}
