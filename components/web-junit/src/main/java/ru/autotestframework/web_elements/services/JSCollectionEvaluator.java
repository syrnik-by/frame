package ru.autotestframework.web_elements.services;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.JavaScript;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JSCollectionEvaluator {
    static ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    /**
     * Get attributes from all elements on xpath
     * @param xpath
     * @param attribute
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> Object evaluate(String xpath, String attribute) {
        return new JavaScript("js/xpathAttributeEvaluator.js")
                .execute(WebDriverRunner.getWebDriver(), xpath, attribute);
    }

    @SneakyThrows
    @Deprecated
    /**
     * too slow due transformation each webElem to browser js elementNode representation
     */
    public static <T> Object evaluate2(String attribute, AbstractList<T> elements) {
        try (InputStream is = classloader.getResourceAsStream("test.csv")) {
            return new JavaScript("js/elemsAttrEvaluator.js")
                    .execute(
                            WebDriverRunner.getWebDriver(),
                            concatWithStream(new Object[] {attribute}, elements.toArray()));
        }
    }

    public static Object[] concatWithStream(Object[] array1, Object[] array2) {
        return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                .toArray(size -> (Object[]) Array.newInstance(array1.getClass().getComponentType(), size));
    }

    public static List<String> texts(String xpath) {
        return (List<String>) evaluate(xpath, "textContent");
    }
}
