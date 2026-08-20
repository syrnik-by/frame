package ru.autotestframework.screen_elements.driver_manager.drivers;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sikuli.script.App;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.exceptions.InitializationException;

/**
 * Sikuli driver test.
 */
@Tag("@ScreenElements")
class SikuliDriverTest {

    /**
     * The Sikuli driver.
     */
    static SikuliDriver sikuliDriver;

    /**
     * Sets up.
     */
    @BeforeAll
    static void setUp() {
        sikuliDriver = Mockito.mock(SikuliDriver.class);
        Mockito.doCallRealMethod().when(sikuliDriver).start();
        Mockito.doCallRealMethod().when(sikuliDriver).quit();
    }

    /**
     * Start positive test.
     *
     * @throws IOException the io exception
     */
    @Test
    void startPositiveTest() throws IOException {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Mockito.doThrow(Error.class).when(processBuilder).start();
        ReflectionTestUtils.setField(sikuliDriver, "processBuilder", processBuilder);
        Assertions.assertThrows(Error.class, () -> sikuliDriver.start());
    }

    /**
     * Start negative test.
     *
     * @throws IOException the io exception
     */
    @Test
    void startNegativeTest() throws IOException {
        App application = Mockito.mock(App.class);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Mockito.doThrow(IOException.class).when(processBuilder).start();
        ReflectionTestUtils.setField(sikuliDriver, "application", application);
        ReflectionTestUtils.setField(sikuliDriver, "processBuilder", processBuilder);
        Assertions.assertThrows(InitializationException.class, () -> sikuliDriver.start());
    }

    /**
     * Quit positive test.
     */
    @Test
    void quitPositiveTest() {
        Process process = Mockito.mock(Process.class);
        Mockito.doThrow(Error.class).when(process).destroy();
        ReflectionTestUtils.setField(sikuliDriver, "process", process);
        System.setProperty("autoit.app.close", "true");
        Assertions.assertThrows(Error.class, () -> sikuliDriver.quit());
    }
}
