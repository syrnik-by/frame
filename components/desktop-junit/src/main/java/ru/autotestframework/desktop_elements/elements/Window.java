package ru.autotestframework.desktop_elements.elements;

import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class Window extends TypifiedDesktopElement {

    private static final String WINDOW_IS_MODAL = "windowIsModal";
    private static final String WINDOW_POPUP = "windowPopup";
    private static final String WINDOW_CONTEXT_MENU = "windowContextMenu";
    private static final String WINDOW_CLOSE = "windowClose";
    private static final String WINDOW_MOVE = "windowMove";
    private static final String WINDOW_SET_TRANSPARENCY = "windowSetTransparency";
    public static final String WINDOW_XPATH = "./Window";
    public static final String TITLE_BAR_XPATH = "./TitleBar";

    public Window(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public Window(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the title of the window.
     *
     * @return title of the window
     */
    public String title() {
        return getSelenideElement().getAttribute("Name");
    }

    /**
     * Gets if the window is modal.
     *
     * @return whether the window is modal
     */
    public boolean isModal() {
        var response = callVoidCommand(WINDOW_IS_MODAL);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * Gets the "TitleBar" of the window.
     *
     * @return titlebar element of the window
     */
    public TitleBar titleBar() {
        SelenideElement titleBarEl = getSelenideElement().$x(TITLE_BAR_XPATH);
        return WebElementExtensions.to(titleBarEl);
    }

    /**
     * Gets a list of all modal child windows.
     *
     * @return list of all modal child windows
     */
    public List<Window> modalWindows() {
        return getSelenideElement().$$(By.xpath(WINDOW_XPATH)).stream()
                .map(Window::new)
                .filter(Window::isModal)
                .collect(Collectors.toList());
    }

    /**
     * Gets the current WPF popup window.
     *
     * @return current popup window
     */
    public TitleBar popup() {
        var response = callVoidCommand(WINDOW_POPUP);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * Gets the context menu for the window.
     * Note: It uses the FrameworkType of the window as lookup logic.
     * Use "GetContextMenuByFrameworkType" if you want to control this.
     *
     * @return context menu of the window
     */
    public Menu contextMenu() {
        var response = callVoidCommand(WINDOW_CONTEXT_MENU);
        if (response == null) {
            return null;
        }
        return new Menu(createWebElementFromResponse(response));
    }

    /**
     * Closes the window.
     */
    public void close() {
        callVoidCommand(WINDOW_CLOSE);
    }

    /**
     * Moves the window to the given coordinates.
     *
     * @param x x offset
     * @param y y offset
     */
    public void move(final int x, final int y) {
        callValueCommand(WINDOW_MOVE, x, y);
    }

    /**
     * Brings the element to the foreground.
     *
     * @param alpha transparency level
     */
    public void setTransparency(final byte alpha) {
        callValueCommand(WINDOW_SET_TRANSPARENCY, alpha);
    }
}
