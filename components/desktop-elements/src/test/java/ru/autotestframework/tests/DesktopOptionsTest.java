package ru.autotestframework.tests;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopOptions;

@Tag("@DesktopElements")
class DesktopOptionsTest {

    static DesktopOptions desktopOptions;
    static String applicationPath;
    static String arguments;
    static Integer launchDelay;
    static Integer processFindTimeOut;
    static String injectionDllType;
    static Integer responseTimeout;
    static String processName;

    @BeforeAll
    static void setUp() {
        desktopOptions = new DesktopOptions();
        applicationPath = "applicationPath";
        arguments = "arguments";
        launchDelay = 777;
        processFindTimeOut = 777;
        injectionDllType = "injectionDllType";
        responseTimeout = 777;
        processName = "processName";
    }

    @Test
    void setApplicationPathTest() {
        desktopOptions.setApplicationPath(applicationPath);
        String applicationPathOption = (String) ReflectionTestUtils.getField(desktopOptions, "applicationPath");
        Assertions.assertEquals(applicationPath, applicationPathOption);
    }

    @Test
    void setArgumentsTest() {
        desktopOptions.setArguments(arguments);
        String argumentsOption = (String) ReflectionTestUtils.getField(desktopOptions, "arguments");
        Assertions.assertEquals(arguments, argumentsOption);
    }

    @Test
    void setConnectToRunningAppTest() {
        desktopOptions.setConnectToRunningApp(true);
        Boolean connectToRunningAppOption =
                (Boolean) ReflectionTestUtils.getField(desktopOptions, "connectToRunningApp");
        Assertions.assertEquals(Boolean.TRUE, connectToRunningAppOption);
    }

    @Test
    void setLaunchDelayTest() {
        desktopOptions.setLaunchDelay(launchDelay);
        Integer launchDelayOption = (Integer) ReflectionTestUtils.getField(desktopOptions, "launchDelay");
        Assertions.assertEquals(launchDelayOption, launchDelay);
    }

    @Test
    void setProcessFindTimeOutTest() {
        desktopOptions.setProcessFindTimeOut(processFindTimeOut);
        Integer processFindTimeOutOption = (Integer) ReflectionTestUtils.getField(desktopOptions, "processFindTimeOut");
        Assertions.assertEquals(processFindTimeOutOption, processFindTimeOut);
    }

    @Test
    void setProcessNameTest() {
        desktopOptions.setProcessName(processName);
        String processNameOption = (String) ReflectionTestUtils.getField(desktopOptions, "processName");
        Assertions.assertEquals(processName, processNameOption);
    }

    @Test
    void setInjectionActivateTest() {
        desktopOptions.setInjectionActivate(true);
        Boolean injectionActivateOption = (Boolean) ReflectionTestUtils.getField(desktopOptions, "injectionActivate");
        Assertions.assertEquals(Boolean.TRUE, injectionActivateOption);
    }

    @Test
    void setInjectionDllTypeTest() {
        desktopOptions.setInjectionDllType(injectionDllType);
        String injectionDllTypeOption = (String) ReflectionTestUtils.getField(desktopOptions, "injectionDllType");
        Assertions.assertEquals(injectionDllType, injectionDllTypeOption);
    }

    @Test
    void setResponseTimeoutTest() {
        desktopOptions.setResponseTimeout(responseTimeout);
        Integer responseTimeoutOption = (Integer) ReflectionTestUtils.getField(desktopOptions, "responseTimeout");
        Assertions.assertEquals(responseTimeoutOption, responseTimeout);
    }

    @Test
    void toCapabilitiesTest() {
        desktopOptions.setInjectionDllType(injectionDllType);
        desktopOptions.setResponseTimeout(responseTimeout);
        desktopOptions.setInjectionActivate(true);
        desktopOptions.setProcessName(processName);
        desktopOptions.setLaunchDelay(launchDelay);
        desktopOptions.setProcessFindTimeOut(processFindTimeOut);
        desktopOptions.setConnectToRunningApp(true);
        desktopOptions.setArguments(arguments);
        desktopOptions.setApplicationPath(applicationPath);
        Capabilities capabilities = desktopOptions.toCapabilities();
        HashMap capabilitiesMap = (HashMap) capabilities.asMap().get("flanium:capabilities");
        Assertions.assertNotNull(capabilitiesMap);
        Assertions.assertEquals(capabilitiesMap.get("app"), applicationPath);
        Assertions.assertEquals(capabilitiesMap.get("args"), arguments);
        Assertions.assertTrue((Boolean) capabilitiesMap.get("connectToRunningApp"));
        Assertions.assertEquals(capabilitiesMap.get("launchDelay"), launchDelay);
        Assertions.assertEquals(capabilitiesMap.get("processFindTimeOut"), processFindTimeOut);
        Assertions.assertEquals(capabilitiesMap.get("processName"), processName);
        Assertions.assertTrue((Boolean) capabilitiesMap.get("injectionActivate"));
        Assertions.assertEquals(capabilitiesMap.get("injectionDllType"), injectionDllType);
        Assertions.assertEquals(capabilitiesMap.get("responseTimeout"), responseTimeout);
    }
}
