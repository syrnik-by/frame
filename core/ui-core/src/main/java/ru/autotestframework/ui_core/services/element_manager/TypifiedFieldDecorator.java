package ru.autotestframework.ui_core.services.element_manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.services.table_service.FindCellsBy;
import ru.autotestframework.ui_core.services.table_service.FindHeadersBy;
import ru.autotestframework.ui_core.services.table_service.ITable;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Typified field decorator.
 */
@Slf4j
public class TypifiedFieldDecorator implements FieldDecorator {

    private ElementLocatorFactory factory;
    private Class<?> pageClass;

    /**
     * Instantiates a new Typified field decorator.
     *
     * @param factory   the factory
     * @param pageClass the page class
     */
    public TypifiedFieldDecorator(final ElementLocatorFactory factory, final Class<?> pageClass) {
        this.factory = factory;
        this.pageClass = pageClass;
    }

    /**
     * Gets super class names.
     *
     * @param clazz the clazz
     * @return the super class names
     */
    public static List<String> getSuperClassNames(Class<?> clazz) {
        List<String> listSuperClass = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            if (superclass.equals(java.lang.Object.class)) {
                break;
            }
            listSuperClass.add(superclass.getSimpleName());
            superclass = superclass.getSuperclass();
        }
        return listSuperClass;
    }

    @Override
    public Object decorate(final ClassLoader loader, final Field field) {
        if (!(IElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        if (IElement.class.isAssignableFrom(field.getType())) {
            return proxyForLocator(loader, locator, field);
        }
        if (List.class.isAssignableFrom(field.getType())) {
            return proxyForLocator(loader, locator);
        }
        return null;
    }

    /**
     * Is decoratable list boolean.
     *
     * @param field the field
     * @return the boolean
     */
    protected boolean isDecoratableList(final Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        var genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }

        return field.getAnnotation(FindBy.class) != null
                || field.getAnnotation(FindBys.class) != null
                || field.getAnnotation(FindAll.class) != null;
    }

    /**
     * Proxy for locator object.
     *
     * @param loader  the loader
     * @param locator the locator
     * @param field   the field
     * @return the object
     */
    @SneakyThrows
    protected Object proxyForLocator(final ClassLoader loader, final ElementLocator locator, final Field field) {
        InvocationHandler handler = new LocatingElementHandler(locator);

        IElement proxy;
        proxy = (IElement) Proxy.newProxyInstance(
                loader, new Class[] {IElement.class, WrapsElement.class, Locatable.class}, handler);
        if (field.getType().isAssignableFrom(IElement.class)) {
            return proxy;
        }
        return getInstanceByFieldType(proxy, field);
    }

    /**
     * Proxy for locator list.
     *
     * @param loader  the loader
     * @param locator the locator
     * @return the list
     */
    @SuppressWarnings("unchecked")
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
                                    ? pageClass.getMethod("getTitle").invoke(pageClass)
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
