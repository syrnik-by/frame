package ru.autotestframework.cucumber.page_manager;

import com.google.common.reflect.ClassPath;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.ElementFactory;
import ru.autotestframework.ui_core.page_manager.Page;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.ui_core.services.table_service.table_manager.TablesManager;
import ru.autotestframework.util.StringUtil;

/**
 * The class implements the storage of all project pages.
 * In the terminology of autotesting, the page is also designated by the word "context".
 */
@Slf4j
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class PageManager {
    private final Map<String, Class<? extends Page>> registeredPageClasses = new HashMap<>();
    private final DriverContainer driverContainer;
    private final TablesManager tablesManager;

    /**
     * The Need throw on doubles.
     */
    @Value("${framework.ui.needThrowOnDoubles:false}")
    boolean needThrowOnDoubles;

    private String[] pagePackages;
    private Page current;

    /**
     * Constructor.
     *
     * @param driverContainer bin with driver.
     * @param tablesManager   the tables manager
     * @param configuration the config that is needed for the property, an array of Package strings that need to be scanned to find Page classes
     */
    public PageManager(
            final DriverContainer driverContainer, final TablesManager tablesManager, UiProperties configuration) {
        this.driverContainer = driverContainer;
        this.pagePackages = configuration.getPagePackage();
        Objects.requireNonNull(pagePackages, "Parameter 'framework.ui.page.package' not set");
        this.tablesManager = tablesManager;
        scan();
    }

    /**
     * Sets current.
     *
     * @param page the object of the page to make the current context.
     * @return returns the same context for further work
     */
    public Page setCurrent(final Page page) {
        tablesManager.setCurrentPageTables(page);
        return setCurrent(page.getClass());
    }

    /**
     * Sets current.
     *
     * @param pageClass the page class
     * @return the current
     */
    public Page setCurrent(final Class<? extends Page> pageClass) {
        current = getPageByClass(pageClass);
        return current;
    }

    /**
     * Gets current.
     *
     * @return returns the current context
     */
    public Page getCurrent() {
        if (Objects.isNull(current)) {
            throw new InitializationException("Current page not set");
        }
        return current;
    }

    /**
     * Gets registered page classes.
     *
     * @return the registered page classes
     */
    public Map<String, Class<? extends Page>> getRegisteredPageClasses() {
        return Collections.unmodifiableMap(registeredPageClasses);
    }

    /**
     * Sets the context by its name.
     *
     * @param title page title
     * @return returns the context
     */
    public Page getPageByTitle(final String title) {
        if (!registeredPageClasses.containsKey(title)) {
            throw new InitializationException("Page with title '{}' not found", title);
        }
        current = getPageByClass(registeredPageClasses.get(title));
        tablesManager.setCurrentPageTables(current);
        return current;
    }

    /**
     * Sets the context by the name of the page class.
     *
     * @param <T>       generic class
     * @param pageClass page class name
     * @return returns the context
     */
    @SuppressWarnings("unchecked")
    public <T extends Page> T getPageByClass(final Class<T> pageClass) {
        if (!registeredPageClasses.containsValue(pageClass)) {
            throw new InitializationException("Page with class name '{}' not found", pageClass.getName());
        }

        var page = ElementFactory.initElements(driverContainer, pageClass);

        checkDoubles(pageClass);
        return page;
    }

    private <T extends Page> void checkDoubles(Class<T> pageClass) {
        var fieldNames = Arrays.stream(FieldUtils.getFieldsWithAnnotation(pageClass, Element.class))
                .map(x -> x.getAnnotation(Element.class).value())
                .collect(Collectors.toList());

        var duplicates = fieldNames.stream()
                .filter(e -> Collections.frequency(fieldNames, e) > 1)
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            var message =
                    StringUtil.format("Page {} have duplicate named field(-s): {}", pageClass.getName(), duplicates);
            if (needThrowOnDoubles) {
                throw new ConfigurationException(message);
            }
            log.warn(message);
        }
    }

    /**
     * The method scans the project to get all the page classes..
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void scan() {
        final var multipleClassNameError = "You have multiple pages with the same class name '{}'";
        final var multipleTitleError = "You have multiple pages with the same title '{}'";
        final Class<? extends Page> parent = AbstractPage.class;

        Arrays.stream(pagePackages)
                .map(pp -> findAllClassesByPackage(pp))
                .flatMap(Set::stream)
                .filter(aClass -> !aClass.equals(parent) && parent.isAssignableFrom(aClass))
                .forEach(pageClass -> {
                    String title = pageClass.getName();
                    if (pageClass.isAnnotationPresent(PageEntry.class)) {
                        title = pageClass.getAnnotation(PageEntry.class).title();
                    }

                    if (registeredPageClasses.containsKey(title)) {
                        throw new InitializationException(multipleTitleError, title);
                    }
                    if (registeredPageClasses.containsValue(pageClass)) {
                        throw new InitializationException(multipleClassNameError, pageClass.getName());
                    }
                    registeredPageClasses.put(title, pageClass);
                });
    }

    @SneakyThrows
    private Set<Class<Page>> findAllClassesByPackage(String packageName) {
        return ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
                .filter(clazz -> clazz.getPackageName().contains(packageName))
                .map(clazz -> (Class<Page>) clazz.load())
                .collect(Collectors.toSet());
    }
}
