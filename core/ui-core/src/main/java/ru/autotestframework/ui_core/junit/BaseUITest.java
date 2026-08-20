package ru.autotestframework.ui_core.junit;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.junit.BaseSpringJunitTest;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;

/**
 * Abstract common class to work with UI modules
 */
@ContextConfiguration
public abstract class BaseUITest extends BaseSpringJunitTest {

    /**
     * The Driver container.
     */
    @Autowired
    protected DriverContainerImpl driverContainer;

    /**
     * The Ui properties.
     */
    @Autowired
    UiProperties uiProperties;

    /**
     * The Default variables.
     */
    @Autowired
    protected FrameworkDefaultVariables defaultVariables;

    /**
     * The Driver setters.
     */
    @Autowired
    protected List<IDriverSetter> driverSetters;

    /**
     * Start up.
     */
    @BeforeEach
    public void startUp() {
        driverSetters.forEach(IDriverSetter::setDriver);
        // TODO CHANGE
        if (driverContainer.get() != null) {
            WebDriverRunner.setWebDriver(driverContainer.get());
        } else {
            Selenide.open();
        }
        Configuration.timeout = uiProperties.getTimeout() * 1000L;
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        driverContainer.release();
    }
}
