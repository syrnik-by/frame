package ru.autotestframework.screen_elements.driver_manager.drivers;

import static ru.autotestframework.Constants.SANITIZE_COMMAND_INJECTION;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sikuli.script.App;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import ru.autotestframework.ui_core.exceptions.InitializationException;

/**
 * Sikuli driver.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SikuliDriver extends RemoteWebDriver {

    private final App application;
    private final ProcessBuilder processBuilder;
    private Process process;

    /**
     * Instantiates a new Sikuli driver.
     *
     * @param app the app
     */
    public SikuliDriver(final String app) {
        if (!SANITIZE_COMMAND_INJECTION.matcher(app).matches()) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.application = new App(app);
        this.processBuilder = new ProcessBuilder(app);
        this.start();
    }

    private static String imgToBase64String(final RenderedImage img, final String formatName) {
        var os = new ByteArrayOutputStream();
        try (OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(img, formatName, b64os);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return os.toString();
    }

    /**
     * Start Desktop Application.
     */
    public void start() {
        try {
            process = processBuilder.start();
        } catch (Exception e) {
            throw new InitializationException("The application '" + application.getName() + "' is not running", e);
        }
    }

    /**
     * Get screenshot by region.
     */
    @SneakyThrows
    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) {
        var region = application.waitForWindow();
        var screen = new Screen();
        ScreenImage str = screen.capture(region);
        return target.convertFromBase64Png(imgToBase64String(str.getImage(), "png"));
    }

    /**
     * Destroy Desktop Application.
     */
    @Override
    public void quit() {
        process.destroy();
    }
}
