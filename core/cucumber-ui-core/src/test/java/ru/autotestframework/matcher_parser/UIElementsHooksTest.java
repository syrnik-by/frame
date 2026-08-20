package ru.autotestframework.matcher_parser;

import io.cucumber.java.Scenario;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.cucumber.hooks.UIElementsHooks;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

/**
 * Ui elements hooks test.
 */
@Tag("@UiCore")
class UIElementsHooksTest {
    private static Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));

    /**
     * Sets id in context test.
     */
    @Test
    void setIdInContextTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        Scenario scenario = Mockito.mock(Scenario.class);
        Mockito.when(scenario.getId()).thenReturn("123");
        List<IDriverSetter> driverSetters = new ArrayList<>();
        UIElementsHooks uiElementsHooks =
                new UIElementsHooks(context, driverSetters, driverContainer, new UiProperties());
        uiElementsHooks.setIdInContext(scenario);

        Assertions.assertEquals("123", context.get("case_id"));
    }

    /**
     * Sets driver test.
     */
    @Test
    void setDriverTest() {
        Context context = Mockito.mock(Context.class);
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        IDriverSetter iDriverSetter = Mockito.mock(IDriverSetter.class);
        List<IDriverSetter> driverSetters = List.of(iDriverSetter);
        Mockito.doThrow(Error.class).when(iDriverSetter).setDriver();
        UIElementsHooks uiElementsHooks =
                new UIElementsHooks(context, driverSetters, driverContainer, new UiProperties());

        Assertions.assertThrows(Error.class, () -> uiElementsHooks.setDriver(Mockito.mock(Scenario.class)));
    }

    /**
     * Tear down is failed test.
     */
    @Test
    void tearDownIsFailedTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        Context context = Mockito.mock(Context.class);
        Scenario scenario = Mockito.mock(Scenario.class);
        Mockito.when(scenario.getName()).thenReturn("name");
        Mockito.when(scenario.isFailed()).thenReturn(true);
        Driver driver = Mockito.mock(Driver.class);
        List<Driver> list = List.of(driver);
        ReflectionTestUtils.setField(driverContainer, "drivers", list);
        Mockito.doCallRealMethod().when(driverContainer).release();
        Mockito.when(driverContainer.getActiveDriver()).thenReturn(driver);
        Mockito.when(driverContainer.get()).thenThrow(Error.class);
        Mockito.doThrow(Error.class).when(driver).release();
        List<IDriverSetter> driverSetters = new ArrayList<>();
        UIElementsHooks uiElementsHooks =
                new UIElementsHooks(context, driverSetters, driverContainer, new UiProperties());

        Assertions.assertThrows(Error.class, () -> uiElementsHooks.tearDown(scenario));
        Mockito.verify(scenario, Mockito.times(1)).getName();
    }

    /**
     * Tear down sequence test.
     */
    @Test
    void tearDownSequenceTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        Driver driver = Mockito.mock(Driver.class);
        List<Driver> list = List.of(driver);
        ReflectionTestUtils.setField(driverContainer, "drivers", list);
        Context context = Mockito.mock(Context.class);
        Scenario scenario = Mockito.mock(Scenario.class);
        Mockito.when(scenario.isFailed()).thenReturn(true);

        Mockito.when(scenario.isFailed()).thenReturn(false);
        Mockito.doCallRealMethod().when(driverContainer).release();
        List<IDriverSetter> driverSetters = new ArrayList<>();
        UiProperties properties = new UiProperties();
        properties.setCloseOnFail(false);
        UIElementsHooks uiElementsHooks = new UIElementsHooks(context, driverSetters, driverContainer, properties);
        ReflectionTestUtils.setField(uiElementsHooks, "casesCounter", 2);

        uiElementsHooks.tearDown(scenario);

        Mockito.verify(driver, Mockito.times(1)).release();
    }

    /**
     * Tear down sequence one test test.
     */
    @Test
    void tearDownSequenceOneTestTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driverContainer.getActiveDriver()).thenReturn(driver);
        Context context = Mockito.mock(Context.class);
        Scenario scenario = Mockito.mock(Scenario.class);
        Mockito.when(scenario.isFailed()).thenReturn(true);
        List<IDriverSetter> driverSetters = new ArrayList<>();
        UiProperties properties = new UiProperties();
        properties.setCloseOnFail(false);
        UIElementsHooks uiElementsHooks = new UIElementsHooks(context, driverSetters, driverContainer, properties);
        ReflectionTestUtils.setField(uiElementsHooks, "casesCounter", 1);

        uiElementsHooks.tearDown(scenario);

        Mockito.verify(driverContainer, Mockito.times(0)).remove();
    }
}
