package ru.autotestframework.core.context;

import java.nio.charset.Charset;
import java.util.HashMap;
import org.apache.commons.text.lookup.StringLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.FileLoaderImpl;

/**
 * Contextual file processing functions
 */
@Component
public class FileProcessingContextFunctionFactory implements ContextFunctionsSupplier {
    /**
     * The File loader.
     */
    @Autowired
    FileLoaderImpl fileLoader;

    @Override
    public HashMap<String, StringLookup> get() {
        var functions = new HashMap<String, StringLookup>();
        var readWithEncoding = new StringLookup() {
            @Override
            public String lookup(String args) {
                var dataParts = args.split("&");
                var filePath = dataParts[0];
                var encoding = dataParts[1];
                return fileLoader.readFileAsStringWithEncoding(filePath, Charset.forName(encoding));
            }
        };

        var createWithEncoding = new StringLookup() {
            @Override
            public String lookup(String args) {
                var dataParts = args.split("&");
                var filePath = dataParts[0];
                var encoding = dataParts[1];
                return fileLoader
                        .getFileWithEncoding(filePath, Charset.forName(encoding))
                        .getAbsolutePath();
            }
        };

        functions.put("fileResolve", createWithEncoding);
        functions.put("fileRead", readWithEncoding);

        return functions;
    }
}
