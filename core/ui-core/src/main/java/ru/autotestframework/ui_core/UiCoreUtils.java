package ru.autotestframework.ui_core;

import static java.lang.Math.min;

import com.google.common.net.MediaType;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.ui_core.typified_elements.enums.ImageFormat;
import ru.autotestframework.util.ServiceLoaderListener;

/**
 * Ui core utils.
 */
@Slf4j
@UtilityClass
public class UiCoreUtils {

    /**
     * The constant ATTACHMENT_FILENAME_MAX_LENGTH.
     */
    public static final int ATTACHMENT_FILENAME_MAX_LENGTH = 127;

    private static final String DELIMITER = "__";

    /**
     * Gets element resolvers.
     *
     * @return the element resolvers
     */
    public static List<ElementResolver> getElementResolvers() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return ServiceLoaderListener.load(ElementResolver.class, classLoader);
    }

    /**
     * Parse value list list.
     *
     * @param value     the value
     * @param delimeter the delimeter
     * @return the list
     */
    public static List<String> parseValueList(final String value, final String delimeter) {
        return Arrays.stream(value.split(delimeter)).map(String::trim).collect(Collectors.toList());
    }

    public void allureAttach(String methodName, WebDriver webDriver) {
        if (Allure.getLifecycle().getCurrentTestCase().isPresent()) {
            try {
                String screenshotName = generateScreenNameOnMethod(methodName, webDriver);
                Allure.addAttachment(
                        screenshotName,
                        MediaType.PNG.toString(),
                        new ByteArrayInputStream(getScreenshotBytes(webDriver)),
                        ".".concat(ImageFormat.PNG.toString()));
            } catch (Exception e) {
                log.error("Error happened while adding screenshot:", e);
            }
        }
    }

    private String generateScreenNameOnMethod(String methodName, WebDriver webDriver) {
        String fullName = methodName
                .concat(DELIMITER)
                .concat(UUID.randomUUID().toString())
                .concat(".")
                .concat(ImageFormat.PNG.toString());
        return fullName.substring(0, min(ATTACHMENT_FILENAME_MAX_LENGTH, fullName.length()));
    }

    private byte[] getScreenshotBytes(WebDriver webDriver) {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }
}
