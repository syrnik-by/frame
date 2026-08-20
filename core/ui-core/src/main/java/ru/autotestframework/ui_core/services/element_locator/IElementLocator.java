package ru.autotestframework.ui_core.services.element_locator;

import static ru.autotestframework.Constants.DEFAULT_GLUE;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Element locator.
 */
public class IElementLocator implements ElementLocator {
    @Setter
    private static String classNames = DEFAULT_GLUE + ".web_elements.elements.typified.TypifiedWebElement";

    private final SearchContext searchContext;
    private final boolean shouldCache;
    private final By by;
    private final Class<?> clazz;
    private final String title;
    private WebElement cachedElement;
    private List<WebElement> cachedElementList;

    /**
     * Use this constructor in order to process custom annotations.
     *
     * @param searchContext The context to use when finding the element
     * @param annotations   AbstractAnnotations class implementation
     * @param field         Current field with element description
     */
    public IElementLocator(
            final SearchContext searchContext, final AbstractAnnotations annotations, final Field field) {
        this.searchContext = searchContext;
        this.shouldCache = annotations.isLookupCached();
        this.by = annotations.buildBy();
        this.clazz = getGenericClassByDriver(field);
        String title = field.getName();
        if (field.isAnnotationPresent(Element.class)) {
            title = field.getAnnotation(Element.class).value();
        }
        this.title = title;
    }

    private Class<?> getGenericClassByDriver(final Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            Class<?> genericType;
            try {
                genericType = Class.forName(
                        ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName());
                if (IElement.class.equals(genericType) || WebElement.class.equals(genericType)) {
                    return Class.forName(classNames);
                }
                if (!IElement.class.isAssignableFrom(genericType)) {
                    throw new ClassNotFoundException();
                }
                return genericType;
            } catch (ClassNotFoundException e) {
                throw new InitializationException("invalid elements type in the list {}", e, title);
            }
        }
        return field.getType();
    }

    /**
     * Find the element.
     * @return WebElement proxy from cache if possible, if not - created one.
     */
    @Override
    public WebElement findElement() {
        if (cachedElement != null && shouldCache()) {
            return cachedElement;
        }
        WebElement element = searchContext.findElement(by);
        if (shouldCache()) {
            cachedElement = element;
        }
        return element;
    }

    /**
     * Find the element list.
     * @return cached WebElement proxy list if possible, if not - created one.
     */
    @SneakyThrows
    @Override
    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache()) {
            return cachedElementList;
        }
        List<WebElement> elements = new ArrayList<>();
        List<WebElement> foundElements = searchContext.findElements(by);
        foundElements.forEach(webElement -> {
            try {
                elements.add((WebElement)
                        clazz.getConstructor(WebElement.class, String.class).newInstance(webElement, title));
            } catch (Exception e) {
                throw new InitializationException("invalid elements type in the list {}", e, title);
            }
        });
        if (shouldCache()) {
            cachedElementList = elements;
        }
        return elements;
    }

    /**
     * Returns whether the element should be cached.
     *
     * @return {@code true} if the element should be cached
     */
    protected boolean shouldCache() {
        return shouldCache;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " '" + by + "'";
    }
}
