package ru.autotestframework.ui_core.page_manager;

import static com.codeborne.selenide.Selenide.$;
import static ru.autotestframework.Constants.DEFAULT_GLUE;

import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * An abstract page that implements basic methods of working with pages.
 */
@Slf4j
public abstract class AbstractPage implements Page {

    @Override
    public String getTitle() {
        String title = getClass().getSimpleName();
        if (getClass().isAnnotationPresent(PageEntry.class)) {
            title = getClass().getAnnotation(PageEntry.class).title();
        }
        return title;
    }

    /**
     * Get t.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @return the t
     */
    @SneakyThrows
    public <T extends SelenideElement> T get(final String elementTitle) {
        Class currentPageClass = getClass();
        while (currentPageClass != AbstractPage.class) {
            for (final Field field : currentPageClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Element.class)
                        && elementTitle.equals(
                                field.getAnnotation(Element.class).value())) {
                    field.setAccessible(true);
                    try {
                        return (T) $((WebElement) field.get(this)).as(elementTitle);
                    } catch (Exception e) {
                        return (T) ((SelenideElement) field.get(this)).as(elementTitle);
                    }
                }
            }
            currentPageClass = currentPageClass.getSuperclass();
        }
        throw new InitializationException(
                "No element '{}' declared on page '{}'",
                elementTitle,
                getClass().getSimpleName());
    }

    /**
     *
     * @param elementTitle {@link Element}
     * @return {@link IElement impl} or wrapped {@link SelenideElement}
     * @param <T> current element
     */
    @SneakyThrows
    @Override
    public <T extends IElement> T getElementByTitle(final String elementTitle) {
        Class currentPageClass = getClass();
        while (currentPageClass != AbstractPage.class) {
            for (final Field field : currentPageClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Element.class)
                        && elementTitle.equals(
                                field.getAnnotation(Element.class).value())) {
                    field.setAccessible(true);
                    if (SelenideElement.class.isAssignableFrom((Class) field.getGenericType())) {
                        return (T) Class.forName(DEFAULT_GLUE + ".web_elements.elements.typified.TypifiedWebElement")
                                .getConstructor(WebElement.class, String.class)
                                .newInstance(field.get(this), elementTitle);
                    }
                    return (T) field.get(this);
                }
            }
            currentPageClass = currentPageClass.getSuperclass();
        }

        return resolveElementByTitle(elementTitle);
    }

    @SneakyThrows
    @Override
    public <T extends WebElement> List<T> getElementsListByTitle(final String elementTitle) {
        Class currentPageClass = getClass();
        while (currentPageClass != AbstractPage.class) {
            for (final Field field : currentPageClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Element.class)
                        && elementTitle.equals(
                                field.getAnnotation(Element.class).value())) {
                    field.setAccessible(true);
                    return (List<T>) field.get(this);
                }
            }
            currentPageClass = currentPageClass.getSuperclass();
        }
        throw new InitializationException(
                "No element '{}' declared on page '{}'",
                elementTitle,
                getClass().getSimpleName());
    }

    @Override
    public <T> T getElement(final String elementTitle, final Class<T> type) {
        IElement element = getElementByTitle(elementTitle);
        if (type.isInstance(element)) {
            return type.cast(element);
        }
        throw new InitializationException(
                "Method does not exist on element `{}'. Add interface '{}'", elementTitle, type);
    }

    @Override
    public <T> List<T> getElementsList(final String elementTitle, final Class<T> type) {
        List<WebElement> elements = getElementsListByTitle(elementTitle).stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
        if (!elements.isEmpty()) {
            elements.forEach(type::cast);
            return (List<T>) elements;
        }
        throw new InitializationException(
                "Method does not exist on elementList `{}'. Add interface '{}'", elementTitle, type);
    }

    @Override
    public <T> Map<String, T> getElementsByType(Class<T> type) {
        Map<String, T> elements = new HashMap<>();
        Class currentPageClass = getClass();
        while (currentPageClass != AbstractPage.class) {
            for (final Field field : currentPageClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Element.class)) {
                    field.setAccessible(true);
                    try {
                        var instanceField = field.get(this);
                        if (ClassUtils.getAllInterfaces(instanceField.getClass())
                                .contains(type)) {
                            elements.put(field.getAnnotation(Element.class).value(), (T) instanceField);
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            currentPageClass = currentPageClass.getSuperclass();
        }
        return elements;
    }

    /**
     * Check if exactly this page exactly expected (title check / url, etc.)
     */
    @Override
    public void checkAcceptor() {
        log.warn(
                "Method checkAcceptor() not implemented on page '{}', page class '{}'",
                getTitle(),
                getClass().getSimpleName());
    }
}
