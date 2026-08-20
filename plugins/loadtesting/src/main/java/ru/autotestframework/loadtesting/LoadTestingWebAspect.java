package ru.autotestframework.loadtesting;

import static ru.autotestframework.util.StringUtil.trimQuotes;

import com.codeborne.selenide.WebDriverRunner;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.test_scope_info.StepInfoContainer;
import ru.autotestframework.web_elements.elements.Button;
import ru.psb.testit.client.api.WorkItemsApi;
import ru.psb.testit.client.invoker.ApiClient;
import ru.psb.testit.client.invoker.ApiException;
import ru.psb.testit.client.invoker.Configuration;
import ru.psb.testit.client.model.WorkItemModel;
import ru.psb.testit.properties.AppProperties;

@Slf4j
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class LoadTestingWebAspect {

    public static final String AUTH_STEP_TAG = "@Auth";
    public static final String LOAD_TESTING_TAG = "@LoadTesting";
    public static final String STEP_COOKIE_NAME = "TestStep";
    public static final String TEST_COOKIE_NAME = "TestName";
    public static final String TAG_COOKIE_NAME = "TestTags";
    // private static final String PRIVATE_TOKEN = "privateToken";
    private static WorkItemsApi workItemsClient;
    private static ThreadLocal<WorkItemModel> model = new ThreadLocal<>();
    private final StepInfoContainer testScopeInfoContainer;
    private final PageManager pageManager;

    @Getter
    @Value("${framework.variables.loadTestingEnabled:false}")
    private boolean isLoadTestingLoggingEnabled;

    @Getter
    @Value("${framework.variables.loadTestingTestIt:false}")
    private boolean isLoadTestingTestItEnabled;

    public static ApiClient createDefaultApiClient() {
        var prop = AppProperties.loadProperties();
        var defaultClient = Configuration.getDefaultApiClient();

        defaultClient.setBasePath(StringUtils.chop(prop.getProperty("url")));
        defaultClient.setApiKeyPrefix(StringUtils.capitalize("privateToken"));
        defaultClient.setApiKey(prop.getProperty("privateToken"));

        return defaultClient;
    }

    private static WorkItemsApi getWorkItemClient() {
        if (workItemsClient == null) {
            var apiClient = createDefaultApiClient();
            workItemsClient = new WorkItemsApi(apiClient);
        }
        return workItemsClient;
    }

    @SneakyThrows
    @Before("execution(public void ru.autotestframework.cucumber.step_defs.StepsUi.click(..)) ")
    public void clickButtonAspect() {
        if (isUiLoadTestCase()) {
            try {
                var elementName = testScopeInfoContainer.getStepArgs()[0].toString();
                if (pageManager.getCurrent().getElementByTitle(elementName) instanceof Button) {
                    addLoadTestIntegrationCookies();
                }
            } catch (Exception e) {
                log.error("testScopeInfoContainer return null value, probably some cycle steps used");
            }
        }
    }

    @SneakyThrows
    @Before("execution(public void ru.autotestframework.web_elements.step_defs.WebStepsUi.openUrl(..)) ")
    public void openUrlAspect() {
        addCookie();
    }

    @SneakyThrows
    @Before("execution(public void ru.autotestframework.cucumber.step_defs.StepsUi.setCurrentPage(..)) ")
    public void setPage() {
        addCookie();
    }

    @SneakyThrows
    @Before("@annotation(ru.autotestframework.loadtesting.Auth)")
    private void AuthStepAspect() {
        if ((isLoadTestingLoggingEnabled() || isLoadTestingTestItEnabled())
                && testScopeInfoContainer.getScenarioTags().contains(LOAD_TESTING_TAG)
                && WebDriverRunner.hasWebDriverStarted()) addLoadTestIntegrationCookies();
    }

    @SneakyThrows
    @Before("@annotation(ru.autotestframework.loadtesting.NeedLoadTestCookie)")
    private void businessStepAspect() {
        addCookie();
    }

    @SneakyThrows
    @Before("@annotation(ru.autotestframework.loadtesting.TestITLoadTestingStep)")
    private void businessStepAspectTestIT() {
        if (isLoadTestingTestItEnabled()
                && testScopeInfoContainer.getScenarioTags().contains(LOAD_TESTING_TAG)
                && WebDriverRunner.hasWebDriverStarted()) {
            try {
                //  addCookie(getCurrentStepTestITDescription());
            } catch (NullPointerException npe) {
                log.error("Ошибка нахождения связанного шага в TMS", npe);
            }
        }
    }

    private void addCookie() {
        if (isUiLoadTestCase()) {
            addLoadTestIntegrationCookies();
        }
    }

    private void addLoadTestIntegrationCookies() {
        String stepCookieValue = testScopeInfoContainer.getCurrentStep().getText();
        if (isAuthStep()) {
            stepCookieValue = AUTH_STEP_TAG + " " + stepCookieValue;
        }
        addCookie(stepCookieValue);
    }

    private void addCookie(String stepCookieValue) {
        var options = WebDriverRunner.getWebDriver().manage();
        var tags = String.join("_", testScopeInfoContainer.getScenarioTags());
        try {
            options.addCookie(new Cookie(STEP_COOKIE_NAME, stepCookieValue));
            options.addCookie(new Cookie(TEST_COOKIE_NAME, testScopeInfoContainer.getScenarioName()));
            options.addCookie(new Cookie(TAG_COOKIE_NAME, tags));
            log.info("LoadTesting integration cookies was added");
        } catch (Exception e) {
            log.error("Unable to add cookies within this step");
        }
    }

    private boolean isAuthStep() {
        return testScopeInfoContainer.getAnnotations().stream()
                .anyMatch(x -> x.contains(Auth.class.getCanonicalName()));
    }

    public boolean isUiLoadTestCase() {
        return isLoadTestingLoggingEnabled
                && testScopeInfoContainer.getScenarioTags().contains(LOAD_TESTING_TAG)
                && WebDriverRunner.hasWebDriverStarted();
    }

    public String getCurrentStepTestITDescription() {
        if (model.get() == null) {

            String workItemTag = testScopeInfoContainer.getScenarioTags().stream()
                    .filter(x -> x.toLowerCase(Locale.ROOT).contains("workitemids"))
                    .findAny()
                    .orElse("");
            var workItemId = workItemTag.substring(workItemTag.indexOf('=') + 1);
            try {
                model.set(getWorkItemClient().getWorkItemById(workItemId, null, null));

            } catch (ApiException ae) {
                log.error("loadTesting integration failed", ae);
            }
        }
        String stepNum = trimQuotes((String) testScopeInfoContainer.getStepArgs()[0]);
        String action =
                model.get().getSteps().get(Integer.parseInt(stepNum) - 1).getAction();
        return getFirstSentence(action);
    }

    private String getFirstSentence(String action) {
        var pattern = Pattern.compile("<p>(.+?)</p>");

        var htmlDescription = "";
        var matcher = pattern.matcher(action);
        if (matcher.find()) {
            htmlDescription = matcher.group();
        }
        var res = htmlDescription.replaceAll("<[^>]*>", "");
        var sentencePattern = Pattern.compile("(.+?)\\.");
        var sentenceMatcher = sentencePattern.matcher(res);
        if (sentenceMatcher.find()) {
            res = sentenceMatcher.group();
        }
        return res;
    }
}
