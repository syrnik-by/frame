package ru.autotestframework.http_steps.tests;

import static ru.autotestframework.Constants.*;

import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.Filter;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.http_steps.components.restassured.RestAssuredConfiguration;
import ru.autotestframework.http_steps.components.restassured.RestAssuredProperties;
import ru.autotestframework.http_steps.components.restassured.SpecificationConfigurer;

@Tag("@HttpSteps")
class RestAssuredConfigurationTest {

    static RestAssuredConfiguration restAssuredConfiguration;
    static FrameworkProperties frameworkProperties = Mockito.mock(FrameworkProperties.class);
    static RestAssuredProperties restAssuredProperties = Mockito.mock(RestAssuredProperties.class);
    static TestRestAssuredConfiguration testRestAssuredConfiguration = new TestRestAssuredConfiguration();

    static class TestRestAssuredConfiguration implements SpecificationConfigurer {
        @Override
        public RequestSpecification configure(RequestSpecification specification) {
            specification = specification.basePath("basePath");
            return specification;
        }
    }

    @BeforeAll
    static void mockProperties() {
        Mockito.when(restAssuredProperties.getNumberReturnType())
                .thenReturn(JsonPathConfig.NumberReturnType.FLOAT_AND_DOUBLE);
    }

    @BeforeEach
    void createNewConfiguration() {
        restAssuredConfiguration = new RestAssuredConfiguration(
                frameworkProperties, restAssuredProperties, List.of(testRestAssuredConfiguration));
    }

    @Test
    void createRestAssuredConfigTest() {
        RestAssuredConfig restAssuredConfig = restAssuredConfiguration.createRestAssuredConfig();
        Assertions.assertTrue(restAssuredConfig.getHttpClientConfig().isUserConfigured());
        Assertions.assertTrue(restAssuredConfig.getSSLConfig().isUserConfigured());
        Assertions.assertTrue(restAssuredConfig.getEncoderConfig().isUserConfigured());
        Assertions.assertTrue(restAssuredConfig.getJsonConfig().isUserConfigured());
    }

    @Test
    void createRestAssuredConfigEncoderTest() {
        EncoderConfig encoderConfig = Mockito.mock(EncoderConfig.class);
        restAssuredConfiguration.createRestAssuredConfig(encoderConfig);
        Assertions.assertEquals(
                restAssuredConfiguration.createRestAssuredConfig().getEncoderConfig(), encoderConfig);
    }

    @Test
    void createRestAssuredConfigSSLTest() {
        SSLConfig sslConfig = Mockito.mock(SSLConfig.class);
        restAssuredConfiguration.createRestAssuredConfig(sslConfig);
        Assertions.assertEquals(
                restAssuredConfiguration.createRestAssuredConfig().getSSLConfig(), sslConfig);
    }

    @Test
    void createRestAssuredConfigHttpClientConfigTest() {
        HttpClientConfig httpClientConfig = Mockito.mock(HttpClientConfig.class);
        restAssuredConfiguration.createRestAssuredConfig(httpClientConfig);
        Assertions.assertEquals(
                restAssuredConfiguration.createRestAssuredConfig().getHttpClientConfig(), httpClientConfig);
    }

    @Test
    void configureLoggingFiltersTest() {
        Mockito.when(restAssuredProperties.isLogsEnabled()).thenReturn(true);
        Mockito.when(restAssuredProperties.isPrettyPrintEnabled()).thenReturn(true);
        RequestSpecificationImpl requestSpecification = Mockito.mock(RequestSpecificationImpl.class);
        Filter filter = Mockito.mock(Filter.class);
        Mockito.doCallRealMethod().when(requestSpecification).noFilters();
        Mockito.doCallRealMethod().when(requestSpecification).getDefinedFilters();
        Mockito.when(requestSpecification.filter(Mockito.any(Filter.class))).thenCallRealMethod();
        ReflectionTestUtils.setField(requestSpecification, "filters", new ArrayList<Filter>());
        requestSpecification.filter(filter);
        restAssuredConfiguration.configureLoggingFilters(requestSpecification);
        List<Filter> definedFilters = requestSpecification.getDefinedFilters();
        Assertions.assertEquals(3, definedFilters.size());
    }

    @Test
    void applyProxyTest() {
        RequestSpecificationImpl requestSpecification = Mockito.mock(RequestSpecificationImpl.class);
        Mockito.when(requestSpecification.proxy(Mockito.any(ProxySpecification.class)))
                .thenCallRealMethod();
        System.setProperty(HTTP_PROXY_PORT, "100");
        System.setProperty(HTTP_PROXY_HOST, "host");
        System.setProperty(HTTPS_PROXY_USER, "user");

        RequestSpecificationImpl applyProxy =
                (RequestSpecificationImpl) restAssuredConfiguration.applyProxy(requestSpecification);
        Mockito.when(applyProxy.getProxySpecification()).thenCallRealMethod();
        Assertions.assertEquals(100, applyProxy.getProxySpecification().getPort());
        Assertions.assertEquals("host", applyProxy.getProxySpecification().getHost());
    }

    @Test
    void getRequestSpecificationTest() {
        System.setProperty(HTTP_PROXY_PORT, "100");
        System.setProperty(HTTP_PROXY_HOST, "host");
        System.setProperty(HTTPS_PROXY_USER, "user");
        RequestSpecificationImpl requestSpecification =
                (RequestSpecificationImpl) restAssuredConfiguration.getRequestSpecification();
        Assertions.assertEquals("basePath", requestSpecification.getBasePath());
        Assertions.assertNotNull(requestSpecification.getProxySpecification());
    }
}
