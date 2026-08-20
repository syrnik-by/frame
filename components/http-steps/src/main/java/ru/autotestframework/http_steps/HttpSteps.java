package ru.autotestframework.http_steps;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsd;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static ru.autotestframework.Constants.TEMP_HTTP_FOLDER;
import static ru.autotestframework.core.matcher.IsEqualJson.isEqualJson;
import static ru.autotestframework.core.matcher.IsEqualXml.isEqualXml;
import static ru.autotestframework.util.Validator.assertThat;
import static ru.autotestframework.util.Validator.checkThat;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.config.EncoderConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.assertj.core.api.SoftAssertions;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.type.HttpBodyValidator;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.http_steps.components.RequestContainer;
import ru.autotestframework.http_steps.components.restassured.RestAssuredConfiguration;

@Slf4j
@RequiredArgsConstructor
@Description("HTTP")
public class HttpSteps {
    private final Context context;
    private final FileLoaderImpl fileLoader;
    private final RestAssuredConfiguration restAssuredConfiguration;
    private final RequestContainer requestContainer;

    /**
     * returns request specification
     *
     * @return RequestSpecification
     */
    public RequestSpecification getSpecification() {
        return requestContainer.getSpecification();
    }

    /**
     * sets request specification
     *
     * @param specification specification for requests
     */
    public void setSpecification(final RequestSpecification specification) {
        requestContainer.setSpecification(specification);
    }

    /**
     * returns response
     *
     * @return Response
     */
    public Response getResponse() {
        return requestContainer.getResponse();
    }

    /**
     * sets response
     *
     * @param response response
     */
    public void setResponse(final Response response) {
        requestContainer.setResponse(response);
    }

    /**
     * returns validatable response
     *
     * @return ValidatableResponse
     */
    public ValidatableResponse getValidatableResponse() {
        return requestContainer.getValidatableResponse();
    }

    /**
     * sets validatable response
     *
     * @param validatableResponse response
     */
    public void setValidatableResponse(final ValidatableResponse validatableResponse) {
        requestContainer.setValidatableResponse(validatableResponse);
    }

    @When("запрос содержит сертификат {path} с паролем {resolvable_string}")
    @Sample("Добавление сертификата аутентификации в SSL Context")
    @Parameter(type = "path", name = "путь к сертификату аутентификации")
    @Parameter(type = "resolvable_string", name = "пароль сертификата аутентификации")
    @Example(example = "И запрос содержит сертификат 'data/http/certificate/documents.pfx' с паролем '12345'")
    @SuppressWarnings("deprecation")
    public void setCertificateInSSLContext(final String certificatePath, final String password) {
        SSLContext sslContext;
        try (InputStream certificateInputStream = new FileInputStream(fileLoader.getFile(certificatePath))) {

            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(certificateInputStream, password.toCharArray());

            var sslContextBuilder = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray());

            sslContextBuilder.loadTrustMaterial(null, (chain, authType) -> true);

            sslContext = sslContextBuilder.build();
        } catch (Exception e) {
            throw new ExecutionException("Error receiving the certificate", e);
        }

        var sslSocketFactory = new SSLSocketFactory(sslContext);

        var sslConfig = SSLConfig.sslConfig().sslSocketFactory(sslSocketFactory);

        restAssuredConfiguration.createRestAssuredConfig(sslConfig);
    }

    @When("изменить тип кодировки тела запроса с типом {resolvable_string} на {resolvable_string}")
    @Sample("Изменения типа кодировки тела запроса")
    @Parameter(type = "resolvable_string", name = "текущий тип кодировки")
    @Parameter(type = "resolvable_string", name = "необходимый тип кодировки")
    @Example(example = "И изменить тип кодировки тела запроса с типом 'application/octet-stream' на 'text/plain'")
    public void setContentType(final String content, final String type) {
        for (ContentType contentType : EnumSet.allOf(ContentType.class)) {
            if (contentType.toString().equals(type)) {
                restAssuredConfiguration.createRestAssuredConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(content, contentType));
            }
        }
    }

    @When(("отправить запрос {http_method} на эндпоинт {resolvable_string}, ожидаемый код ответа {int}"))
    public void sendRequestToEndpoint(Method method, String endpoint, int status) {
        setEndpoint(endpoint);
        sendRequest(method);
        checkResponseStatus(status);
    }

    @When("адрес сервера {resolvable_string}")
    @Sample("Добавление baseUri в запрос")
    @Parameter(type = "resolvable_string", name = "адрес сервера")
    @Example(example = "И адрес сервера 'https://retail-tst.payment.ru/'")
    public void setUrl(final String url) {
        requestContainer.setSpecification(
                restAssuredConfiguration.getRequestSpecification().baseUri(url));
    }

    @When("эндпоинт {resolvable_string}")
    @Sample("Добавление эндпоинта")
    @Parameter(type = "resolvable_string", name = "эндпоинт")
    @Example(example = "И эндпоинт '/endpoint'")
    public void setEndpoint(final String endpoint) {
        // see https://github.com/rest-assured/rest-assured/issues/800
        boolean isBasePathContainsBadChar = endpoint.contains("{");
        checkThat(!isBasePathContainsBadChar, "URL contains invalid character: '{'");
        requestContainer.setSpecification(getSpecification().basePath(endpoint));
    }

    @When("установить header'ы:")
    @Sample("Добавление header'ов в запрос")
    @Parameter(type = ":", name = "список header'ов")
    @Example(example = "И установить header'ы:" + "| Content - Type | application / octet - stream |")
    public void setHeaders(final List<Header> headers) {
        requestContainer.getSpecification().headers(new Headers(headers));
    }

    @When("это SOAP запрос")
    @Sample("Указание что это SOAP запрос")
    @Example(example = "И это SOAP запрос")
    public void setSoap() {
        requestContainer.getSpecification().when().header("Content-type", "text/xml; charset=UTF-8");
    }

    @When("установить query parameters:")
    @Sample("Добавить query параметры в запрос")
    @Parameter(type = ":", name = "список query параметров")
    @Example(example = "И установить query parameters:" + "| key | value |")
    public void setQueryParameters(final List<Pair> parameters) {
        for (var parameter : parameters) {
            requestContainer.getSpecification().queryParam(parameter.getFirst(), parameter.getSecond());
        }
    }

    @When("установить form parameters:")
    @Sample("Добавить form параметры в запрос")
    @Parameter(type = ":", name = "список form параметров")
    @Example(example = "И установить form parameters:" + "| key | value |")
    public void setFormParameters(final List<Pair> parameters) {
        for (var parameter : parameters) {
            requestContainer.getSpecification().formParam(parameter.getFirst(), parameter.getSecond());
        }
    }

    @When("установить cookies:")
    @Sample("Добавление cookies в запрос")
    @Parameter(type = ":", name = "список cookies")
    @Example(example = "И установить cookies:" + "| key | value |")
    public void setCookie(final List<Pair> cookies) {
        for (var cookie : cookies) {
            requestContainer.getSpecification().cookie(cookie.getFirst(), cookie.getSecond());
        }
    }

    @When("установить Basic аутентификацию: {resolvable_string} {resolvable_string}")
    @Sample("Добавление данные для basic аутентификации")
    @Parameter(type = "resolvable_string", name = "логин")
    @Parameter(type = "resolvable_string", name = "пароль")
    @Example(example = "И установить Basic аутентификацию: 'login' 'password'")
    public void setBasicAuth(final String login, final String password) {
        requestContainer.getSpecification().auth().preemptive().basic(login, password);
    }

    @When("запрос содержит body {path}")
    @Sample("Добавление body в запрос из файла")
    @Parameter(type = "path", name = "путь к файлу с body")
    @Example(example = "И запрос содержит body 'data/http/body.json'")
    public void setBodyByPath(final String path) {
        var body = fileLoader.readFileAsString(path);
        requestContainer.getSpecification().and().body(body);
    }

    @When("запрос содержит body:")
    @Sample("Добавление body в запрос")
    @Parameter(type = ":", name = "строка с body")
    @Example(example = "И запрос содержит body:" + "\"\"\"" + "body" + "\"\"\"")
    public void setBody(final String body) {
        requestContainer.getSpecification().and().body(body);
    }

    @When("запрос содержит файл {path} с именем {resolvable_string}")
    @Sample("Добавление файла в запрос как multiPart")
    @Parameter(type = "path", name = "путь к файлу")
    @Parameter(type = "resolvable_string", name = "имя файла в запросе")
    @Example(example = "И запрос содержит файл 'data/http/file.txt' с именем 'file'")
    public void setFile(final String path, final String fileName) {
        requestContainer.getSpecification().and().multiPart(fileName, fileLoader.getFile(path));
    }

    @When("отправить {http_method} запрос")
    @Sample("Отправка текущего запроса выбранным методом")
    @Parameter(type = "http_method", name = "метод отправки")
    @Example(example = "И отправить POST запрос")
    public void sendRequest(final Method method) {
        restAssuredConfiguration.configureLoggingFilters(requestContainer.getSpecification());
        requestContainer.setResponse(requestContainer.getSpecification().request(method));
        requestContainer.setValidatableResponse(requestContainer.getResponse().then());
    }

    @Then("получить переменные из body:")
    @Sample("Получение переменных из body")
    @Parameter(type = ":", name = "список переменных и имен значение в body")
    @Example(example = "И получить переменные из body:" + "| variableName | valueName |")
    public void putVariablesFromResponse(final List<Pair> rows) {
        for (var row : rows) {
            var variableName = row.getFirst();
            var selector = row.getSecond();
            var value = requestContainer.getResponse().path(selector);
            context.set(variableName, value);
        }
    }

    @Then("сохранить из ответа файл с именем {resolvable_string}, записать путь в переменную {string}")
    @Sample("Получение файла из body")
    @Parameter(type = "resolvable_string", name = "Наименование файла")
    @Parameter(type = "path", name = "путь куда сохранитcя файл")
    @Example(example = "И сохранить из ответа файл с именем 'temp.txt', записать путь в переменную 'filePath'")
    public void saveFileResponse(final String fileName, final String variableName) {

        var uuid = UUID.randomUUID().toString();
        var path = Path.of(TEMP_HTTP_FOLDER, uuid, fileName).toString();
        try (var inputStream = requestContainer.getResponse().getBody().asInputStream()) {
            fileLoader.createFile(path, inputStream);
        } catch (Exception e) {
            throw new ExecutionException("Error receiving the file", e);
        }
        context.set(variableName, "file:".concat(path));
    }

    @Then("статус ответа = {int}")
    @Sample("Проверка статуса ответа")
    @Parameter(type = "int", name = "код ответа")
    @Example(example = "И статус ответа = 200")
    public void checkResponseStatus(final int status) {
        requestContainer.getValidatableResponse().statusCode(status);
    }

    @Then("статус ответа {matcher} {resolvable_string}")
    @Sample("Проверка статуса ответа")
    @Parameter(type = "int", name = "код ответа")
    @Example(example = "И статус ответа >= 200" + "И статус ответа<=210")
    public void checkResponseStatus(final MatcherName matcherName, String values) {
        var matcher = MatcherParser.getMatcher(matcherName, values);
        requestContainer.getValidatableResponse().statusCode(matcher);
    }

    @Then("время ответа <= {int} мс")
    @Sample("Проверка времени ответа")
    @Parameter(type = "int", name = "время в миллисекундах")
    @Example(example = " И время ответа <= 5000 мс")
    public void checkResponseTime(final int responseTime) {
        requestContainer.getValidatableResponse().time(lessThanOrEqualTo((long) responseTime));
    }

    @Then("ответ не пустой")
    @Sample("Проверить что ответ не пустой")
    @Example(example = "И ответ не пустой")
    public void checkThatResponseNotEmpty() {
        int responseLength = requestContainer.getResponse().asByteArray().length;
        assertThat(responseLength, greaterThan(0), "Response is empty");
    }

    @Then("ответ пустой")
    @Sample("Проверить что ответ пустой")
    @Example(example = "И ответ пустой")
    public void checkThatResponseIsEmpty() {
        int responseLength = requestContainer.getResponse().asByteArray().length;
        assertThat(responseLength, equalTo(0), "Response is not empty");
    }

    @Then("ответ содержит body:")
    @Sample("Проверка ответа на определенное body по jsonPath")
    @Parameter(type = ":", name = "шаблон body")
    @Example(example = "И ответ содержит body:\n" + "\"\"\"\nbody\n\"\"\"")
    public void checkResponseBodyWithValidator(final List<HttpBodyValidator> validators) {
        SoftAssertions.assertSoftly(
                softly -> validators.forEach(validator -> softly.assertThatCode(() -> requestContainer
                                .getValidatableResponse()
                                .assertThat()
                                .body(validator.getSelector(), validator.getMatcher()))
                        .doesNotThrowAnyException()));
    }

    @Then("ответ соответствует JSON схеме {path}")
    @Sample("Проверка ответа на соответствие JSON схеме")
    @Parameter(type = "path", name = "путь к JSON схеме")
    @Example(example = "И ответ соответствует JSON схеме 'data/http/scheme.json'")
    public void checkResponseByJsonSchema(final String jsonFilePath) {
        var jsonSchema = fileLoader.readFileAsString(jsonFilePath);
        requestContainer.getValidatableResponse().assertThat().body(matchesJsonSchema(jsonSchema));
    }

    @Then("ответ соответствует XSD схеме {path}")
    @Sample("Проверка ответа на соответствие XSD схеме")
    @Parameter(type = "path", name = "путь к XSD схеме")
    @Example(example = "И ответ соответствует XSD схеме 'data/http/scheme.xsd'")
    public void checkResponseByXsdSchema(final String filePath) {
        var xsdSchema = fileLoader.readFileAsString(filePath);
        requestContainer.getValidatableResponse().assertThat().body(matchesXsd(xsdSchema));
    }

    @SneakyThrows
    @When("строка {resolvable_string} соответствует XSD схеме {path}")
    public void checkXsd(String xml, String xsdPath) {
        var xsdStreamSource = new StreamSource(xsdPath);
        var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        var schema = schemaFactory.newSchema(xsdStreamSource);
        var validator = schema.newValidator();
        validator.validate(new StreamSource(xml));
    }

    // необходимо для тех JSON, что получены не посредством http(-s) запросов), н-р после декода JWT
    @SneakyThrows
    @Then("строка {resolvable_string} соответствует JSON схеме {path}")
    @Sample("Проверка JSON на соответствие JSON схеме")
    @Parameter(type = "path", name = "путь к JSON схеме")
    @Parameter(type = "resolvable_variable", name = "переменная содержащая строку JSON")
    @Example(
            example = "проверить, что JSON из переменной 'decodedResponse'"
                    + "соответствует JSON схеме 'data/http/scheme.json'")
    public void checkJsonByJsonSchema(String payloadVarName, String jsonSchemaPath) {
        final var fstabSchema = JsonLoader.fromFile(fileLoader.getFile(jsonSchemaPath));
        final var json = JsonLoader.fromString(context.get(payloadVarName));
        final var factory = JsonSchemaFactory.byDefault();
        final var schema = factory.getJsonSchema(fstabSchema);
        var report = schema.validate(json);
        assertThat(report.isSuccess(), report.toString());
    }

    @Then("ответ содержит XML {path}")
    @Sample("Проверка ответа на наличие XML")
    @Parameter(type = "path", name = "путь к файлу XML")
    @Example(example = "И ответ содержит XML 'data/http/file.xml'")
    public void checkXmlBodyByFile(final String filePath) {
        var expectedXml = fileLoader.readFileAsString(filePath);
        requestContainer.getValidatableResponse().assertThat().body(isEqualXml(expectedXml));
    }

    @Then("ответ содержит XML:")
    @Sample("Проверка ответа на наличие XML")
    @Parameter(type = ":", name = "xml")
    @Example(example = "И ответ содержит XML:" + "\"\"\"" + "xml" + "\"\"\"")
    public void checkXmlBody(final String xml) {
        requestContainer.getValidatableResponse().assertThat().body(isEqualXml(xml));
    }

    @Then("ответ содержит JSON {path}")
    @Sample("Проверка ответа на наличие JSON")
    @Parameter(type = "path", name = "путь к файлу JSON")
    @Example(example = "И ответ содержит XML 'data/http/file.json'")
    public void checkJsonBodyByFile(final String filePath) {
        var expectedJson = fileLoader.readFileAsString(filePath);
        requestContainer.getValidatableResponse().assertThat().body(isEqualJson(expectedJson, JSONCompareMode.LENIENT));
    }

    @Then("ответ содержит JSON:")
    @Sample("Проверка ответа на наличие JSON")
    @Parameter(type = ":", name = "json")
    @Example(example = "И ответ содержит JSON:" + " \"\"\" " + " json " + " \"\"\" ")
    public void checkJsonBody(final String json) {
        requestContainer.getValidatableResponse().assertThat().body(isEqualJson(json, JSONCompareMode.LENIENT));
    }

    @Then("тело ответа {matcher} {resolvable_string}")
    @Sample("Проверка тела ответа по matcher-у")
    @Parameter(type = ":", name = "json")
    @Example(example = "И тело ответа contains '${{paymentId}}'" + "И тело ответа matchesRegex '^\\d+$'")
    public void checkBody(final MatcherName matcherName, final String expected) {
        var matcher = MatcherParser.getMatcher(matcherName, expected);
        requestContainer.getValidatableResponse().assertThat().body(matcher);
    }
}
