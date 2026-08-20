package ru.autotestframework.http_steps.components.restassured;

import io.restassured.specification.RequestSpecification;

/**
 * Интерфейс позволяющий дополнительно конфигурировать спецификацию RestAssured.
 */
public interface SpecificationConfigurer {

    RequestSpecification configure(RequestSpecification specification);
}
