package ru.autotestframework.autoit_elements.driver_manager;

import java.io.IOException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.autoit_junit.driver_manager.drivers.AutoItXDriver;

/**
 * Auto it x driver test.
 */
@Tag("@AutoItElements")
class AutoItXDriverTest {

    /**
     * The Auto it x driver.
     */
    static AutoItXDriver autoItXDriver;

    /**
     * Sets up.
     */
    @BeforeAll
    static void setUp() {
        autoItXDriver = Mockito.mock(AutoItXDriver.class);
        Mockito.doCallRealMethod().when(autoItXDriver).start();
        Mockito.doCallRealMethod().when(autoItXDriver).quit();
        Mockito.doCallRealMethod().when(autoItXDriver).setNewApplication(Mockito.anyString());
    }

    /**
     * Start test.
     *
     * @throws IOException the io exception
     */
    @Test
    void startTest() throws IOException {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Mockito.doThrow(Error.class).when(processBuilder).start();
        ReflectionTestUtils.setField(autoItXDriver, "processBuilder", processBuilder);
        Assertions.assertThrows(Error.class, () -> autoItXDriver.start());
    }

    /**
     * Quit test.
     */
    @Test
    void quitTest() {
        Process process = Mockito.mock(Process.class);
        Mockito.doThrow(Error.class).when(process).destroy();
        ReflectionTestUtils.setField(autoItXDriver, "process", process);
        System.setProperty("autoit.app.close", "true");
        Assertions.assertThrows(Error.class, () -> autoItXDriver.quit());
    }

    /**
     * Sets new application test.
     */
    @Test
    void setNewApplicationTest() {
        Mockito.doThrow(Error.class).when(autoItXDriver).start();
        Mockito.doCallRealMethod().when(autoItXDriver).getApplication();
        Mockito.doCallRealMethod().when(autoItXDriver).getProcessBuilder();
        Mockito.doCallRealMethod().when(autoItXDriver).getProcess();
        Mockito.doCallRealMethod().when(autoItXDriver).setProcess(null);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        ReflectionTestUtils.setField(autoItXDriver, "processBuilder", processBuilder);
        Process process = Mockito.mock(Process.class);
        ReflectionTestUtils.setField(autoItXDriver, "process", process);
        Mockito.doAnswer((answer) -> {
                    autoItXDriver.setProcess(null);
                    return null;
                })
                .when(process)
                .destroy();
        Assertions.assertThrows(Error.class, () -> autoItXDriver.setNewApplication("test"));
        Assertions.assertEquals("test", autoItXDriver.getApplication());
        Assertions.assertNotEquals(autoItXDriver.getProcessBuilder(), processBuilder);
        Assertions.assertNull(autoItXDriver.getProcess());
    }
}
