package ru.autotestframework.desktop_elements.elements;

import static ru.autotestframework.desktop_elements.elements.WebElementExtensions.NO_TITLE;

import com.codeborne.selenide.collections.SizeGreaterThan;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;

public class Menu extends TypifiedDesktopElement implements ISelectable {

    public Menu(final WebElement wrappedElement) {
        super(wrappedElement, NO_TITLE);
    }

    public Menu(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
    /**
     * Gets all MenuItem which are inside this element.
     *
     * @return list of menu items inside element
     */
    public List<MenuItem> items() {
        return getSelenideElement().$$x("./MenuItem").shouldBe(new SizeGreaterThan(0)).stream()
                .map(MenuItem::new)
                .collect(Collectors.toList());
    }

    /**
     * selects next menu
     * @param value
     * @throws ElementInteractionException
     */
    @Override
    public void select(String value) throws ElementInteractionException {
        getSelenideElement().click();

        var selected = items().stream()
                .filter(x -> x.getAttribute("Name").equals(value))
                .findFirst()
                .orElseThrow(
                        () -> new ElementInteractionException("Menu '{}' doesn't contain '{}'", getTitle(), value));
        selected.click();
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
