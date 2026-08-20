package ru.autotestframework.loadtesting;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.nio.file.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import ru.autotestframework.configuration.ProxyProperties;
import ru.autotestframework.test_scope_info.StepInfoContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.proxy.ProxyContainer;

@Slf4j
@RequiredArgsConstructor
public class Hooks {

    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    private static boolean enabled = false;
    private final DriverContainerImpl driverContainer;
    private final LoadTestingWebAspect aspect;
    private final StepInfoContainer stepInfoContainer;
    private final ProxyProperties properties;

    @Value("${framework.variables.loadTestingArtifactDir:harDir}")
    private String artifactDir;

    @Getter
    @Value("${framework.variables.loadTestingFiltering:false}")
    private boolean filtered;

    @Getter
    @Value("${framework.variables.createHarOnFail:false}")
    private boolean isCreateOnFail;

    @AfterAll
    public static void destroy() {
        if (enabled) {
            ProxyContainer.getInstance().getProxy().stop();
        }
    }

    @After(order = Integer.MAX_VALUE - 1000)
    public void testEnd(Scenario scenario) {
        String message = scenario.isFailed() ? FAIL : SUCCESS;
        if (aspect.isUiLoadTestCase()) {
            var options = driverContainer.get().manage();
            options.addCookie(new Cookie(LoadTestingWebAspect.STEP_COOKIE_NAME, message));
            driverContainer.get().navigate().refresh();
        }
    }

    @Before(value = LoadTestingWebAspect.LOAD_TESTING_TAG, order = Integer.MAX_VALUE - 10001)
    public void initNewArtifact() {
        ProxyContainer.getInstance().getProxy().newHar();
    }

    @After(value = LoadTestingWebAspect.LOAD_TESTING_TAG, order = Integer.MAX_VALUE - 10001)
    public void createArtifact(Scenario scenario) throws Exception {
        enabled = properties.isEnabled();
        if (properties.isEnabled() && (!scenario.isFailed() || isCreateOnFail())) {
            var fName = Path.of(
                            artifactDir,
                            stepInfoContainer
                                    .getScenarioName()
                                    .replace(" ", "_")
                                    .replace("/", "_")
                                    .replace(".", "_"))
                    .toString();

            ProxyContainer.getInstance().createArtifacts(fName);
        }
        ProxyContainer.getInstance().getHar();
    }
}
