package ru.autotestframework.tests.services;

import java.util.ArrayList;
import java.util.logging.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.helpers.ConsoleLogContainer;
import ru.autotestframework.web_elements.services.ConsoleLogService;

@Tag("@webElemElements")
class ConsoleLogServiceTest {

    @Test
    void provideLogsTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();

        ConsoleLogContainer consoleLogContainer = Mockito.spy(ConsoleLogContainer.class);
        DriverContainerImpl driverContainer = Mockito.mock(DriverContainerImpl.class);
        ConsoleLogService consoleLogService =
                new ConsoleLogService(webDriversProperties, driverContainer, consoleLogContainer);
        MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint =
                Mockito.mock(MethodInvocationProceedingJoinPoint.class);
        webDriversProperties.setBrowserLoggingStrategy("page");
        Mockito.when(methodInvocationProceedingJoinPoint.getArgs()).thenReturn(new String[] {"Name"});
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
        LogEntry logEntry2 = new LogEntry(Level.ALL, 2L, "test2");
        logEntryArrayList.add(logEntry2);
        Mockito.when(logEntries.getAll()).thenReturn(logEntryArrayList);
        consoleLogService.provideLogs(methodInvocationProceedingJoinPoint);
        Assertions.assertEquals(
                consoleLogContainer.getConsoleLog().get("Name"),
                "'Name' page loaded. Console log presented further: \n" + "[1970-01-01T00:00:00.001Z] [ALL] test1\n"
                        + "[1970-01-01T00:00:00.002Z] [ALL] test2");
        Assertions.assertEquals(1, consoleLogContainer.getConsoleLog().size());
    }
}
