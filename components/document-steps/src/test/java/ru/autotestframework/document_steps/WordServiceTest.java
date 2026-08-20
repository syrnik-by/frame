package ru.autotestframework.document_steps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.document_steps.document_service.WordService;

/**
 * Word service test.
 */
@Tag("@DocumentSteps")
class WordServiceTest {

    /**
     * Read word document text positive test.
     */
    @Test
    void readWordDocumentTextPositiveTest() {
        WordService wordService = new WordService("src/test/resources/data/files/Test doc.docx");
        String documentText = wordService.readWordDocumentText();
        Assertions.assertEquals("Тестовый документ\n", documentText);
    }

    /**
     * Read word document text negative test.
     */
    @Test
    void readWordDocumentTextNegativeTest() {
        Assertions.assertThrows(
                RuntimeException.class, () -> new WordService("src/test/доукмент.docx").readWordDocumentText());
    }
}
