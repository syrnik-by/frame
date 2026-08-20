package ru.autotestframework.http_steps;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.newRequestPattern;
import static org.hamcrest.Matchers.*;
import static ru.autotestframework.util.Validator.notNull;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import com.github.tomakehurst.wiremock.matching.RegexPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Header;
import io.restassured.http.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PreDestroy;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.type.HttpBodyValidator;
import ru.autotestframework.http_steps.components.MockService;
import ru.autotestframework.http_steps.components.MockServiceProperties;

@Slf4j
@RequiredArgsConstructor
@Description("WireMock")
public class WireMockSteps {

    public static final String NOT_NULL_REGEX = ".*";
    private final FileLoaderImpl fileLoader;
    private final PlaceholderResolver placeholderResolver;
    private final MockServiceProperties mockServiceProperties;

    @Getter
    @Setter
    private MockService mockService;

    @Getter
    @Setter
    private RequestPatternBuilder requestPattern;

    @When("запустить сервер заглушек(.)")
    @SneakyThrows
    @Sample("Запустить сервер заглушек")
    @Example(example = "И запустить сервер заглушек.")
    public void startMockServer() {
        mockService = new MockService(mockServiceProperties);
        mockService.startServer();
        mockService.configureClient();
    }

    @When("остановить сервер заглушек(.)")
    @Sample("Остановить сервер заглушек")
    @Example(example = "И остановить сервер заглушек.")
    public void stopMockServer() {
        mockService.stopServer();
    }

    @When("загрузить в заглушку маппинги {path}")
    @Sample("Загрузить маппинги в заглушку")
    @Parameter(type = "path", name = "путь к каталогу с файлами маппингов")
    @Example(example = "И загрузить в заглушку маппинги 'data/mock'")
    public void registerStubMappingsByPattern(final String filesLocationPattern) {
        var mappings = fileLoader.readFilesAsString(filesLocationPattern);
        mappings.forEach(mockService::register);
    }

    @When("загрузить в заглушку маппинг {path}")
    @Sample("Загрузить маппинг в заглушку")
    @Parameter(type = "path", name = "путь к файлам маппинга")
    @Example(example = "И загрузить в заглушку маппинг 'data/mock.json'")
    public void registerStubMappingFromFile(final String jsonPath) {
        var mappingJson = fileLoader.readFileAsString(jsonPath);
        mockService.register(mappingJson);
    }

    @When("загрузить в заглушку маппинг:")
    @Sample("Загрузить маппинг в заглушку")
    @Parameter(type = ":", name = "маппинг в виде строки")
    @Example(example = "И загрузить в заглушку маппинг:" + "\"\"\"" + "mapping" + "\"\"\"")
    public void registerStubMappingFromString(final String mapping) {
        mockService.register(placeholderResolver.resolve(mapping));
    }

    @Then("проверить в заглушке запрос {resolvable_string}, отправленный методом {http_method}")
    @Sample("Провалидировать метод и endpoint запроса в заглушке")
    @Parameter(type = "http_method", name = "метод запроса")
    @Parameter(type = "resolvable_string", name = "эндпоинт для отправки")
    @Example(example = "И проверить в заглушке запрос 'endpoint, отправленный методом POST'")
    public void verifyRequest(final String endpoint, final Method method) {
        requestPattern = newRequestPattern(new RequestMethod(method.name()), WireMock.urlMatching(endpoint));
        verify(requestPattern);
    }

    @Then("этот запрос содержит header'ы:")
    @Then("этот запрос содержит заголовки:")
    @Sample("Добавление header'ов в запрос")
    @Parameter(type = ":", name = "список header'ов")
    @Example(example = "И этот запрос содержит header'ы:" + "| Content-Type | application/octet-stream |")
    public void verifyRequestHeaders(final List<Header> headers) {
        notNull(requestPattern, "RequestPattern is missing within Context");

        headers.forEach(header -> requestPattern.withHeader(header.getName(), matching(header.getValue())));
        verify(requestPattern);
    }

    @Then("этот запрос содержит cookies:")
    @Sample("Проверка наличия cookies в запросе")
    @Parameter(type = ":", name = "список cookies")
    @Example(example = "И этот запрос содержит cookies:" + "| cookie_1 | cookie_parameter_1 |")
    public void verifyRequestCookies(final List<Header> cookies) {
        notNull(requestPattern, "RequestPattern is missing within Context");

        cookies.forEach(cookie -> requestPattern.withCookie(cookie.getName(), matching(cookie.getValue())));
        verify(requestPattern);
    }

    @Then("этот запрос содержит в body строку:")
    @Sample("Добавление body в запрос")
    @Parameter(type = ":", name = "строка с body")
    @Example(example = "И запрос содержит body:" + "\"\"\"" + "body" + "\"\"\"")
    public void checkRequestBody(final String body) {
        notNull(requestPattern, "RequestPattern is missing within Scenario Context");

        requestPattern.withRequestBody(containing(body));
        verify(requestPattern);
    }

    @Then("этот запрос содержит body:")
    @Sample("проверить Body запроса, отправленного на заглушку")
    public void checkRequestBody(final List<HttpBodyValidator> validators) {
        notNull(requestPattern, "RequestPattern is missing within Scenario Context");
        validators.forEach(x -> requestPattern.withRequestBody(matchingJsonPath(
                x.getSelector(),
                matcherTransform(
                        x.getMatcher(),
                        Optional.ofNullable(x.getExpectedValue())
                                .map(Object::toString)
                                .orElse(null)))));
        verify(requestPattern);
    }

    private StringValuePattern matcherTransform(Matcher matcher, String value) {
        StringValuePattern result = null;

        if (matcher instanceof IsEqual) {
            result = equalTo(value);
        }

        if (Objects.equals(matcher.toString(), is(nullValue()).toString())) {
            result = absent();
        }

        if (Objects.equals(matcher.toString(), is(notNullValue()).toString())) {
            result = new RegexPattern(NOT_NULL_REGEX);
        }

        if (matcher instanceof StringContains) {
            result = new ContainsPattern(value);
        }

        if (matcher instanceof org.hamcrest.text.MatchesPattern) {
            result = new RegexPattern(value);
        }

        return Optional.ofNullable(result)
                .orElseThrow(() -> new AutotestException("Unsupported Wiremock matcher found {}", value));
    }

    @When("очистить в заглушке историю запросов")
    @Sample("Очистить историю запросов к заглушке")
    @Example(example = "И очистить в заглушке историю запросов")
    public void cleanRequestsJournal() {
        mockService.cleanRequestsJournal();
    }

    @When("очистить в заглушке маппинги")
    @Sample("Очистить маппинги к заглушке")
    @Example(example = "И очистить в заглушке маппинги")
    public void cleanMappings() {
        mockService.cleanAll();
    }

    /**
     * stops server
     */
    @PreDestroy
    public void stop() {
        if (mockService != null) {
            mockService.stopServer();
        }
    }
}
