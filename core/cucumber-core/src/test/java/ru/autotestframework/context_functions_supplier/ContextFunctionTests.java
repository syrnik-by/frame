package ru.autotestframework.context_functions_supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.configuration.PlaceholderResolverConfig;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.ContextFunctionsSupplier;

/**
 * Context function tests.
 */
@Tag("@BackendCore")
class ContextFunctionTests {

    private final FileLoader fileLoader =
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
    private final ContextFunctionsSupplier funcSupplier =
            new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);

    /**
     * Test context function extract text from file.
     */
    @Test
    void testContextFunctionExtractTextFromFile() {
        var actualDateString = funcSupplier.get().get("file").lookup("data/demo/file1.txt");
        assertEquals("success", actualDateString);
    }
}
