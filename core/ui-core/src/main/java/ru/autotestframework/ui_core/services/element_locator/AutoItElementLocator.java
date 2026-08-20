package ru.autotestframework.ui_core.services.element_locator;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.services.element_manager.FindByControl;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Auto it element locator.
 */
public class AutoItElementLocator implements ElementLocator {

    private final String winTitle;
    private final String title;
    private final String control;
    private Field field;

    /**
     * Instantiates a new Auto it element locator.
     *
     * @param field the field
     */
    public AutoItElementLocator(final Field field) {
        this(
                field.getAnnotation(FindByControl.class).winTitle(),
                field.getAnnotation(Element.class).value(),
                field.getAnnotation(FindByControl.class).control());
        this.field = field;
    }

    private AutoItElementLocator(final String winTitle, final String title, final String control) {
        this.winTitle = winTitle;
        this.title = title;
        this.control = control;
    }

    @Override
    public WebElement findElement() {
        try {
            return (IElement) field.getType()
                    .getConstructor(String.class, String.class, String.class)
                    .newInstance(winTitle, title, control);
        } catch (Exception e) {
            throw new InitializationException("invalid elements type in the list {}", e, title);
        }
    }

    @Override
    public List<WebElement> findElements() {
        throw new InitializationException("list don't supported by autoit-elements", title);
    }
}
