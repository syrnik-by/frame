package ru.autotestframework.cucumber.hooks;

import com.codeborne.selenide.WebDriverRunner;
import com.google.common.net.MediaType;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.typified_elements.enums.ImageFormat;

/**
 * Ui elements hooks.
 */
@Slf4j
@RequiredArgsConstructor
public class UIElementsHooks {
    private static int casesCounter = 0;
    private final Context context;
    private final List<IDriverSetter> driverSetters;
    private final DriverContainer driverContainer;
    private final UiProperties properties;

    /**
     * Sets case id in context.
     *
     * @param scenario the scenario
     */
    @Before(order = Integer.MIN_VALUE)
    public void setIdInContext(final Scenario scenario) {
        context.set("case_id", scenario.getId());
    }

    /**
     * Sets driver.
     *
     * @param scenario the scenario
     */
    @Before(order = Integer.MIN_VALUE + 100000)
    public void setDriver(Scenario scenario) {
        // TODO DESKTOP CUCUMBER
        if (!(properties.getDriverInit() || scenario.getSourceTagNames().contains("@UI"))) {
            log.error("Driver initialization is disabled");
        } else {
            driverSetters.forEach(IDriverSetter::setDriver);
            casesCounter += 1;
            if (driverContainer.get() != null) {
                WebDriverRunner.setWebDriver(driverContainer.get());
            }
        }
    }

    /**
     * Tear down.
     *
     * @param scenario the scenario
     */
    @After
    public void tearDown(final Scenario scenario) {
        if (scenario.isFailed() && driverContainer.getActiveDriver() != null) {
            try {
                takeScreenshot(scenario);
            } catch (Exception e) {
                log.error("Error happened while adding screenshot:", e);
            }
            if (!properties.isCloseOnFail()) {
                if (casesCounter == 1) {
                    return;
                } else {
                    log.error(
                            "Change framework.ui.closeOnFail property in case of errors. Its probably incompatible with other settings: driver type or parallelization");
                }
            }
        }
        driverContainer.release();
    }

    private void takeScreenshot(final Scenario scenario) {
        String screenshotName = scenario.getName().replace(" ", "_").concat(".").concat(ImageFormat.PNG.toString());
        scenario.attach(getScreenshotBytes(), MediaType.PNG.toString(), screenshotName);
    }

    private byte[] getScreenshotBytes() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
