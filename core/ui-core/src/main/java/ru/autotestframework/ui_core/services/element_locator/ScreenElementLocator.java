package ru.autotestframework.ui_core.services.element_locator;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.services.element_manager.FindByScreen;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Screen element locator.
 */
public class ScreenElementLocator implements ElementLocator {

    private final String title;
    private final String regionLocation;
    private final String location;
    private final int x;
    private final int y;
    private Field field;

    /**
     * Instantiates a new Screen element locator.
     *
     * @param field the field
     */
    public ScreenElementLocator(final Field field) {
        this(
                field.getAnnotation(Element.class).value(),
                field.getAnnotation(FindByScreen.class).regionLocation(),
                field.getAnnotation(FindByScreen.class).location(),
                field.getAnnotation(FindByScreen.class).offsetX(),
                field.getAnnotation(FindByScreen.class).offsetY());
        this.field = field;
    }

    private ScreenElementLocator(
            final String title, final String regionLocation, final String location, final int x, final int y) {
        this.title = title;
        this.regionLocation = regionLocation;
        this.location = location;
        this.x = x;
        this.y = y;
    }

    @Override
    public WebElement findElement() {
        try {
            return (IElement) field.getType()
                    .getConstructor(String.class, String.class, String.class, int.class, int.class)
                    .newInstance(title, regionLocation, location, x, y);
        } catch (Exception e) {
            throw new InitializationException("invalid elements type in the list {}", e, title);
        }
    }

    @Override
    public List<WebElement> findElements() {
        throw new InitializationException("list don't supported by screen-elements", title);
    }
}
