package ru.autotestframework.tests.services;

import io.cucumber.java.Scenario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.helpers.ConsoleLogContainer;
import ru.autotestframework.web_elements.services.WebElementsHooks;

@Tag("@webElemElements")
class WebElementsHooksTest {

    @Test
    void afterTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        UiProperties uiProperties = new UiProperties();

        WebElementsHooks htmlElementsHooks = Mockito.mock(WebElementsHooks.class);
        Scenario scenario = Mockito.mock(Scenario.class);
        Mockito.doCallRealMethod().when(htmlElementsHooks).after(scenario);
        Mockito.when(scenario.isFailed()).thenReturn(true);
        ReflectionTestUtils.setField(htmlElementsHooks, "webDriversProperties", webDriversProperties);
        ReflectionTestUtils.setField(htmlElementsHooks, "uiProperties", uiProperties);
        webDriversProperties.setBrowserLoggingStrategy("on");
        uiProperties.setDriverInit(true);
        DriverContainer driverContainer = Mockito.spy(DriverContainer.class);
        ReflectionTestUtils.setField(htmlElementsHooks, "driverContainer", driverContainer);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driverContainer.get()).thenReturn(webDriver);
        WebDriver.Options options = Mockito.mock(WebDriver.Options.class);
        Mockito.when(webDriver.manage()).thenReturn(options);
        Logs logs = Mockito.mock(Logs.class);
        Mockito.when(options.logs()).thenReturn(logs);
        LogEntries logEntries = Mockito.mock(LogEntries.class);
        Mockito.when(logs.get(LogType.BROWSER)).thenReturn(logEntries);
        ArrayList<LogEntry> logEntryArrayList = new ArrayList<>();
        LogEntry logEntry1 = new LogEntry(Level.ALL, 1L, "test1");
        logEntryArrayList.add(logEntry1);
        Mockito.when(logEntries.getAll()).thenReturn(logEntryArrayList);
        ConsoleLogContainer consoleLogContainer = Mockito.mock(ConsoleLogContainer.class);
        ReflectionTestUtils.setField(htmlElementsHooks, "consoleLogContainer", consoleLogContainer);
        Mockito.when(consoleLogContainer.getConsoleLog()).thenReturn(Collections.singletonMap("key", "value"));
        htmlElementsHooks.after(scenario);
        Mockito.verify(scenario, Mockito.times(2)).attach(Mockito.anyString(), Mockito.any(), Mockito.any());
    }
}
