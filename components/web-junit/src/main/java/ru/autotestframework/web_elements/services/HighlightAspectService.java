package ru.autotestframework.web_elements.services;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.events.WebDriverListener;
import ru.autotestframework.ui_core.UiCoreUtils;
import ru.autotestframework.ui_core.services.JSExecutor;
import ru.autotestframework.ui_core.services.ScreenshotRegistry;

/**
 * Listener that command web-driver to highlight UI elements when driver interacting with them
 */
@Slf4j
@RequiredArgsConstructor
public class HighlightAspectService implements WebDriverListener {

    public static final String STYLE_BORDER = "return arguments[0].style.border";
    public static final String HIGHLIGHTING_ATTRIBUTE = "__selenideHighlighting";
    private final JSExecutor jsExecutor = new JSExecutor();

    @Override
    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        highlight(element, "red");
        if (checkForScreenWithHighlightAnnotation(element)) {
            UiCoreUtils.allureAttach(method.getName(), ((WrapsDriver) element).getWrappedDriver());
        }
    }

    private boolean checkForScreenWithHighlightAnnotation(WebElement element) {
        return ScreenshotRegistry.shouldTakeScreenshot(element);
    }

    @Override
    public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
        highlightElementOff(element);
    }

    private String getElementBorderStyle(final WebElement webElement) {
        try {
            return jsExecutor.executeJavaScript(STYLE_BORDER, webElement);
        } catch (Exception e) {
            log.error("Error! Element probably missing on web page: ", e);
        }
        return "";
    }

    private void highlight(final WebElement webElement, String color) {
        String originalElementStyle = getElementBorderStyle(webElement);
        try {
            jsExecutor.executeJavaScript(
                    "arguments[0].style.border='5px solid " + color + "'; arguments[0].setAttribute('"
                            + HIGHLIGHTING_ATTRIBUTE + "', '" + originalElementStyle + "')",
                    webElement);
        } catch (Exception e) {
            log.error("Error! ", e);
        }
    }

    private void highlightElementOff(final WebElement webElement) {
        String elementStyle = webElement.getAttribute(HIGHLIGHTING_ATTRIBUTE);
        try {
            jsExecutor.executeJavaScript("arguments[0].style.border='" + elementStyle + "'", webElement);
        } catch (Exception e) {
            log.error("Error! Probably element disappeared after interaction : ", e);
        }
    }
}
