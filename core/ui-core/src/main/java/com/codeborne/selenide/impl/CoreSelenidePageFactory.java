package com.codeborne.selenide.impl;

import static ru.autotestframework.ui_core.UiCoreUtils.getElementResolvers;
import static ru.autotestframework.ui_core.services.element_manager.TypifiedFieldDecorator.getSuperClassNames;

import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.services.element_locator.IElementLocatorFactory;
import ru.autotestframework.ui_core.services.element_manager.FindByControl;
import ru.autotestframework.ui_core.services.element_manager.FindByScreen;
import ru.autotestframework.ui_core.services.table_service.FindCellsBy;
import ru.autotestframework.ui_core.services.table_service.FindHeadersBy;
import ru.autotestframework.ui_core.services.table_service.ITable;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Factory class to make using Page Objects simpler and easier (extended from {@link SelenidePageFactory} to work with custom annotations
 *
 * @see <a href="https://github.com/SeleniumHQ/selenium/wiki/PageObjects">Page Objects Wiki</a>
 */
@Slf4j
@ParametersAreNonnullByDefault
public class CoreSelenidePageFactory extends SelenidePageFactory {
    /**
     * The Page.
     */
    Object page;

    @Override
    @CheckReturnValue
    @Nonnull
    public <PageObjectClass, T extends PageObjectClass> PageObjectClass page(Driver driver, T pageObject) {
        page = pageObject;
        return super.page(driver, pageObject);
    }

    // TODO fix for selenide not exist etc error (when using $(webElem))
    @CheckReturnValue
    @Nullable
    @SneakyThrows
    @Override
    public Object decorate(
            ClassLoader loader,
            Driver driver,
            @Nullable WebElementSource searchContext,
            Field field,
            By selector,
            Type[] genericTypes) {
        if (ElementsContainer.class.equals(field.getDeclaringClass()) && "self".equals(field.getName())) {
            if (searchContext != null) {
                return ElementFinder.wrap(SelenideElement.class, searchContext);
            } else {
                log.warn("Cannot initialize field {}", field);
                return null;
            }
        }

        SearchContext context = searchContext == null
                ? Optional.ofNullable(driver).map(x -> x.getWebDriver()).orElse(null)
                : searchContext.getWebElement();

        // TODO remove to resolver
        if (IElement.class.isAssignableFrom(field.getType())) {
            ElementLocator el = new IElementLocatorFactory(context).createLocator(field);
            InvocationHandler handler = new LocatingElementHandler(el);
            var proxy = Proxy.newProxyInstance(
                    loader, new Class[] {IElement.class, WrapsElement.class, Locatable.class}, handler);
            if (field.getType().isAssignableFrom(IElement.class)) {
                return proxy;
            }
            return getInstanceByFieldType((IElement) proxy, field);
        }

        if (WebElement.class.isAssignableFrom(field.getType())) {
            var elem = ElementFinder.wrap(driver, searchContext, selector, 0);
            if (field.isAnnotationPresent(Element.class)) {
                return elem.as(field.getAnnotation(Element.class).value());
            }
            return elem;
        }

        if (isDecoratableList(field, genericTypes, IElement.class)) {
            ElementLocator el = new IElementLocatorFactory(context).createLocator(field);
            return proxyForLocator(loader, el);
        }

        if (ElementsCollection.class.isAssignableFrom(field.getType())
                || isDecoratableList(field, genericTypes, WebElement.class)) {
            return new ElementsCollection(new BySelectorCollection(driver, searchContext, selector));
        } else if (ElementsContainer.class.isAssignableFrom(field.getType())) {
            return createElementsContainer(driver, searchContext, field, selector);
        } else if (isDecoratableList(field, genericTypes, ElementsContainer.class)) {
            return createElementsContainerList(driver, searchContext, field, genericTypes, selector);
        }

        var result = getElementResolvers().stream()
                .filter(x -> !x.skipApply(field))
                .findFirst()
                .map(x -> x.resolve(field));
        if (result.isPresent() || searchContext == null) {
            return result.orElse(null);
        }

        return defaultFieldDecorator(driver, searchContext).decorate(loader, field);
    }

    /**
     * Proxy for locator list.
     *
     * @param loader  the loader
     * @param locator the locator
     * @return the list
     */
    protected List<IElement> proxyForLocator(final ClassLoader loader, final ElementLocator locator) {
        InvocationHandler handler = new LocatingElementListHandler(locator);
        List<IElement> proxy;
        proxy = (List<IElement>) Proxy.newProxyInstance(loader, new Class[] {List.class}, handler);
        return proxy;
    }

    /**
     * Gets instance by field type.
     *
     * @param proxy the proxy
     * @param field the field
     * @return the instance by field type
     */
    @SneakyThrows
    protected Object getInstanceByFieldType(final IElement proxy, final Field field) {
        List<String> classNames = getSuperClassNames(field.getType());
        String title = field.getName();
        if (field.isAnnotationPresent(Element.class)) {
            title = field.getAnnotation(Element.class).value();
        }
        // TODO classloader strategy on Field inside of each Components (for ability to enhance this logic (any other
        // application type on libuser side)
        if (classNames.contains("TypifiedScreenElement")) {
            return field.getType()
                    .getConstructor(String.class, String.class, String.class, int.class, int.class, int.class)
                    .newInstance(
                            title,
                            field.getAnnotation(FindByScreen.class).regionLocation(),
                            field.getAnnotation(FindByScreen.class).location(),
                            field.getAnnotation(FindByScreen.class).searchType(),
                            field.getAnnotation(FindByScreen.class).offsetX(),
                            field.getAnnotation(FindByScreen.class).offsetY());
        }
        if (classNames.contains("TypifiedAutoItElement")) {
            return field.getType()
                    .getConstructor(String.class, String.class, String.class)
                    .newInstance(
                            field.getAnnotation(FindByControl.class).winTitle().isEmpty()
                                    ? page.getClass().getMethod("getTitle").invoke(page)
                                    : field.getAnnotation(FindByControl.class).winTitle(),
                            title,
                            field.getAnnotation(FindByControl.class).control());
        }

        try {
            if (ITable.class.isAssignableFrom((Class) field.getGenericType())) {
                return field.getType()
                        .getConstructor(WebElement.class, String.class, String.class, String.class)
                        .newInstance(
                                proxy,
                                title,
                                field.getAnnotation(FindHeadersBy.class).xpath(),
                                field.getAnnotation(FindCellsBy.class).xpath());
            }
        } catch (Exception e) {
            log.warn("Custom table used: {}", title, e);
        }

        return field.getType().getConstructor(WebElement.class, String.class).newInstance(proxy, title);
    }
}
