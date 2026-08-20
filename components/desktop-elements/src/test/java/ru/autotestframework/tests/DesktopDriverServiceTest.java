package ru.autotestframework.tests;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Capabilities;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriverService;

@Tag("@DesktopElements")
class DesktopDriverServiceTest {

    @Test
    void scoreTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.score(Mockito.any(Capabilities.class))).thenCallRealMethod();
        int score = builder.score(Mockito.mock(Capabilities.class));
        Assertions.assertEquals(0, score);
    }

    @Test
    void usingDriverExecutableTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.usingDriverExecutable(Mockito.any(File.class))).thenCallRealMethod();
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.isFile()).thenReturn(true);
        Mockito.when(file.canExecute()).thenReturn(true);
        builder.usingDriverExecutable(file);
        File exe = (File) ReflectionTestUtils.getField(builder, "exe");
        Assertions.assertEquals(exe, file);
    }

    @Test
    void usingPortTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.usingPort(Mockito.anyInt())).thenCallRealMethod();
        builder.usingPort(777);
        Integer port = (Integer) ReflectionTestUtils.getField(builder, "port");
        Assertions.assertEquals(777, port);
    }

    @Test
    void withVerboseTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.withVerbose(Mockito.anyBoolean())).thenCallRealMethod();
        builder.withVerbose(true);
        Boolean verbose = (Boolean) ReflectionTestUtils.getField(builder, "verbose");
        Assertions.assertTrue(verbose);
    }

    @Test
    void withSilentTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.withSilent(Mockito.anyBoolean())).thenCallRealMethod();
        builder.withSilent(true);
        Boolean silent = (Boolean) ReflectionTestUtils.getField(builder, "silent");
        Assertions.assertTrue(silent);
    }

    @Test
    void withTimeoutTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        Mockito.when(builder.withTimeout(Mockito.any(Duration.class))).thenCallRealMethod();
        builder.withTimeout(Duration.ZERO);
        Duration timeout = (Duration) ReflectionTestUtils.getField(builder, "timeout");
        Assertions.assertEquals(Duration.ZERO, timeout);
    }

    @Test
    void buildDesktopServiceTest() {
        DesktopDriverService.Builder builder = Mockito.mock(DesktopDriverService.Builder.class);
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.isFile()).thenReturn(true);
        Mockito.when(file.canExecute()).thenReturn(true);
        Mockito.when(builder.buildDesktopService()).thenCallRealMethod();
        Mockito.when(builder.usingPort(Mockito.anyInt())).thenCallRealMethod();
        Mockito.when(builder.withTimeout(Mockito.any(Duration.class))).thenCallRealMethod();
        Mockito.when(builder.usingDriverExecutable(Mockito.any(File.class))).thenCallRealMethod();
        builder.usingPort(777).withTimeout(Duration.ZERO).usingDriverExecutable(file);
        DesktopDriverService desktopDriverService = builder.buildDesktopService();
        URL url = (URL) ReflectionTestUtils.getField(desktopDriverService, "url");
        Duration duration = (Duration) ReflectionTestUtils.getField(desktopDriverService, "timeout");
        Assertions.assertNotNull(url);
        Assertions.assertEquals("localhost:777", url.getAuthority());
        Assertions.assertEquals(Duration.ZERO, duration);
    }
}
