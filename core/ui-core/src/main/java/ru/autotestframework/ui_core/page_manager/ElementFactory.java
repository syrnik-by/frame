package ru.autotestframework.ui_core.page_manager;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.CoreSelenidePageFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.BaseElement;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Factory for page classes with switch / checks executed
 */
@Slf4j
public class ElementFactory extends PageFactory {
    /**
     * Init elements t.
     *
     * @param <T>              the type parameter
     * @param driverContainer  the driver container
     * @param pageClassToProxy the page class to proxy
     * @return the t
     */
    public static <T extends Page> T initElements(
            final DriverContainer driverContainer, final Class<T> pageClassToProxy) {
        var page = instantiatePage(driverContainer.get(), pageClassToProxy);
        page = initElements(page);
        return page;
    }

    /**
     * Init elements t.
     *
     * @param <T>  the type parameter
     * @param page the page
     * @return the t
     */
    public static <T extends Page> T initElements(T page) {
        page = init(page);
        page.switchTo();
        page.checkAcceptor();
        return page;
    }

    /**
     * Init t.
     *
     * @param <T>  the type parameter
     * @param page the page
     * @return the t
     */
    public static <T extends Page> T init(T page) {
        page = new CoreSelenidePageFactory().page(WebDriverRunner.driver(), page);
        T finalPage = page;
        Arrays.stream(FieldUtils.getAllFields(page.getClass())).forEach(field -> {
            field.setAccessible(true);
            try {
                if (IElement.class.isAssignableFrom(field.getType())) {
                    var baseElement = (BaseElement) field.get(finalPage);
                    baseElement.setAnnotations(field.getAnnotations());
                }
            } catch (Exception iae) {
                log.error("Error happened while trying to inject Element metaData (annotations)", iae);
            }
        });
        return page;
    }

    /**
     * Reinit t.
     *
     * @param <T>  the type parameter
     * @param page the page
     * @return the t
     */
    public static <T extends Page> T reinit(T page) {
        page = (T) getNewInstanceDeclaredConstructor(page.getClass());
        return init(page);
    }

    private static <T> T instantiatePage(final SearchContext searchContext, final Class<T> pageClassToProxy) {
        try {
            return pageClassToProxy.getConstructor(WebDriver.class).newInstance(searchContext);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            log.warn(e.getMessage());
        }
        return getNewInstanceDeclaredConstructor(pageClassToProxy);
    }

    private static <T> T getNewInstanceDeclaredConstructor(final Class<T> pageClassToProxy) {
        try {
            return pageClassToProxy.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new InitializationException("{} page initialization failure", e, pageClassToProxy.getCanonicalName());
        }
    }
}
