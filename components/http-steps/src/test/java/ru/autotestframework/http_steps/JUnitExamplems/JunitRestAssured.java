package ru.autotestframework.http_steps.JUnitExamplems;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.http_steps.components.restassured.AllureRestAssuredFilter;
import ru.autotestframework.http_steps.components.restassured.RestAssuredConfiguration;
import ru.autotestframework.junit.BaseSpringJunitTest;

@Tag("@HttpSteps")
class JunitRestAssured extends BaseSpringJunitTest {

    @Autowired
    RestAssuredConfiguration configuration;

    ResponseSpecification respSpec = expect().statusCode(equalTo(200)).body("jsonPath", notNullValue());

    @Test
    void startUp() {

        RequestSpecification spec = configuration.getRequestSpecification();

        var resp = given().spec(spec)
                .filter(new AllureRestAssuredFilter())
                .baseUri("https://gitlab-01/")
                .formParams("form_1", "value 1")
                .expect()
                .spec(respSpec)
                .when()
                .get();

        //  resp.statusCode(200);
        resp.cookie("_gitlab_session");
    }
}
