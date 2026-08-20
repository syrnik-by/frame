package ru.autotestframework.ui_core.services.element_locator;

import java.lang.reflect.Field;
import lombok.Getter;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ru.autotestframework.ui_core.services.element_manager.FindByControl;
import ru.autotestframework.ui_core.services.element_manager.FindByScreen;

/**
 * Element locator factory.
 */
public class IElementLocatorFactory implements ElementLocatorFactory {

    @Getter
    private final SearchContext searchContext;

    /**
     * Instantiates a new Element locator factory.
     *
     * @param searchContext the search context
     */
    public IElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    // TODO ability to customize strategy
    @Override
    public ElementLocator createLocator(final Field field) {
        if (field.isAnnotationPresent(FindByScreen.class)) {
            return new ScreenElementLocator(field);
        } else if (field.isAnnotationPresent(FindByControl.class)) {
            return new AutoItElementLocator(field);
        } else {
            return new IElementLocator(searchContext, new Annotations(field), field);
        }
    }
}
