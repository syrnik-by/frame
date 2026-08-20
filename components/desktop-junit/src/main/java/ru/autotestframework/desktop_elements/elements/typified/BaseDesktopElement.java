package ru.autotestframework.desktop_elements.elements.typified;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;
import ru.autotestframework.desktop_elements.elements.WebElementExtensions;
import ru.autotestframework.desktop_elements.elements.Window;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.enums.ImageFormat;

/**
 * Class implements methods to work with Driver for Windows Desktop Applications.
 * Contains Desktop specific logic to interact with Windows API.
 */
@Slf4j
public abstract class BaseDesktopElement extends BaseElement {

    private static final String WINDOW_GET_ACTIVE_WINDOW = "windowGetActiveWindow";
    private static final String ELEMENT_SCREENSHOT = "elementScreenshot";
    private static final String INDEX = "index";
    private static final int TIMEOUT = Integer.parseInt(System.getProperty("framework.ui.timeout"));
    private static WebDriver parent;

    @Getter
    private final WebElement elementWrapper;

    @Setter
    private String id;

    private JsonToWebElementConverter converter;
    private SessionId sessionId;
    private CommandExecutor executor;
    private Capabilities capabilities;
    private static FileDetector fileDetector;
    private SelenideElement selenideElement;
    private int baseDesktopElementTimeout;

    public BaseDesktopElement(final WebElement element, final String title) {
        this.title = title;
        this.elementWrapper = element;
        baseDesktopElementTimeout = TIMEOUT;
    }

    /**
     * sets session id
     *
     * @param parent
     */
    protected void setSessionId(final WebDriver parent) {
        this.sessionId = ((DesktopDriver) parent).getSessionId();
    }

    /**
     * sets parent
     *
     * @param parent
     */
    public void setParent(final WebDriver parent) {
        BaseDesktopElement.parent = parent;
    }

    /**
     * executes command with params
     *
     * @param command
     * @param params
     */
    public void exec(final String command, final Map<String, ?> params) {
        execute(command, params);
    }

    public String getId() {
        if (id == null) {
            id = ((RemoteWebElement) getSelenideElement().getWrappedElement()).getId();
        }
        return id;
    }

    /**
     * sets driver
     *
     * @param driver
     */
    public static void setDriver(final WebDriver driver) {
        parent = driver;
    }

    /**
     * returns element's selenide element
     *
     * @return
     */
    public SelenideElement getSelenideElement() {
        if (selenideElement == null) {
            selenideElement = initSelenideElement(baseDesktopElementTimeout);
        }
        return selenideElement;
    }

    /**
     * sets timeout
     *
     * @param timeout
     * @return
     */
    public BaseDesktopElement withTimeout(final int timeout) {
        this.baseDesktopElementTimeout = timeout;
        return this;
    }

    /**
     * To reinitialise desktop element due to ui reformation.
     * Element became unavailable (zero size) and another element is presented instead of, so need init on Project Side.
     */
    @SneakyThrows
    public SelenideElement initSelenideElement(final int timeout) {
        var stopWatch = StopWatch.createStarted();
        while (stopWatch.getTime(TimeUnit.MILLISECONDS) <= timeout * 1000L && Objects.isNull(selenideElement)) {
            try {
                return $(((WrapsElement) elementWrapper).getWrappedElement());
            } catch (Exception ignored) {
                Thread.sleep(100);
            }
        }
        stopWatch.stop();
        this.baseDesktopElementTimeout = TIMEOUT;
        selenideElement = $(elementWrapper);
        return selenideElement;
    }

    /**
     * returns parent
     *
     * @return
     */
    public WebDriver getParent() {
        if (parent == null) {
            parent = getSelenideElement().getWrappedDriver();
        }
        return parent;
    }

    /**
     * returns commmand executor
     *
     * @return
     */
    public CommandExecutor getExecutor() {
        if (executor == null) {
            executor = ((DesktopDriver) getParent()).getCommandExecutor();
        }
        return executor;
    }

    /**
     * creates element from command's response
     *
     * @param response
     * @return
     */
    // region Create from Response
    public WebElement createWebElementFromResponse(final Response response) {
        Object value = response.getValue();
        if (value instanceof SelenideElement) {
            return (SelenideElement) value;
        }

        if (!(value instanceof Map<?, ?>)) {
            return null;
        }
        Map<?, ?> elementDictionary = (Map<?, ?>) value;
        TypifiedDesktopElement result = WebElementExtensions.to((WebElement) value);
        result.setId((String) elementDictionary.get("ELEMENT"));
        return result;
    }

    /**
     * creates elements from command response
     *
     * @param response
     * @return
     */
    public List<WebElement> createWebElementsFromResponse(final Response response) {

        Object responseValue = response.getValue();

        List allElements;
        try {
            allElements = (List) responseValue;
        } catch (ClassCastException e) {
            throw new InitializationException(
                    "Returned value cannot be converted to List<WebElement>: {}", e, responseValue);
        }

        var iterator = allElements.iterator();

        while (iterator.hasNext()) {
            TypifiedDesktopElement element = WebElementExtensions.to((WebElement) iterator.next());
            element.setParent((DesktopDriver) getSelenideElement().getWrappedDriver());
        }
        return allElements;
    }

    /**
     * returns localDateTime from command's response
     *
     * @param response
     * @return
     */
    public List<LocalDateTime> createLocalDateTimeFromResponse(final Response response) {

        Object responseValue = response.getValue();

        List allElements;
        try {
            allElements = (List) responseValue;
        } catch (ClassCastException var8) {
            throw new InitializationException(
                    "Returned value cannot be converted to List<LocalDateTime>: {}", var8, responseValue);
        }

        var var6 = allElements.iterator();
        List<LocalDateTime> list = new ArrayList<>();

        while (var6.hasNext()) {
            var object = var6.next();
            var value = object.toString();
            var localDateTime = parseDateTime(value);
            list.add(localDateTime);
        }
        return list;
    }
    // endregion

    /**
     * executes command with parameters
     *
     * @param command
     * @param parameters
     * @return
     */
    public Response exe(final String command, final HashMap<String, Object> parameters) {
        Response response;
        try {
            response = execute(command, parameters);
        } catch (NoSuchElementException e) {
            response = null;
        }
        return response;
    }

    // region Call Command

    /**
     * executes command with parameters
     *
     * @param command
     * @return
     */
    public Response callVoidCommand(final String command) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        return exe(command, parameters);
    }

    /**
     * executes command with value and parameters
     *
     * @param command
     * @param value
     * @return
     */
    public Response callValueCommand(final String command, final String value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put("value", value);
        return exe(command, parameters);
    }

    /**
     * executes command with index and parameters
     *
     * @param command
     * @param index
     * @return
     */
    public Response callValueCommand(final String command, final int index) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put(INDEX, index);
        return exe(command, parameters);
    }

    protected Response callValueCommand(final String command, final int index, final String text) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put(INDEX, index);
        parameters.put("text", text);
        return exe(command, parameters);
    }

    protected Response callValueCommand(final String command, final int index, final String text, final int count) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put(INDEX, index);
        parameters.put("text", text);
        parameters.put("count", count);
        return exe(command, parameters);
    }

    protected Response callValueCommand(final String command, final int x, final int y) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        return exe(command, parameters);
    }

    protected Response callValueCommand(final String command, final LocalDateTime dateTime) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.getId());
        parameters.put("dateTime", dateTime.toString());
        return exe(command, parameters);
    }
    // endregion

    // region public Method

    /**
     * Get the active window.
     *
     * @return The active window.
     */
    public Window getActiveWindow() {
        var response = callVoidCommand(WINDOW_GET_ACTIVE_WINDOW);
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }
    // endregion

    // region Parse

    protected double parseDouble(final Response response) {
        var value = response.getValue().toString();
        if (value.contains(",")) {
            value = value.replace(",", ".");
        }
        return Double.parseDouble(value);
    }

    protected LocalDateTime parseDateTime(final String dateTime) {
        String pattern = null;

        if (dateTime.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "dd.MM.uuuu HH:mm:ss";
        } else if (dateTime.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d:\\d{2}:\\d{2}")) {
            pattern = "dd.MM.uuuu H:mm:ss";
        }

        if (pattern != null) {
            var dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        }
        return LocalDateTime.parse(dateTime);
    }
    // endregion

    /**
     * TODO why in diff formats( if needed - use selenide and img type reconstracting via BufferedImage)
     * Taking a screenshot of the current item.
     *
     * @param outputType  Return type BASE64, BYTES or FILE.
     * @param imageFormat Image format: BMP, EMF, WMF, GIF, JPEG, PNG, TIFF, EXIF, ICON.
     * @param foreground  If the parameter is set to false,
     *                    it allows you to take a screenshot of an object that is not in the foreground.
     * @param <X>         generic
     * @return Screenshot of the current item.
     * @throws WebDriverException ...
     */
    public <X> X getScreenshot(final OutputType<X> outputType, final ImageFormat imageFormat, final boolean foreground)
            throws WebDriverException {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.withTimeout(0).getId());
        parameters.put("format", imageFormat.toString());
        parameters.put("foreground", foreground);

        var response = this.execute(ELEMENT_SCREENSHOT, parameters);
        return DesktopDriver.screenshotResponse(response, outputType);
    }

    /**
     * Taking a screenshot of the current item.
     *
     * @param imageFormat Image format: BMP, EMF, WMF, GIF, JPEG, PNG, TIFF, EXIF, ICON.
     * @return Screenshot file of the current item.
     */
    public File getScreenshotFile(final ImageFormat imageFormat) {
        return getScreenshot(OutputType.FILE, imageFormat, true);
    }

    /**
     * Taking a screenshot of the not foreground current item.
     *
     * @param imageFormat Image format: BMP, EMF, WMF, GIF, JPEG, PNG, TIFF, EXIF, ICON.
     * @return Screenshot file of the current item.
     */
    public File getScreenshotFileNotForeground(final ImageFormat imageFormat) {
        return getScreenshot(OutputType.FILE, imageFormat, false);
    }

    /**
     * returns screenshot of current item
     *
     * @param outputType
     * @param imageFormat
     * @param <X>
     * @return
     * @throws WebDriverException
     */
    public <X> X getScreenshot(final OutputType<X> outputType, final ImageFormat imageFormat)
            throws WebDriverException {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", this.withTimeout(0).getId());
        parameters.put("format", imageFormat.toString());

        var response = this.execute(ELEMENT_SCREENSHOT, parameters);
        return DesktopDriver.screenshotResponse(response, outputType);
    }

    /**
     * Taking a screenshot of the current item. Image format: PNG.
     *
     * @return Screenshot file of the current item.
     */
    public File getPngScreenshotFile() {
        var screen = getScreenshotFile(ImageFormat.PNG);
        try {
            Rectangle rect = getRect();
            ImageIO.write(ImageIO.read(screen).getSubimage(rect.x, rect.y, rect.width, rect.height), "png", screen);
        } catch (Exception e) {
            log.info("Unable to get element subImage, returning full image");
        }
        return screen;
    }

    /**
     * Taking a screenshot of the current item. Image format: JPEG.
     *
     * @return Screenshot file of the current item.
     */
    public File getJpegScreenshotFile() {
        return getScreenshotFile(ImageFormat.JPEG);
    }

    /**
     * Taking a screenshot of the not foreground current item. Image format: PNG.
     *
     * @return Screenshot file of the current item.
     */
    public File getPngScreenshotFileNotForeground() {
        return getScreenshotFileNotForeground(ImageFormat.PNG);
    }

    /**
     * Taking a screenshot of the not foreground current item. Image format: JPEG.
     *
     * @return Screenshot file of the current item.
     */
    public File getJpegScreenshotFileNotForeground() {
        return getScreenshotFileNotForeground(ImageFormat.JPEG);
    }

    /**
     * Taking a screenshot of the current item and save to file. Image format: PNG.
     *
     * @param file File path.
     * @throws IOException ...
     */
    public void savePngScreenshotFile(final String file) throws IOException {
        FileUtils.copyFile(getPngScreenshotFile(), new File(file));
    }

    /**
     * Taking a screenshot of the current item and save to file. Image format: JPEG.
     *
     * @param file File path.
     * @throws IOException ...
     */
    public void saveJpegScreenshotFile(final String file) throws IOException {
        FileUtils.copyFile(getJpegScreenshotFile(), new File(file));
    }

    /**
     * Taking a screenshot of the not foreground current item and save to file. Image format: PNG.
     *
     * @param file File path.
     * @throws IOException ...
     */
    public void savePngScreenshotFileNotForeground(final String file) throws IOException {
        FileUtils.copyFile(getPngScreenshotFileNotForeground(), new File(file));
    }

    /**
     * Taking a screenshot of the not foreground current item and save to file. Image format: JPEG.
     *
     * @param file File path.
     * @throws IOException ...
     */
    public void saveJpegScreenshotFileNotForeground(final String file) throws IOException {
        FileUtils.copyFile(getJpegScreenshotFileNotForeground(), new File(file));
    }

    public String getTitle() {
        return title;
    }

    /**
     * executes command from command payload
     *
     * @param payload
     * @return
     */
    public Response execute(final CommandPayload payload) {
        try {
            var command = new Command(getSessionId(), payload);
            Response response;

            String currentName = Thread.currentThread().getName();
            Thread.currentThread()
                    .setName(String.format("Forwarding %s on session %s to remote", command.getName(), getSessionId()));
            try {
                response = getExecutor().execute(command);

                if (response == null) {
                    return null;
                }

                Object value = getElementConverter().apply(response.getValue());
                response.setValue(value);
            } catch (Exception e) {
                WebDriverException toThrow;
                if (command.getName().equals(DriverCommand.NEW_SESSION)) {
                    if (e instanceof SessionNotCreatedException) {
                        toThrow = (WebDriverException) e;
                    } else {
                        toThrow = new SessionNotCreatedException(
                                "Possible causes are invalid address of the remote server or browser start-up failure.",
                                e);
                    }
                } else if (e instanceof WebDriverException) {
                    toThrow = (WebDriverException) e;
                } else {
                    toThrow = new UnreachableBrowserException(
                            "Error communicating with the remote browser. It may have died.", e);
                }
                populateWebDriverException(toThrow);
                toThrow.addInfo("Command", command.toString());
                throw toThrow;
            } finally {
                Thread.currentThread().setName(currentName);
            }
            return response;
        } catch (WebDriverException ex) {
            ex.addInfo("Element", this.toString());
            throw ex;
        }
    }

    /**
     * executes command with parameters in parent
     *
     * @param command
     * @param parameters
     * @return
     */
    public Response execute(final String command, final Map<String, ?> parameters) {
        try {
            return ((DesktopDriver) getParent()).execute(command, parameters);
        } catch (WebDriverException ex) {
            ex.addInfo("Element", this.toString());
            throw ex;
        }
    }

    private void populateWebDriverException(final WebDriverException ex) {
        ex.addInfo(WebDriverException.DRIVER_INFO, this.getClass().getName());
        if (getSessionId() != null) {
            ex.addInfo(WebDriverException.SESSION_ID, getSessionId().toString());
        }
        if (getCapabilities() != null) {
            ex.addInfo("Capabilities", getCapabilities().toString());
        }
    }

    /**
     * returns session id
     *
     * @return
     */
    public SessionId getSessionId() {
        if (sessionId == null) {
            sessionId = ((DesktopDriver) getParent()).getSessionId();
        }
        return sessionId;
    }

    /**
     * sets and returns capabilities
     *
     * @return
     */
    public Capabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = ((DesktopDriver) getParent()).getCapabilities();
        }
        if (capabilities == null) {
            capabilities = new ImmutableCapabilities();
        }
        return capabilities;
    }

    protected JsonToWebElementConverter getElementConverter() {
        if (converter == null) {
            converter = new JsonToWebElementConverter((DesktopDriver) getParent());
        }
        return converter;
    }

    public WebDriver getWrappedDriver() {
        return getParent();
    }
}
