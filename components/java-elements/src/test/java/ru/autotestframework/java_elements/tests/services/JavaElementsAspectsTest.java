package ru.autotestframework.java_elements.tests.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.java_elements.pages.AspectTestPage;
import ru.autotestframework.java_elements.pages.LoginPage;
import ru.autotestframework.java_elements.services.JavaElementsAspects;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@Tag("@JavaElements")
public class JavaElementsAspectsTest {

    @Test
    void waitWindowAspectWithSpecificTitleTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        UiProperties uiProperties = Mockito.mock(UiProperties.class);
        PageManager pageManager = Mockito.mock(PageManager.class);
        Driver driver = Mockito.mock(Driver.class);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getDriver()).thenReturn(webDriver);
        Mockito.when(driver.getPath()).thenReturn("driverPath");
        driverContainer.add(driver);
        AspectTestPage aspectTestPage = new AspectTestPage();
        Mockito.when(pageManager.getCurrent()).thenReturn(aspectTestPage);
        Set<String> windowHandles = new HashSet<>(Arrays.asList("1", "2", "3", "4"));
        Mockito.when(webDriver.getWindowHandles()).thenReturn(windowHandles);
        WebDriver.TargetLocator targetLocator = Mockito.mock(WebDriver.TargetLocator.class);
        Mockito.when(webDriver.switchTo()).thenReturn(targetLocator);
        Mockito.when(targetLocator.window("4")).thenThrow(Error.class);
        JavaElementsAspects javaElementsAspects = new JavaElementsAspects(driverContainer, pageManager, uiProperties);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Assertions.assertThrows(NullPointerException.class, () -> javaElementsAspects.waitWindowAspect(joinPoint));
    }

    @Test
    void waitWindowAspectTest() {
        DriverContainer driverContainer = Mockito.mock(DriverContainer.class);
        UiProperties uiProperties = Mockito.mock(UiProperties.class);
        PageManager pageManager = Mockito.mock(PageManager.class);
        Driver driver = Mockito.mock(Driver.class);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getDriver()).thenReturn(webDriver);
        Mockito.when(driver.getPath()).thenReturn("driverPath");
        driverContainer.add(driver);
        LoginPage loginPage = new LoginPage();
        Mockito.when(pageManager.getCurrent()).thenReturn(loginPage);
        WebDriver.TargetLocator targetLocator = Mockito.mock(WebDriver.TargetLocator.class);
        Mockito.when(webDriver.switchTo()).thenReturn(targetLocator);
        Mockito.when(targetLocator.window("Login")).thenThrow(Error.class);
        JavaElementsAspects javaElementsAspects = new JavaElementsAspects(driverContainer, pageManager, uiProperties);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Assertions.assertThrows(NullPointerException.class, () -> javaElementsAspects.waitWindowAspect(joinPoint));
    }
}
