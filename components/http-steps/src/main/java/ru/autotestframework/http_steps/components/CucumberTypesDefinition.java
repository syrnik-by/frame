package ru.autotestframework.http_steps.components;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.restassured.http.Header;
import io.restassured.http.Method;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.Constants;
import ru.autotestframework.core.PlaceholderResolver;

@Slf4j
@RequiredArgsConstructor
public class CucumberTypesDefinition {

    private final PlaceholderResolver placeholderResolver;

    /**
     * returns http method by name
     * @param value
     * @return
     */
    @ParameterType(name = "http_method", value = Constants.HTTP_METHOD_REGEX)
    public Method http_method(final String value) {
        return Method.valueOf(value);
    }

    /**
     * resolves headers
     * @param header
     * @return
     */
    @DataTableType
    public Header parseHeader(final List<String> header) {
        var name = placeholderResolver.resolve(header.get(0));
        var value = placeholderResolver.resolve(header.get(1));
        return new Header(name, value);
    }
}
