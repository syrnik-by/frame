package ru.autotestframework.ui_core.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.Page;

/**
 * Class give a functionality to extract Element Data from Annotations.
 */
@UtilityClass
public class CoreReflections {

    /**
     * Gets xpath.
     *
     * @param field the field
     * @return the xpath
     */
    public static String getXpath(final Field field) {
        checkFindByAnnotation(field);
        final var findBy = field.getAnnotation(FindBy.class);
        return Objects.isNull(findBy.xpath()) ? "" : findBy.xpath();
    }

    /**
     * Gets id.
     *
     * @param field the field
     * @return the id
     */
    public static String getId(final Field field) {
        checkFindByAnnotation(field);
        final var findBy = field.getAnnotation(FindBy.class);
        return Objects.isNull(findBy.id()) ? "" : findBy.id();
    }

    /**
     * Gets css.
     *
     * @param field the field
     * @return the css
     */
    public static String getCss(final Field field) {
        checkFindByAnnotation(field);
        final var findBy = field.getAnnotation(FindBy.class);
        return Objects.isNull(findBy.css()) ? "" : findBy.css();
    }

    /**
     * Gets class name.
     *
     * @param field the field
     * @return the class name
     */
    public static String getClassName(final Field field) {
        checkFindByAnnotation(field);
        final var findBy = field.getAnnotation(FindBy.class);
        return Objects.isNull(findBy.className()) ? "" : findBy.className();
    }

    /**
     * Get a field on PageObject with given Title.
     *
     * @param page  - given page name.
     * @param title - element title.
     * @return element ' page field
     * @throws InitializationException the initialization exception
     */
    public static Field getFieldByTitle(final Page page, final String title) throws InitializationException {
        return getFields(page).stream()
                .filter(f -> title.equals(f.getAnnotation(Element.class).value()))
                .findFirst()
                .orElseThrow(() -> new InitializationException(
                        "No element '{}' declared on page '{}'",
                        title,
                        page.getClass().getSimpleName()));
    }

    /**
     * Gets fields.
     *
     * @param page the page
     * @return the fields
     */
    public static List<Field> getFields(final Page page) {
        return Stream.of(page.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Element.class))
                .collect(Collectors.toList());
    }

    /**
     * Determines the type of locator (xpath, css, id, etc.)
     *
     * @param selector from page
     * @return locator name
     */
    public static String determineLocatorType(String selector) {
        if (selector == null || selector.isEmpty()) {
            return "css";
        }
        selector = selector.trim();
        if (selector.startsWith("//")
                || selector.startsWith("./")
                || selector.startsWith("(")
                || selector.startsWith("/html")
                || selector.startsWith(".//")) {
            return "xpath";
        } else if (selector.startsWith("#")) {
            return "id";
        } else if (selector.startsWith(".")) {
            return "className";
        } else if (selector.startsWith("[name=") || selector.contains("name=\"")) {
            return "name";
        } else if (selector.startsWith("[data-")) {
            return "css";
        } else if (selector.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            return "tagName";
        } else if (selector.contains("contains(") || selector.contains("text()=")) {
            return "xpath";
        } else {
            // По умолчанию считаем CSS селектором
            return "css";
        }
    }

    private static void checkFindByAnnotation(final Field field) {
        if (!field.isAnnotationPresent(FindBy.class)) {
            throw new InitializationException("No annotation 'FindBy' present under field '{}'", field.getName());
        }
    }
}
