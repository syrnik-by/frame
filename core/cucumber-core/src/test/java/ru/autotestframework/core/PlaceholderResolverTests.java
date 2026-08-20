package ru.autotestframework.core;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.DataTableTypeRegistryTableConverter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.configuration.PlaceholderResolverConfig;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextFunctionFactory;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.cucumber.CucumberPlaceholderResolverImpl;
import ru.autotestframework.util.generator.FakerRU;

/**
 * Placeholder resolver tests.
 */
@Tag("@BackendCore")
class PlaceholderResolverTests {

    private Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
    private static final String RESULT = "fromContext";
    private final FileLoader fileLoader =
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
    private final ContextFunctionsSupplier funcSupplier =
            new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
    private final StringSubstitutor stringSubstitutor =
            new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
    private CucumberPlaceholderResolverImpl placeholderResolver =
            new CucumberPlaceholderResolverImpl(stringSubstitutor);

    /**
     * String resolver test.
     */
    @Test
    void stringResolverTest() {
        context.set("test", RESULT);
        context.set("test1", 1, "sadas", "1312", "asd", Integer.valueOf(1));
        Assertions.assertEquals(RESULT, placeholderResolver.resolve("${{test}}"));
    }

    /**
     * Map resolver test.
     */
    @Test
    void mapResolverTest() {
        context.set("test", RESULT);
        Map<String, String> testMap = Map.of("test", "${{test}}", "${{test}}", "test");
        Map<String, String> resolvedMap = placeholderResolver.resolve(testMap);
        Assertions.assertEquals(RESULT, resolvedMap.get("test"));
        Assertions.assertEquals("test", resolvedMap.get(RESULT));
    }

    /**
     * Data table resolver test.
     */
    @Test
    void dataTableResolverTest() {
        context.set("test", RESULT);
        DataTable dataTable = DataTable.create(List.of(List.of("test", "${{test}}")));
        DataTable resolvedDataTable = placeholderResolver.resolve(dataTable);
        Assertions.assertEquals(
                new DataTableTypeRegistryTableConverter(new DataTableTypeRegistry(Locale.ENGLISH))
                        .toLists(resolvedDataTable, String.class)
                        .get(0)
                        .get(1),
                RESULT);
    }

    /**
     * Faker ru resolver test.
     */
    @Test
    void fakerRuResolverTest() {
        var pattern = "Address.streetName";
        var generatedFaker = FakerRU.generate(pattern);

        Assertions.assertNotEquals(pattern, generatedFaker);
        Assertions.assertNotNull(generatedFaker);
        Assertions.assertEquals(15, FakerRU.instance().ogrnip().generate().length());
        Assertions.assertEquals(15, FakerRU.generate("ogrnip").length());
        Assertions.assertEquals(12, FakerRU.generate("innfl").length());
        Assertions.assertEquals(10, FakerRU.generate("innul").length());
        Assertions.assertEquals(13, FakerRU.generate("ogrn").length());
        Assertions.assertEquals(9, FakerRU.generate("kpp").length());
        Assertions.assertEquals(11, FakerRU.generate("snils").length());
    }

    /**
     * Faker check distinct generation test.
     */
    @Test
    void fakerCheckDistinctGenerationTest() {
        String result1 = FakerRU.generate("Address.streetName");
        String result2 = FakerRU.generate("Address.streetName");
        Assertions.assertNotEquals(result1, result2);
    }

    /**
     * Faker missed key test.
     */
    @Test
    void fakerMissedKeyTest() {
        org.assertj.core.api.Assertions.assertThatCode(() -> FakerRU.generate("Address.noname"))
                .hasMessageStartingWith("Cannot find key");
    }

    /**
     * Null var string resolver test.
     */
    @Test
    void nullVarStringResolverTest() {
        context.set("nullVar", null);
        Assertions.assertNull(placeholderResolver.resolve("${{nullVar}}"));
    }

    /**
     * Null var concat exception test.
     */
    @Test
    void nullVarConcatExceptionTest() {
        context.set("nullVar", null);
        Assertions.assertEquals("123null", placeholderResolver.resolve("123${{nullVar}}"));
        Assertions.assertEquals("123null456", placeholderResolver.resolve("123${{nullVar}}456"));
        Assertions.assertEquals(
                "123nullaaanullbbb111", placeholderResolver.resolve("123${{nullVar}}aaa${{nullVar}}bbb111"));
        Assertions.assertEquals("null123aaanullbbb", placeholderResolver.resolve("${{nullVar}}123aaa${{nullVar}}bbb"));
    }

    /**
     * Inner function resolve test.
     */
    @Test
    void innerFunctionResolveTest() {
        Assertions.assertFalse(placeholderResolver
                .resolve("${{escape:${{fakerRu:Name.first_name}}}}")
                .contains("{{"));
    }

    /**
     * Masked function resolve test.
     */
    @Test
    void maskedFunctionResolveTest() {
        context.set("test", RESULT);
        String input = "${{secret:${{test}}}}";

        String result = placeholderResolver.resolve(input);
        Assertions.assertEquals(RESULT, result);
    }

    /**
     * Masked function un resolve test.
     */
    @Test
    void maskedFunctionUnResolveTest() {
        context.set("test", RESULT);
        String input = "${{secret:${{test}}}}";
        String result = placeholderResolver.resolve(input, true);
        Assertions.assertEquals(ContextFunctionFactory.SECRET_DUMMY, result);
    }
}
