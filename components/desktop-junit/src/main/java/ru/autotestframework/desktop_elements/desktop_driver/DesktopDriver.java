package ru.autotestframework.desktop_elements.desktop_driver;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.*;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.desktop_elements.driver_manager.drivers.DriverDesktop;
import ru.autotestframework.desktop_elements.elements.WebElementExtensions;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.typified_elements.enums.KeyCombination;
import ru.autotestframework.ui_core.typified_elements.enums.KeyboardLayout;

@Slf4j
public class DesktopDriver extends RemoteWebDriver {

    private static final String DRAG_AND_DROP = "dragAndDrop";
    private static final String GET_ACTIVE_WINDOW = "getActiveWindow";
    private static final String SEND_CHARS_TO_ACTIVE_ELEMENT = "sendCharsToActiveElement";
    private static final String GET_KEYBOARD_LAYOUT = "getKeyboardLayout";
    private static final String SET_KEYBOARD_LAYOUT = "setKeyboardLayout";
    private static final String GET_CLIPBOARD_TEXT = "getClipboardText";
    private static final String SET_CLIPBOARD_TEXT = "setClipboardText";
    private static final String KEY_COMBINATION = "keyCombination";
    private static final String SET_ROOT = "setRootElement";
    private static final String KILL_PROCESSES = "killProcesses";
    private static final String START_APP = "startApp";

    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String S_TIMEOUT = "timeout";
    private static final String APP_PATH = "appPath";
    private static final String APP_ARGUMENTS = "appArguments";
    private static final String LAUNCH_DELAY = "launchDelay";
    private static final int TIMEOUT = 30;

    /**
     * Initializes a new instance of the {@link DesktopDriver} class using the specified options.
     *
     * @param options Thre {@link DesktopDriverOptions} to be used with the DesktopDriver driver.
     */
    public DesktopDriver(final DesktopDriverOptions options) {
        this(createDefaultService(options.getClass()), options);
    }

    /**
     * Initializes a new instance of the {@link DesktopDriver} class using the specified {@link DesktopDriverService}
     * and options.
     *
     * @param service The {@link DesktopDriverService} to use.
     * @param options The {@link DesktopDriverOptions} used to initialize the driver.
     */
    public DesktopDriver(final DesktopDriverService service, final DesktopDriverOptions options) {
        super(new DesktopDriverCommandExecutor(service), options.toCapabilities());
    }

    /**
     * Initializes a new instance of the {@link DesktopDriver} class using the specified {@link DesktopDriverService}
     * and options.
     *
     * @param service The {@link DesktopDriverService} to use.
     * @param dc      The {@link DesiredCapabilities} used to initialize the driver.
     */
    public DesktopDriver(final DesktopDriverService service, final DesiredCapabilities dc) {
        super(new DesktopDriverCommandExecutor(service), dc);
    }

    /**
     * Initializes a new instance of the {@link DesktopDriver} lass using the specified remote address and options.
     *
     * @param remoteAddress URL containing the address of the DesktopDriver remote server.
     * @param options       The {@link DesktopDriverOptions} object to be used with the DesktopDriver driver.
     */
    public DesktopDriver(final URL remoteAddress, final DesktopDriverOptions options) {
        super(new DesktopDriverCommandExecutor(remoteAddress), options.toCapabilities());
    }

    /**
     * Initializes a new instance of the {@link DesktopDriver} lass using the specified remote address and options.
     *
     * @param remoteAddress URL containing the address of the DesktopDriver remote server.
     * @param dc            The {@link DesiredCapabilities} object to be used with the DesktopDriver driver.
     */
    public DesktopDriver(final URL remoteAddress, final DesiredCapabilities dc) {
        super(new DesktopDriverCommandExecutor(remoteAddress), dc);
    }

    private static DesktopDriverService createDefaultService(final Class<? extends DesktopDriverOptions> optionsType) {
        if (optionsType == DesktopOptions.class) {
            return DesktopDriverService.createDesktopService();
        }
        throw new IllegalArgumentException("Option type must be type of DesktopOptions");
    }

    /**
     * quits driver
     */
    @SneakyThrows
    @Override
    public void quit() {
        execute("close");
        try {
            execute("quit");
        } catch (Exception e) {
            log.warn("Unable to close driver by command, trying by force");
        }
        Process p = Runtime.getRuntime().exec("taskkill /F /IM FlaNium.Driver.exe");
        if (!p.waitFor(TIMEOUT, SECONDS)) {
            p.destroyForcibly();
        }
    }

    /**
     * closes working application
     */
    @SneakyThrows
    public void closeApp() {
        var appName = this.getCapabilities().getCapability("app").toString();
        String processName =
                Arrays.stream(appName.split("/")).reduce((a, a1) -> a1).orElse("");
        Process p = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
        if (!p.waitFor(TIMEOUT, SECONDS)) {
            p.destroyForcibly();
        }
    }

    /**
     * kills all given processes
     * @param processName
     */
    public void killAllProcessesByName(String processName) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, processName);
        this.execute(KILL_PROCESSES, parameters);
    }

    /**
     * starts given application with properties
     * @param appPath
     * @param appArguments
     * @param launchDelayMs
     */
    public void startApp(String appPath, String appArguments, Integer launchDelayMs) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(APP_PATH, appPath);
        if (appArguments != null) {
            parameters.put(APP_ARGUMENTS, appArguments);
        }
        parameters.put(LAUNCH_DELAY, launchDelayMs);
        this.execute(START_APP, parameters);
    }

    /**
     * executes driver command with properties
     * @param driverCommand
     * @param parameters
     * @return
     */
    @Override
    public Response execute(final String driverCommand, final Map<String, ?> parameters) {
        return super.execute(driverCommand, parameters);
    }

    /**
     * Drags and drops the mouse from the starting point with the given distance.
     *
     * @param x  X coordinate of the start point.
     * @param y  Y coordinate of the start point.
     * @param dx The x distance to drag and drop, + for right, - for left.
     * @param dy The y distance to drag and drop, + for down, - for up.
     */
    public void dragAndDrop(final int x, final int y, final int dx, final int dy) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put("dx", dx);
        parameters.put("dy", dy);

        this.execute(DRAG_AND_DROP, parameters);
    }

    /**
     * Get the active window.
     *
     * @return The active window.
     */
    public TypifiedDesktopElement getActiveWindow() {
        try {
            var response = this.execute(GET_ACTIVE_WINDOW);
            Object value = response.getValue();
            if (value instanceof RemoteWebElement) {
                return new TypifiedDesktopElement((WebElement) value, WebElementExtensions.NO_TITLE);
            }
            if (!(value instanceof Map<?, ?>)) {
                return null;
            }

            Map<?, ?> elementDictionary = (Map<?, ?>) value;
            var result = new RemoteWebElement();
            result.setParent(this);
            result.setId((String) elementDictionary.get("ELEMENT"));
            return new TypifiedDesktopElement((WebElement) value, WebElementExtensions.NO_TITLE);

        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Simulate keystrokes. Send chars to active element.
     *
     * @param chars String of chars
     */
    public void sendChars(final String chars) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(VALUE, chars);

        this.execute(SEND_CHARS_TO_ACTIVE_ELEMENT, parameters);
    }

    /**
     * Get keyboard layout.
     *
     * @return - hex string code of keyboard layout.
     */
    public String getKeyboardLayoutCode() {
        return this.execute(GET_KEYBOARD_LAYOUT).getValue().toString();
    }

    /**
     * Set keyboard layout.
     *
     * @param keyboardLayout - hex string code of keyboard layout.
     */
    public void setKeyboardLayoutCode(final String keyboardLayout) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(VALUE, keyboardLayout);

        this.execute(SET_KEYBOARD_LAYOUT, parameters);
    }

    /**
     * Get keyboard layout.
     *
     * @return - {@link KeyboardLayout} instance of keyboard layout.
     */
    public KeyboardLayout getKeyboardLayout() {
        return KeyboardLayout.getKeyboardLayout(getKeyboardLayoutCode());
    }

    /**
     * Set keyboard layout.
     *
     * @param keyboardLayout - {@link KeyboardLayout} instance of keyboard layout.
     */
    public void setKeyboardLayout(final KeyboardLayout keyboardLayout) {
        setKeyboardLayoutCode(keyboardLayout.getLayoutCode());
    }

    /**
     * Get clipboard text.
     *
     * @return clipboard text string. Returned empty string if clipboard empty or contains no text.
     */
    public String getClipboardText() {
        return this.execute(GET_CLIPBOARD_TEXT).getValue().toString();
    }

    /**
     * Set clipboard text.
     *
     * @param text the text to be copied to the clipboard.
     */
    public void setClipboardText(final String text) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(VALUE, text);

        this.execute(SET_CLIPBOARD_TEXT, parameters);
    }

    /**
     * Keystrokes of the selected combination.
     *
     * @param keyCombination {@link KeyCombination} instance of key combination.
     */
    public void performKeyCombination(final KeyCombination keyCombination) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, keyCombination.toString());

        this.execute(KEY_COMBINATION, parameters);
    }

    /**
     * changes active process
     * @param processName
     * @param timeout
     */
    public void changeProcess(String processName, int timeout) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(NAME, processName);
        parameters.put(S_TIMEOUT, timeout);

        this.execute("changeProcess", parameters);
    }

    /**
     * sets new root window
     * @param webElement
     */
    public void setRoot(RemoteWebElement webElement) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(ID, webElement.getId());
        parameters.put(TYPE, "element");
        this.execute(SET_ROOT, parameters);
    }

    /**
     * resets root window
     */
    public void resetRoot() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(TYPE, "process");
        this.execute(SET_ROOT, parameters);
    }

    /**
     * sets desktop as root window
     */
    public void setDesktopAsRoot() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(TYPE, "desktop");
        this.execute(SET_ROOT, parameters);
    }

    @Override
    public String getPageSource() {
        return null;
    }

    /**
     * returns screenshot of active window
     * @param outputType
     * @return
     * @param <X>
     * @throws WebDriverException
     */
    @SneakyThrows
    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        TypifiedDesktopElement window = getActiveWindow();
        try {
            return window.getScreenshotAs(outputType);
        } catch (Exception e) {
            var response = execute(DriverCommand.SCREENSHOT);

            if (outputType.equals(OutputType.BYTES)) {
                var screen = super.getScreenshotAs(OutputType.BYTES);
                var image = (ImageIO.read(new ByteArrayInputStream(screen)));
                try {
                    Rectangle rect = DriverDesktop.getElementRect(window);
                    image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
                } catch (Exception ex) {
                    log.info("Unable extract subImage, return fullscreen");
                }
                var baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);

                return (X) baos.toByteArray();
            }

            return screenshotResponse(response, outputType);
        }
    }

    /**
     * returns screenshot from command's response
     * @param response
     * @param outputType
     * @return
     * @param <X>
     * @throws WebDriverException
     */
    public static <X> X screenshotResponse(Response response, OutputType<X> outputType) throws WebDriverException {
        Object result = response.getValue();
        if (result instanceof String) {
            String base64EncodedPng = (String) result;
            if (base64EncodedPng.contains("Unknown command")) {
                throw new AutotestException("Unable to get screenshot on chosen target");
            }
            return outputType.convertFromBase64Png(base64EncodedPng);
        } else if (result instanceof byte[]) {
            if (((byte[]) result).length == 0) {
                throw new AutotestException("Unable to get screenshot on chosen target");
            }
            return outputType.convertFromPngBytes((byte[]) result);
        } else {
            throw new RuntimeException(String.format(
                    "Unexpected result for %s command: %s",
                    DriverCommand.SCREENSHOT,
                    result == null ? "null" : result.getClass().getName() + " instance"));
        }
    }
}
