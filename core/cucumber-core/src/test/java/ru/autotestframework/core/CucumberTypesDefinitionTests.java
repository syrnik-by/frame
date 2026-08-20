package ru.autotestframework.core;

import io.cucumber.datatable.DataTable;
import java.util.List;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.configuration.PlaceholderResolverConfig;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.CucumberPlaceholderResolverImpl;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.type.CucumberTypesDefinition;
import ru.autotestframework.cucumber.type.HttpBodyValidator;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableDataTable;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.cucumber.type.resolvable.ResolvableString;

public class CucumberTypesDefinitionTests {

    @Test
    void resolvable_string1Test() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String resolvableString = cucumberTypesDefinition.resolvable_string("test");
        Assertions.assertEquals("test", resolvableString);
    }

    @Test
    void resolvable_string2Test() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String resolvableString = cucumberTypesDefinition.resolvable_string("\"test\"");
        Assertions.assertEquals("test", resolvableString);
    }

    @Test
    void resolvable_string3Test() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String resolvableString = cucumberTypesDefinition.resolvable_string("'test'");
        Assertions.assertEquals("test", resolvableString);
    }

    @Test
    void resolvable_string4Test() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String resolvableString = cucumberTypesDefinition.resolvable_string("\"test'");
        Assertions.assertEquals("\"test'", resolvableString);
    }

    @Test
    void objectTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        context.set("object", "fromObject");
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        String fromContext = (String) cucumberTypesDefinition.object("object");
        Assertions.assertEquals("fromObject", fromContext);
    }

    @Test
    void visibilityPositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean visibility = cucumberTypesDefinition.visibility("отображается");
        Assertions.assertTrue(visibility);
    }

    @Test
    void visibilityNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean visibility = cucumberTypesDefinition.visibility("не отображается");
        Assertions.assertFalse(visibility);
    }

    @Test
    void fulledTablePositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean fulledTable = cucumberTypesDefinition.fulledTable("пустая");
        Assertions.assertTrue(fulledTable);
    }

    @Test
    void fulledTableNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean fulledTable = cucumberTypesDefinition.fulledTable("не пустая");
        Assertions.assertFalse(fulledTable);
    }

    @Test
    void activityPositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean activity = cucumberTypesDefinition.activity("активен");
        Assertions.assertTrue(activity);
    }

    @Test
    void activityNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean activity = cucumberTypesDefinition.activity("не активен");
        Assertions.assertFalse(activity);
    }

    @Test
    void editablePositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean editable = cucumberTypesDefinition.editable("доступен");
        Assertions.assertTrue(editable);
    }

    @Test
    void editableNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean editable = cucumberTypesDefinition.editable("не доступен");
        Assertions.assertFalse(editable);
    }

    @Test
    void clickTypePositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean clickType = cucumberTypesDefinition.clickType("дважды кликнуть");
        Assertions.assertTrue(clickType);
    }

    @Test
    void clickTypeNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Boolean clickType = cucumberTypesDefinition.clickType("кликнуть");
        Assertions.assertFalse(clickType);
    }

    @Test
    void storagesTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        context.set("localStorage", "localStorage");
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        String storages = cucumberTypesDefinition.storages("${{localStorage}}");
        Assertions.assertEquals("localStorage", storages);
    }

    @Test
    void pathTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        String path = cucumberTypesDefinition.path("\\\\\"test\\\\'var");
        Assertions.assertEquals("\\\"test\\'var", path);
    }

    @Test
    void popup_actionTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String popupAction = cucumberTypesDefinition.popup_action("test");
        Assertions.assertEquals("test", popupAction);
    }

    @Test
    void matcherTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        MatcherName contains = cucumberTypesDefinition.matcher("contains");
        Assertions.assertEquals(MatcherName.CONTAINS_STRING, contains);
    }

    @Test
    void scrollDirectionTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String scrollDirection = cucumberTypesDefinition.scrollDirection("test");
        Assertions.assertEquals("test", scrollDirection);
    }

    @Test
    void pageDirectionTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String pageDirection = cucumberTypesDefinition.pageDirection("test");
        Assertions.assertEquals("test", pageDirection);
    }

    @Test
    void tableElementTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String tableElement = cucumberTypesDefinition.tableElement("test");
        Assertions.assertEquals("test", tableElement);
    }

    @Test
    void queueTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        String queue = cucumberTypesDefinition.queue("test");
        Assertions.assertEquals("test", queue);
    }

    @Test
    void parseResolvableStringListPositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        ResolvableString resolvableString = cucumberTypesDefinition.parseResolvableString(List.of("test"));
        Assertions.assertEquals("test", resolvableString.toString());
    }

    @Test
    void parseResolvableStringListNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Assertions.assertThrows(
                AutotestException.class, () -> cucumberTypesDefinition.parseResolvableString(List.of("1", "2")));
    }

    @Test
    void parseResolvableStringTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        ResolvableString resolvableString = cucumberTypesDefinition.parseResolvableString("unresolvable");
        Assertions.assertInstanceOf(ResolvableString.class, resolvableString);
    }

    @Test
    void parseResolvableDataTableTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        ResolvableDataTable resolvableDataTable =
                cucumberTypesDefinition.parseResolvableDataTable(DataTable.emptyDataTable());
        Assertions.assertInstanceOf(ResolvableDataTable.class, resolvableDataTable);
    }

    @Test
    void parseResolvableMapTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        ResolvableMap resolvableMap = cucumberTypesDefinition.parseResolvableMap(DataTable.emptyDataTable());
        Assertions.assertInstanceOf(ResolvableMap.class, resolvableMap);
    }

    @Test
    void parseResolvableListTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        ResolvableList resolvableList = cucumberTypesDefinition.parseResolvableList(DataTable.emptyDataTable());
        Assertions.assertInstanceOf(ResolvableList.class, resolvableList);
    }

    @Test
    void parsePairPositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Pair pair = cucumberTypesDefinition.parsePair(List.of("1", "2"));
        Assertions.assertEquals(Pair.of("1", "2"), pair);
    }

    @Test
    void parsePairNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Assertions.assertThrows(
                AutotestException.class, () -> cucumberTypesDefinition.parsePair(List.of("1", "2", "3")));
    }

    @Test
    void parseTriplePositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Triple triple = cucumberTypesDefinition.parseTriple(List.of("1", "2", "3"));
        Assertions.assertEquals(Triple.of("1", "2", "3"), triple);
    }

    @Test
    void parseTripleNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Assertions.assertThrows(AutotestException.class, () -> cucumberTypesDefinition.parseTriple(List.of("1", "2")));
    }

    @Test
    void bodyValidatorPositiveTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        String string = "string";
        HttpBodyValidator httpBodyValidator =
                cucumberTypesDefinition.bodyValidator(List.of("selector", "contains", string));
        Assertions.assertEquals("selector", httpBodyValidator.getSelector());
        Assertions.assertEquals(string, httpBodyValidator.getExpectedValue());
    }

    @Test
    void bodyValidatorNegativeTest() {
        CucumberTypesDefinition cucumberTypesDefinition = new CucumberTypesDefinition();
        Context context = new ContextImpl(Mockito.mock(DefaultContextVariables.class));
        ReflectionTestUtils.setField(cucumberTypesDefinition, "context", context);
        FileLoader fileLoader =
                new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), new FrameworkProperties());
        ContextFunctionsSupplier funcSupplier =
                new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
        StringSubstitutor stringSubstitutor =
                new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
        CucumberPlaceholderResolverImpl placeholderResolver = new CucumberPlaceholderResolverImpl(stringSubstitutor);
        ReflectionTestUtils.setField(cucumberTypesDefinition, "placeholderResolver", placeholderResolver);
        Assertions.assertThrows(
                AutotestException.class, () -> cucumberTypesDefinition.bodyValidator(List.of("1", "2")));
    }
}
