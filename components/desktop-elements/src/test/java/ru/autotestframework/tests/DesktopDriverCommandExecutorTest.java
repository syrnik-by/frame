package ru.autotestframework.tests;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.remote.Command;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriverCommandExecutor;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriverService;

@Tag("@DesktopElements")
class DesktopDriverCommandExecutorTest {

    static DesktopDriverCommandExecutor desktopDriverCommandExecutor;
    static DesktopDriverService desktopDriverService;
    static Command command;

    @BeforeAll
    static void setUp() throws IOException {
        desktopDriverCommandExecutor = Mockito.mock(DesktopDriverCommandExecutor.class);
        desktopDriverService = Mockito.mock(DesktopDriverService.class);
        command = Mockito.mock(Command.class);
        Mockito.when(desktopDriverCommandExecutor.execute(command)).thenCallRealMethod();
    }

    @Test
    void executeWhenServiceIsNotNullAndNewSessionTest() throws IOException {
        ReflectionTestUtils.setField(desktopDriverCommandExecutor, "service", desktopDriverService);
        Mockito.when(command.getName()).thenReturn("newSession");
        Assertions.assertThrows(NullPointerException.class, () -> desktopDriverCommandExecutor.execute(command));
        Mockito.verify(desktopDriverService, Mockito.times(1)).start();
    }

    @Test
    void executeWhenServiceIsNotNullAndQuitTest() throws IOException {
        ReflectionTestUtils.setField(desktopDriverCommandExecutor, "service", desktopDriverService);
        Mockito.when(command.getName()).thenReturn("quit");
        desktopDriverCommandExecutor.execute(command);
        Mockito.verify(desktopDriverService, Mockito.times(1)).stop();
    }
}
