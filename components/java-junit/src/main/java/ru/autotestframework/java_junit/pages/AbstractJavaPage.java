package ru.autotestframework.java_junit.pages;

import java.lang.reflect.Field;
import lombok.SneakyThrows;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;

public class AbstractJavaPage extends AbstractPage {

    // TODO Временное переопределение до перехода на SelenideElement с его умными ожиданиями
    @SneakyThrows
    @Override
    public TypifiedJavaElement getElementByTitle(final String elementTitle) {
        for (final Field field : getClass().getFields()) {
            if (field.isAnnotationPresent(Element.class)
                    && elementTitle.equals(field.getAnnotation(Element.class).value())) {
                field.setAccessible(true);
                TypifiedJavaElement element = (TypifiedJavaElement) field.get(this);
                element.waitElement();
                return element;
            }
        }
        throw new InitializationException(
                "No element '{}' declared on page '{}'",
                elementTitle,
                getClass().getSimpleName());
    }
}
