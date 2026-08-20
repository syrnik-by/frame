package ru.autotestframework.ui_core.tests.page_manager;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import java.lang.reflect.Field;
import java.util.Optional;
import lombok.SneakyThrows;
import ru.autotestframework.ui_core.ElementResolver;
import ru.autotestframework.ui_core.page_manager.Element;

/**
 * Custom element resolver.
 */
public class CustomElementResolver implements ElementResolver {
    @SneakyThrows
    @Override
    public <T> Object resolve(Field field) {
        String dataId =
                field.getAnnotation(ElementResolverTests.FindByDataId.class).value();

        String title = Optional.ofNullable(field.getAnnotation(Element.class))
                .map(Element::value)
                .orElse(field.getName());

        return field.getType()
                .getConstructor(SelenideElement.class, String.class)
                .newInstance($x(dataId), title);
    }

    @Override
    public boolean skipApply(Field field) {
        return !field.isAnnotationPresent(ElementResolverTests.FindByDataId.class);
    }
}
