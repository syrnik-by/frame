package ru.autotestframework.document_steps.document_service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Word service.
 */
public class WordService {
    private final XWPFDocument document;

    /**
     * Gets document.
     *
     * @return the document
     */
    public XWPFDocument getDocument() {
        return document;
    }

    /**
     * Instantiates a new Word service.
     *
     * @param path the path
     */
    public WordService(String path) {
        var file = new File(path);
        document = createDocument(file);
    }

    /**
     * Read word document text string.
     *
     * @return the string
     */
    public String readWordDocumentText() {
        var xwpfWordExtractor = new XWPFWordExtractor(document);
        return xwpfWordExtractor.getText();
    }

    private XWPFDocument createDocument(File file) {
        XWPFDocument document;
        try (InputStream is = Files.newInputStream(Paths.get(String.valueOf(file)))) {
            document = new XWPFDocument(is);
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось создать документ", ex);
        }
        return document;
    }
}
