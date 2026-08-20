package ru.autotestframework.ui_core.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebElement;

@UtilityClass
public class ScreenshotRegistry {

    private static Set<String> webElementMap = new LinkedHashSet<>();

    public static void registerPage(Object pageObject) {
        Class<?> clazz = pageObject.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ScreenWithHighlight.class)
                        && WebElement.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    try {
                        WebElement element = (WebElement) field.get(pageObject);
                        if (element != null) {
                            Method getWrappedElement = element.getClass().getMethod("getWrappedElement");
                            webElementMap.add(LocatorExtractor.extractLocator(
                                    getWrappedElement.invoke(element).toString()));
                        }
                    } catch (Exception e) {

                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public static boolean shouldTakeScreenshot(WebElement webElement) {
        String locator = LocatorExtractor.extractLocator(webElement.toString());
        if (webElementMap.contains(locator)) {
            webElementMap.remove(locator);
            return true;
        }
        return false;
    }
}
