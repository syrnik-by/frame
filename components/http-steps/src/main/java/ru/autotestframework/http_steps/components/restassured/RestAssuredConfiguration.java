package ru.autotestframework.http_steps.components.restassured;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static org.apache.http.impl.client.DefaultHttpClient.setDefaultHttpParams;
import static ru.autotestframework.Constants.HTTP_PROXY_HOST;
import static ru.autotestframework.Constants.HTTP_PROXY_PORT;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.SyncBasicHttpParams;
import org.springframework.context.annotation.Configuration;
import ru.autotestframework.configuration.FrameworkProperties;

@Slf4j
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class RestAssuredConfiguration {

    private final FrameworkProperties frameworkProperties;
    private final RestAssuredProperties properties;
    private final List<SpecificationConfigurer> customSpecConfigurators;
    private RestAssuredConfig restAssuredConfig;

    /**
     * Apply Project customization of specification.
     * @return specification
     */
    public RequestSpecification getRequestSpecification() {
        var specification = given().config(createRestAssuredConfig());

        specification = applyProxy(specification);

        for (var customConfigurator : customSpecConfigurators) {
            specification = customConfigurator.configure(specification);
        }

        return specification;
    }

    /**
     * Apply framework configurable Proxy to specification.
     * @param specification RequestSpecification to add filters
     * @return specification
     */
    public RequestSpecification applyProxy(final RequestSpecification specification) {
        String port = System.getProperty(HTTP_PROXY_PORT);
        String host = System.getProperty(HTTP_PROXY_HOST);
        if (StringUtils.isNotBlank(port) && StringUtils.isNotBlank(host)) {
            var proxySpecification = ProxySpecification.host(host).withPort(Integer.parseInt(port));
            log.info("RestAssured proxy enabled on {}:{}", host, port);
            specification.proxy(proxySpecification);
        }

        return specification;
    }

    /**
     * При каждом запросе, RestAssured добавляет два своих фильтра, даже если они уже есть.
     * Это приводит к тому, что добавленные кастомные фильтры не исполняются. Поэтому нужно перед каждым запросом
     * очищать фильтры и устанавливать их заново.
     * @param requestSpecification RequestSpecification to add filters
     */
    public void configureLoggingFilters(final RequestSpecification requestSpecification) {
        if (properties.isLogsEnabled()) {
            requestSpecification
                    .noFilters()
                    .filter(new RequestLoggingFilter(LogDetail.ALL, properties.isPrettyPrintEnabled(), System.out))
                    .filter(new ResponseLoggingFilter(LogDetail.ALL, properties.isPrettyPrintEnabled(), System.out))
                    .filter(new AllureRestAssuredFilter());
        }
    }

    /**
     * configures RA config
     * @param encoderConfig
     */
    public void createRestAssuredConfig(final EncoderConfig encoderConfig) {
        checkRestAssuredConfig();
        restAssuredConfig = restAssuredConfig.encoderConfig(encoderConfig);
    }

    /**
     * configures RA config
     * @param sslConfig
     */
    public void createRestAssuredConfig(final SSLConfig sslConfig) {
        checkRestAssuredConfig();
        restAssuredConfig = restAssuredConfig.sslConfig(sslConfig);
    }

    /**
     * configures RA config
     * @param httpClientConfig
     */
    public void createRestAssuredConfig(final HttpClientConfig httpClientConfig) {
        checkRestAssuredConfig();
        restAssuredConfig = restAssuredConfig.httpClient(httpClientConfig);
    }

    /**
     * @return Basic RA config.
     */
    public RestAssuredConfig createRestAssuredConfig() {
        if (restAssuredConfig != null) {
            return restAssuredConfig;
        }
        restAssuredConfig = RestAssured.config()
                .httpClient(getDefaultHttpClientConfig())
                .sslConfig(getDefaultSSLConfig())
                .encoderConfig(getDefaultEncoderConfig())
                .jsonConfig(getJsonConfig());

        RestAssured.replaceFiltersWith(
                new RequestLoggingFilter(LogDetail.ALL, properties.isPrettyPrintEnabled(), System.out),
                (new ResponseLoggingFilter(LogDetail.ALL, properties.isPrettyPrintEnabled(), System.out)),
                (new AllureRestAssuredFilter()));

        return restAssuredConfig;
    }

    private JsonConfig getJsonConfig() {

        return jsonConfig().numberReturnType(properties.getNumberReturnType());
    }

    private EncoderConfig getDefaultEncoderConfig() {
        return encoderConfig().defaultCharsetForContentType("UTF-8", "application/json");
    }

    private SSLConfig getDefaultSSLConfig() {
        var sslConfig = new SSLConfig();
        if (!frameworkProperties.isSslEnabled()) {
            sslConfig = sslConfig.allowAllHostnames().relaxedHTTPSValidation();
        }
        var trustStore = frameworkProperties.getTrustStore();
        if ((StringUtils.isNotBlank(trustStore))) {
            sslConfig = sslConfig.trustStore(trustStore, "changeit");
        }
        return sslConfig;
    }

    private HttpClientConfig getDefaultHttpClientConfig() {
        // TODO Заменить на HttpClientBuilder когда пофиксят http://jira.codehaus.org/browse/GROOVY-4647
        // в новых версиях RestAssured
        return new HttpClientConfig().httpClientFactory(this::createHttpClient);
    }

    private HttpClient createHttpClient() {
        var params = new SyncBasicHttpParams();
        setDefaultHttpParams(params);

        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, properties.getTimeout());
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, properties.getTimeout());

        var httpClient = new DefaultHttpClient(params);
        httpClient.setCredentialsProvider(new SystemDefaultCredentialsProvider());

        return httpClient;
    }

    private void checkRestAssuredConfig() {
        if (restAssuredConfig == null) {
            restAssuredConfig = createRestAssuredConfig();
        }
    }
}
