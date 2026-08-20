package ru.autotestframework.ui_core.page_manager;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Page.
 */
public interface Page {
    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets element by title.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @return the element by title
     */
    <T extends IElement> T getElementByTitle(String elementTitle);

    /**
     * Gets elements list by title.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @return the elements list by title
     */
    <T extends WebElement> List<T> getElementsListByTitle(String elementTitle);

    /**
     * Gets element.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @param elementType  the element type
     * @return the element
     */
    <T> T getElement(String elementTitle, Class<T> elementType);

    /**
     * Gets elements list.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @param elementType  the element type
     * @return the elements list
     */
    <T> List<T> getElementsList(String elementTitle, Class<T> elementType);

    /**
     * Gets elements by type.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the elements by type
     */
    <T> Map<String, T> getElementsByType(final Class<T> type);

    /**
     * Resolve element by title t.
     *
     * @param <T>          the type parameter
     * @param elementTitle the element title
     * @return the t
     */
    default <T extends IElement> T resolveElementByTitle(String elementTitle) {
        throw new InitializationException(
                "No element '{}' declared on page '{}'",
                elementTitle,
                getClass().getSimpleName());
    }

    /**
     * Метод отвечает за проверку, что загрузилась именно ожидаемая страница
     * и на ней отображены необходимые для начала взаимодействия элементы.
     */
    void checkAcceptor();

    /**
     * Метод отвечает за переключение страницы в нужный контекст (фрейм и т.д.)
     */
    default void switchTo() {}
}
