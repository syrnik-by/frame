package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class MenuItem extends TypifiedDesktopElement {

    private static final String MENU_ITEM_INVOKE = "menuItemInvoke";
    private static final String MENU_ITEM_EXPAND = "menuItemExpand";
    private static final String MENU_ITEM_COLLAPSE = "menuItemCollapse";
    private static final String MENU_ITEM_IS_CHECKED = "menuItemIsChecked";
    public static final String MENU_ITEM_XPATH = "./MenuItem";

    public MenuItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public MenuItem(final WebElement wrappedElement, String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets all MenuItem which are inside this element.
     *
     * @return list of menu items inside element
     */
    public List<MenuItem> items() {
        return getSelenideElement().$$(By.xpath(MENU_ITEM_XPATH)).stream()
                .map(MenuItem::new)
                .collect(Collectors.toList());
    }

    /**
     * Invokes the element.
     *
     * @return menu item which was invoked
     */
    public MenuItem invoke() {
        var response = callVoidCommand(MENU_ITEM_INVOKE);
        if (response == null) {
            return null;
        }
        return new MenuItem(createWebElementFromResponse(response));
    }

    /**
     * expands MenuItem
     * @return
     */
    public MenuItem expand() {
        var response = callVoidCommand(MENU_ITEM_EXPAND);
        if (response == null) {
            return null;
        }
        return new MenuItem(createWebElementFromResponse(response));
    }

    /**
     * collapses MenuItem
     * @return
     */
    public MenuItem collapse() {
        var response = callVoidCommand(MENU_ITEM_COLLAPSE);
        if (response == null) {
            return null;
        }
        return new MenuItem(createWebElementFromResponse(response));
    }

    /**
     * checks if MenuItem is checked
     * @return
     */
    public boolean isChecked() {
        var response = callVoidCommand(MENU_ITEM_IS_CHECKED);
        return Boolean.parseBoolean(response.getValue().toString());
    }
}
