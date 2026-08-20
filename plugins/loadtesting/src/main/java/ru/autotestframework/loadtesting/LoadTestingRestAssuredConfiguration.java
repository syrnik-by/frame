package ru.autotestframework.loadtesting;

import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.autotestframework.http_steps.components.restassured.SpecificationConfigurer;
import ru.autotestframework.test_scope_info.StepInfoContainer;

@Component
@RequiredArgsConstructor
public class LoadTestingRestAssuredConfiguration implements SpecificationConfigurer {
    private final StepInfoContainer testScopeInfoContainer;

    @Value("${framework.variables.loadTestingEnabled:true}")
    private boolean isLoadTestingLoggingEnabled;

    @Override
    public RequestSpecification configure(RequestSpecification specification) {
        if (isLoadTestingLoggingEnabled
                && testScopeInfoContainer.getScenarioTags().contains(LoadTestingWebAspect.LOAD_TESTING_TAG)) {
            var tags = String.join("_", testScopeInfoContainer.getScenarioTags());
            String stepCookieValue = testScopeInfoContainer.getCurrentStep().getText();
            if (testScopeInfoContainer.getAnnotations().contains(LoadTestingWebAspect.AUTH_STEP_TAG)) {
                stepCookieValue = LoadTestingWebAspect.AUTH_STEP_TAG + " " + stepCookieValue;
            }
            var stepCookie = new Cookie.Builder(LoadTestingWebAspect.STEP_COOKIE_NAME, stepCookieValue).build();
            var nameCookie = new Cookie.Builder(
                            LoadTestingWebAspect.TEST_COOKIE_NAME, testScopeInfoContainer.getScenarioName())
                    .build();
            var tagsCookie = new Cookie.Builder(LoadTestingWebAspect.TAG_COOKIE_NAME, tags).build();

            specification = specification.cookies(new Cookies(tagsCookie, nameCookie, stepCookie));
        }
        return specification;
    }
}
