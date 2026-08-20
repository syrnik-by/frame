package ru.autotestframework.cucumber.type;

import static ru.autotestframework.Messages.BODY_VALIDATOR_EXAMPLE;
import static ru.autotestframework.util.StringUtil.trimQuotes;
import static ru.autotestframework.util.Validator.checkThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.DataTableTypeRegistryTableConverter;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DocStringType;
import io.cucumber.java.ParameterType;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.Constants;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.parser.Type;
import ru.autotestframework.cucumber.type.resolvable.ResolvableDataTable;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.cucumber.type.resolvable.ResolvableString;

/**
 * Cucumber types definition.
 */
@Slf4j
@RequiredArgsConstructor
public class CucumberTypesDefinition {

    private static final DataTableTypeRegistry REGISTRY = new DataTableTypeRegistry(Locale.ENGLISH);
    /**
     * The constant TABLE_CONVERTER.
     */
    public static final DataTable.TableConverter TABLE_CONVERTER = new DataTableTypeRegistryTableConverter(REGISTRY);
    /**
     * The constant TRIPLE_COLUMN_SIZE.
     */
    public static final int TRIPLE_COLUMN_SIZE = 3;

    @Autowired
    private PlaceholderResolver placeholderResolver;

    @Autowired
    private Context context;

    /**
     * Resolvable string string.
     *
     * @param value the value
     * @return the string
     */
    // методы записаны в snake_case, т.к. иначе IntelliJ не определяет этот параметр в *.features
    @ParameterType(name = "resolvable_string", value = Constants.STRING_REGEX_2)
    public String resolvable_string(final String value) {
        return trimQuotes(value);
    }

    /**
     * Object t.
     *
     * @param <T>          the type parameter
     * @param variableName the variable name
     * @return the t
     */
    @ParameterType(name = "object", value = Constants.STRING_REGEX_2)
    public <T extends Object> T object(final String variableName) {
        return context.getObj(variableName);
    }

    /**
     * Visibility boolean.
     *
     * @param value the value
     * @return the boolean
     */
    @ParameterType(name = "visibility", value = "отображается|отображаются|не отображается|не отображаются|")
    public Boolean visibility(final String value) {
        return !value.contains("не");
    }

    /**
     * Fulled table boolean.
     *
     * @param value the value
     * @return the boolean
     */
    @ParameterType(name = "fulledTable", value = "не пустая|пустая")
    public Boolean fulledTable(final String value) {
        return !value.contains("не");
    }

    /**
     * Activity boolean.
     *
     * @param value the value
     * @return the boolean
     */
    @ParameterType(name = "activity", value = "активен|активны|не активен|не активны|")
    public Boolean activity(final String value) {
        return !value.contains("не");
    }

    /**
     * Editable boolean.
     *
     * @param value the value
     * @return the boolean
     */
    @ParameterType(name = "editable", value = "доступен|доступны|недоступен|недоступны|")
    public Boolean editable(final String value) {
        return !value.contains("не");
    }

    /**
     * Click type boolean.
     *
     * @param value the value
     * @return the boolean
     */
    @ParameterType(name = "clickType", value = "кликнуть|нажать|дважды кликнуть|дважды нажать|")
    public Boolean clickType(final String value) {
        return value.contains("дважды");
    }

    /**
     * Storages string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "storages", value = "localStorage|sessionStorage|")
    public String storages(final String value) {
        return placeholderResolver.resolve(trimQuotes(value));
    }

    /**
     * Path string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "path", value = Constants.STRING_REGEX_2)
    public String path(final String value) {
        return placeholderResolver
                .resolve(trimQuotes(value))
                .replaceAll("\\\\\"", "\"")
                .replaceAll("\\\\'", "'");
    }

    /**
     * Popup action string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "popup_action", value = "Подтвердить|Отменить|")
    public String popup_action(final String value) {
        return value;
    }

    /**
     * Matcher matcher name.
     *
     * @param matcherSymbol the matcher symbol
     * @return the matcher name
     */
    @ParameterType(name = "matcher", value = MatcherName.REGEX)
    public MatcherName matcher(final String matcherSymbol) {
        return MatcherName.getBy(matcherSymbol);
    }

    /**
     * Scroll direction string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "scrollDirection", value = "вниз|вверх|")
    public String scrollDirection(final String value) {
        return value;
    }

    /**
     * Page direction string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "pageDirection", value = "назад|вперёд|")
    public String pageDirection(final String value) {
        return value;
    }

    /**
     * Table element string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "table_element", value = "столбец|строка|")
    public String tableElement(final String value) {
        return value;
    }

    /**
     * Queue string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType(name = "queue", value = "KAFKA|RABBITMQ|QUEUE|")
    public String queue(final String value) {
        return value;
    }

    /**
     * Parse resolvable string resolvable string.
     *
     * @param unresolvableList the unresolvable list
     * @return the resolvable string
     */
    @DataTableType
    public ResolvableString parseResolvableString(final List<String> unresolvableList) {
        checkThat(unresolvableList.size() == 1, "Table should contain only 1 column");
        return new ResolvableString(unresolvableList.get(0), placeholderResolver);
    }

    /**
     * Parse resolvable string resolvable string.
     *
     * @param unresolvableString the unresolvable string
     * @return the resolvable string
     */
    @DocStringType
    public ResolvableString parseResolvableString(final String unresolvableString) {
        return new ResolvableString(unresolvableString, placeholderResolver);
    }

    /**
     * Parse resolvable data table resolvable data table.
     *
     * @param unresolvableDataTable the unresolvable data table
     * @return the resolvable data table
     */
    @DataTableType
    public ResolvableDataTable parseResolvableDataTable(final DataTable unresolvableDataTable) {
        return new ResolvableDataTable(
                unresolvableDataTable, (ru.autotestframework.cucumber.PlaceholderResolver) placeholderResolver);
    }

    /**
     * Parse resolvable map resolvable map.
     *
     * @param unresolvableDataTable the unresolvable data table
     * @return the resolvable map
     */
    @DataTableType
    public ResolvableMap parseResolvableMap(final DataTable unresolvableDataTable) {
        Map<String, String> unresolvableMap = TABLE_CONVERTER.toMap(unresolvableDataTable, String.class, String.class);
        return new ResolvableMap(unresolvableMap, placeholderResolver);
    }

    /**
     * Parse resolvable list resolvable list.
     *
     * @param unresolvableDataTable the unresolvable data table
     * @return the resolvable list
     */
    @DataTableType
    public ResolvableList parseResolvableList(final DataTable unresolvableDataTable) {
        List<String> unresolvableList = TABLE_CONVERTER.toList(unresolvableDataTable, String.class);
        return new ResolvableList(unresolvableList, placeholderResolver);
    }

    /**
     * Parse pair pair.
     *
     * @param columns the columns
     * @return the pair
     */
    @DataTableType
    public Pair parsePair(final List<String> columns) {
        checkThat(columns.size() == 2, "Table should contain only 2 columns");

        var first = placeholderResolver.resolve(columns.get(0));
        var second = placeholderResolver.resolve(columns.get(1));

        return Pair.of(first, second);
    }

    /**
     * Parse triple triple.
     *
     * @param columns the columns
     * @return the triple
     */
    @DataTableType
    public Triple parseTriple(final List<String> columns) {
        checkThat(columns.size() == TRIPLE_COLUMN_SIZE, "Table should contain only 3 columns");

        var first = placeholderResolver.resolve(columns.get(0));
        var second = placeholderResolver.resolve(columns.get(1));
        var third = placeholderResolver.resolve(columns.get(2));

        return Triple.of(first, second, third);
    }

    /**
     * Body validator http body validator.
     *
     * @param columns the columns
     * @return the http body validator
     */
    @DataTableType
    public HttpBodyValidator bodyValidator(final List<String> columns) {
        checkThat(columns.size() == TRIPLE_COLUMN_SIZE, "Incorrect DataTable format. {}", BODY_VALIDATOR_EXAMPLE);
        var selector = placeholderResolver.resolve(columns.get(0));
        var matcherSymbol = placeholderResolver.resolve(columns.get(1));
        var operand = placeholderResolver.resolve(columns.get(2));

        var matcherName = MatcherName.getBy(matcherSymbol);
        var objectType = Type.getByOperand(operand);

        var expectedObject = MatcherParser.getObject(objectType, trimQuotes(operand));
        var matcher = MatcherParser.getMatcher(matcherName, expectedObject);

        return new HttpBodyValidator(selector, matcher, expectedObject);
    }
}
