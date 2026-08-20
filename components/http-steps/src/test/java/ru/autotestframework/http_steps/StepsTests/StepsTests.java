package ru.autotestframework.http_steps.StepsTests;

import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.FLOAT_AND_DOUBLE;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.http_steps.HttpSteps;
import ru.autotestframework.http_steps.components.RequestContainer;
import ru.autotestframework.http_steps.components.restassured.RestAssuredConfiguration;
import ru.autotestframework.http_steps.components.restassured.RestAssuredProperties;
import ru.autotestframework.http_steps.components.restassured.SpecificationConfigurer;

@Tag("@HttpSteps")
class StepsTests {

    private final FrameworkProperties frameworkProperties = new FrameworkProperties();
    private final FileLoaderImpl fileLoader =
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), frameworkProperties);
    private final RestAssuredProperties raProperties = new RestAssuredProperties();
    private final List<SpecificationConfigurer> customSpecConfigurators = new ArrayList<>();
    private final RestAssuredConfiguration raConfig =
            new RestAssuredConfiguration(frameworkProperties, raProperties, customSpecConfigurators);
    private final HttpSteps steps = new HttpSteps(
            new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables())),
            fileLoader,
            raConfig,
            new RequestContainer());

    {
        raProperties.setNumberReturnType(FLOAT_AND_DOUBLE);
    }

    @Test
    void checkEndpointPathValidation() {
        Assertions.assertDoesNotThrow(() -> steps.setUrl("https://yandex.ru/"));
        Assertions.assertThrows(AutotestException.class, () -> steps.setEndpoint("Personal/{Loans/"));
    }
}
