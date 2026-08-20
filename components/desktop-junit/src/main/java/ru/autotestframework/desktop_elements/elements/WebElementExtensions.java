package ru.autotestframework.desktop_elements.elements;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

/**
 * Утилитный класс для более удобного создания экземпляров элементов.
 */
@UtilityClass
public final class WebElementExtensions {

    public static final String NO_TITLE = "no title";

    /**
     * converts WebElement to TypifiedDesktopElement subclass
     * @param element
     * @return
     * @param <T>
     */
    public static <T extends TypifiedDesktopElement> T to(final WebElement element) {
        return ((T) (new TypifiedDesktopElement(element, NO_TITLE)));
    }
}
