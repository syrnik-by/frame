package ru.autotestframework.autoit_junit.driver_manager.drivers;

import static ru.autotestframework.Constants.SANITIZE_COMMAND_INJECTION;

import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.autotestframework.ui_core.exceptions.InitializationException;

/**
 * Auto it x driver.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AutoItXDriver extends RemoteWebDriver {

    private String application;
    private ProcessBuilder processBuilder;
    private Process process;

    /**
     * Instantiates a new Auto it x driver.
     *
     * @param app the app
     */
    public AutoItXDriver(String app) {
        if (!SANITIZE_COMMAND_INJECTION.matcher(app).matches()) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.application = app;
        this.processBuilder = new ProcessBuilder(app);
        this.start();
    }

    /**
     * Start Desktop Application.
     */
    public void start() {
        try {
            process = processBuilder.start();
        } catch (Exception e) {
            throw new InitializationException("The application '" + processBuilder + "' is not running");
        }
    }

    /**
     * Destroy Desktop Application.
     */
    @Override
    public void quit() {
        if (System.getProperty("autoit.app.close").equals("true")) {
            process.destroy();
        }
    }

    /**
     * Sets new application.
     *
     * @param app the app
     */
    public void setNewApplication(String app) {
        if (Objects.nonNull(process)) {
            process.destroy();
        }
        this.application = app;
        this.processBuilder = new ProcessBuilder(app);
        this.start();
    }
}
