package ru.autotestframework.http_steps.components;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class RequestContainer {

    private RequestSpecification specification;

    private Response response;

    private ValidatableResponse validatableResponse;
}
