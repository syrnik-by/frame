package ru.autotestframework.desktop_elements.elements;

import static ru.autotestframework.desktop_elements.elements.WebElementExtensions.NO_TITLE;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class ListBoxItem extends TypifiedDesktopElement {

    private static final String LIST_BOX_ITEM_SCROLL_INTO_VIEW = "listBoxItemScrollIntoView";

    public ListBoxItem(final WebElement wrappedElement) {
        super(wrappedElement, NO_TITLE);
    }

    /**
     * Scrolls the element into view.
     *
     * @return visible item
     */
    public ListBoxItem scrollIntoView() {
        var response = callVoidCommand(LIST_BOX_ITEM_SCROLL_INTO_VIEW);
        if (response == null) {
            return null;
        }
        return new ListBoxItem(createWebElementFromResponse(response));
    }

    /**
     * Gets if the listbox item is checked, if checking is supported.
     *
     * @return checkable status of the item
     */
    public boolean isChecked() {
        return isSelected();
    }

    /**
     * Sets if the listbox item is checked, if checking is supported.
     *
     * @param state state of the item which should be set
     */
    public void changeState(final boolean state) {
        if (isChecked() != state) {
            getSelenideElement().click();
        }
    }
}
