package ru.autotestframework.cucumber.step_defs.back;

import static ru.autotestframework.util.Validator.assertThat;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Cleanable;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.step_executor.DontAddToStepExecutor;
import ru.autotestframework.cucumber.step_executor.StepExecutor;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.cucumber.type.Triple;

/**
 * Common steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("Общие")
public class CommonSteps {

    /**
     * The constant MILLIS_IN_SECOND.
     */
    public static final long MILLIS_IN_SECOND = 1000L;

    private final FrameworkProperties frameworkProperties;
    private final List<Cleanable> cleanableBins;
    private final StepExecutor stepExecutor;
    private final PlaceholderResolver placeholderResolver;
    private final Context context;
    private final XPathFactory xpathFactory = XPathFactory.newInstance();
    private final XPath xpath = xpathFactory.newXPath();

    @SneakyThrows
    private static Document convertStringToDocument(final String xmlStr) {
        var factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new ExecutionException("The conversion of a string to an XML document failed with an error {}", e);
        }
    }

    /**
     * Sets variable.
     *
     * @param key   the key
     * @param value the value
     */
    @When("установить переменную {resolvable_string} = {resolvable_string}")
    @Sample("Добавление переменной в context")
    @Parameter(type = "resolvable_string", name = "имя переменной")
    @Parameter(type = "resolvable_string", name = "значение переменной")
    @Example(example = "И установить переменную 'variableName' = 'value'")
    public void setVariable(final String key, final String value) {
        context.set(key, value);
    }

    /**
     * Sets variables.
     *
     * @param variables the variables
     */
    @When("установить переменные:")
    @Sample("Добавление переменных в context")
    @Parameter(type = ":", name = "список переменных")
    @Example(example = "И установить переменные:" + "| variableName | value |")
    public void setVariables(final Map<String, String> variables) {
        for (var row : variables.entrySet()) {
            context.set(placeholderResolver.resolve(row.getKey()), placeholderResolver.resolve(row.getValue()));
        }
    }

    /**
     * Validate variables.
     *
     * @param rows the rows
     */
    @Then("переменные имеют значения:")
    @Sample("Проверка переменных в context")
    @Parameter(type = ":", name = "список переменных")
    @Example(example = "И переменные имеют значения:" + "| variableName | == | value |")
    @SuppressWarnings("unchecked")
    public void validateVariables(final List<Triple> rows) {
        SoftAssertions.assertSoftly(softly -> rows.forEach(row -> {
            var variableName = row.getFirst();
            var matcherSymbol = row.getSecond();
            var matcherName = MatcherName.getBy(matcherSymbol);
            var actualValue = matcherName.isStringMatcher()
                    ? context.get(variableName)
                    : new BigDecimal(context.get(variableName));
            var expectedValue = matcherName.isStringMatcher() ? row.getThird() : new BigDecimal(row.getThird());
            var matcher = MatcherParser.getMatcher(matcherName, expectedValue);
            softly.assertThatCode(() -> assertThat(
                            actualValue,
                            matcher,
                            "Value of variable '{}': '{}' doesn't match the expected `{} {}`",
                            variableName,
                            actualValue,
                            matcherSymbol,
                            expectedValue))
                    .doesNotThrowAnyException();
        }));
    }

    /**
     * Sleep.
     *
     * @param seconds the seconds
     */
    @SneakyThrows
    @When("установить ожидание {int} секунд/секунду/секунды")
    @Sample("Установить ожидание")
    @Parameter(type = "int", name = "время задержки в секундах")
    @Example(example = "И установить ожидание 5 секунд")
    public void sleep(final Integer seconds) {
        Thread.sleep(seconds * MILLIS_IN_SECOND);
    }

    /**
     * Clean context.
     */
    @When("очистить контекст")
    @Sample("Очистка context")
    @Example(example = "И очистить контекст")
    public void cleanContext() {
        cleanableBins.forEach(Cleanable::clean);
        log.info("Context was cleared");
    }

    /**
     * Reset variables to default.
     */
    @When("вернуть переменные к дефолтным значениям")
    @Sample("Вернуть все переменные к значениям по умолчанию")
    @Example(example = "И вернуть переменные к дефолтным значениям")
    public void resetVariablesToDefault() {
        context.clean();
    }

    /**
     * Variable taken with regex to new variable.
     *
     * @param rows the rows
     */
    @When("получить переменные регуляркой из других переменных:")
    @Sample("Извлечь переменные из строки, используя RegExp-s")
    @Parameter(type = ":", name = "список переменных")
    @Example(
            example = "получить переменные регуляркой из других переменных:"
                    + "| newVariableName | .+(?=@) | oldVariableName |")
    public void variableTakenWithRegexToNewVariable(final List<Triple> rows) {
        for (var row : rows) {
            var newVariableName = row.getFirst();
            var regex = row.getSecond();
            var oldVariableName = row.getThird();
            var content = context.get(oldVariableName);
            var matcher = Pattern.compile(regex).matcher(content);
            assertThat(matcher.find(), "No matches found for regex '{}' in variable: {}", regex, content);
            var result = matcher.group();
            context.set(newVariableName, result);
        }
    }

    /**
     * Put variables from xml response.
     *
     * @param stringToParse the string to parse
     * @param rows          the rows
     * @throws XPathExpressionException the x path expression exception
     */
    @When("получить переменные из XML строки {resolvable_string} по XPath:")
    @Sample("Извлечь переменные из XML документа, представленного строкой, используя xpath")
    @Parameter(type = ":", name = "список переменных")
    @Example(
            example = "получить переменные из XML строки '${{xmlString}}' по XPath:"
                    + "| arrayFemale | /students/student[gender='Female']/name |")
    public void putVariablesFromXMLResponse(final String stringToParse, final List<Pair> rows)
            throws XPathExpressionException {
        var doc = convertStringToDocument(stringToParse.replaceAll("[\r\n]", ""));
        for (var row : rows) {
            var variableName = row.getFirst();
            var selector = placeholderResolver.resolve(row.getSecond());
            var xPathExpression = xpath.compile(selector);
            var xpathResult = xPathExpression.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) xpathResult;

            String res;
            if (nodes.getLength() == 1) {
                var singleNode = nodes.item(0);
                res = singleNode.getTextContent();

            } else {
                List<String> list = new ArrayList<>();
                for (var i = 0; i < nodes.getLength(); i++) {
                    String nodeValue = nodes.item(i).getTextContent().trim();

                    if (!StringUtils.isBlank(nodeValue)) {
                        list.add(nodeValue);
                    }
                }
                res = String.join(frameworkProperties.getArrayStringDelimiter(), list);
            }
            context.set(variableName, res);
        }
    }

    /**
     * Start retry steps chain.
     *
     * @param maxAttempts  the max attempts
     * @param waitDuration the wait duration
     */
    @DontAddToStepExecutor
    @When("начало цепочки шагов с {int} попытками с интервалом {int} мс")
    @Sample(
            "Установить кол-во повторений цепочки шагов с интервалом (конец цепочки определяется шагом \"конец чепочки шагов\"")
    @Parameter(type = "int", name = "количество попыток")
    @Parameter(type = "int", name = "интервал между попытками")
    @Example(example = "И начало цепочки шагов с 5 попытками с интервалом 5000 мс")
    public void startRetryStepsChain(final int maxAttempts, final int waitDuration) {
        stepExecutor.enable(maxAttempts, waitDuration);
    }

    /**
     * Start.
     *
     * @param minPassedSteps the min passed steps
     */
    @DontAddToStepExecutor
    @When("начало цепочки шагов в которой {int} шаг/шага/шагов должен/должны быть успешным/успешными")
    @Sample(
            "Установить цепочки шагов в которой только определенное кол-во должны быть успешными (конец цепочки определяется шагом \"конец чепочки шагов\"")
    @Parameter(type = "int", name = "количество шагов")
    @Example(example = "И начало цепочки шагов в которой 1 шаг должен быть успешным")
    public void start(final int minPassedSteps) {
        stepExecutor.enable(minPassedSteps);
    }

    /**
     * Stop steps chain.
     */
    @DontAddToStepExecutor
    @When("конец цепочки шагов")
    @Sample("Установить окончание цепочки шагов")
    @Example(example = "И конец цепочки шагов")
    public void stopStepsChain() {
        stepExecutor.execute();
    }

    /**
     * Gets variables from json by json path.
     *
     * @param json the json
     * @param rows the rows
     */
    @When("получить переменные из json {resolvable_string}:")
    @Sample("получение значение переменных из указанной переменной, содержащей json")
    @Parameter(type = "resolvable_string", name = "имя контекстной переменной, содержащей json")
    @Parameter(type = ":", name = "список переменных и имен значение в body")
    @Example(example = "И получить переменные из json {resolvable_string}:" + "| variableName | valueName |")
    @SneakyThrows
    public void getVariablesFromJsonByJsonPath(String json, List<Pair> rows) {
        var conf = Configuration.builder()
                .jsonProvider(new JsonOrgJsonProvider())
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        var jsonObject = new JSONObject(json);
        for (var row : rows) {
            var variableName = row.getFirst();
            var selector = placeholderResolver.resolve(row.getSecond());
            var value = JsonPath.compile("$." + selector).read(jsonObject, conf);
            // https://github.com/json-path/JsonPath/issues/272
            if (selector.contains("?(@")) {
                value = ((JSONArray) value).get(0).toString();
            }
            context.set(variableName, value);
        }
    }

    /**
     * Make arithmetic operation format.
     *
     * @param args         the args
     * @param variableName the variable name
     * @param format       the format
     */
    @When(
            "выполнить арифметическую операцию {resolvable_string} и записать результат в переменную {resolvable_string} форматирование {resolvable_string}")
    @Sample("Выполняет арифметическую операцию")
    @Parameter(type = "{resolvable_string}", name = "арифметическое выражение")
    @Parameter(type = "{resolvable_string}", name = "название переменной")
    @Parameter(type = "{resolvable_string}", name = "формат ответа")
    @Example(
            example =
                    "И выполнить арифметическую операцию '(5+12)/2' и записать результат в переменную 'Результат' форматирование '%.1f'")
    @SneakyThrows
    public void makeArithmeticOperationFormat(String args, String variableName, String format) {
        var regex = "\\s+|\\u00a0";
        var sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("JavaScript");
        var replaceSpaceInString = args.replaceAll(regex, "");
        log.info("Удалены пробелы и служебные символы из выражения {}", args);
        var result = "";
        if (args.contains(".") || args.contains(",")) {
            Double value = (Double) engine.eval(replaceSpaceInString);
            result = String.format(format, value).replace(",", ".");
            log.info("Применен шаблон форматирования {}, (точность - 1 знак после запятой) {}", format, result);
        } else {
            result = engine.eval(replaceSpaceInString).toString();
        }

        log.info(". Результат выполнения арифметической операции :{}", result);
        context.set(variableName, result);
    }
}
