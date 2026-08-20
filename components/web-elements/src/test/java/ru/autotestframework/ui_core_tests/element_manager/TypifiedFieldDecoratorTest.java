package ru.autotestframework.ui_core_tests.element_manager;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ru.autotestframework.pages.local.DynamicTablePageForUnits;
import ru.autotestframework.ui_core.services.element_manager.TypifiedFieldDecorator;
import ru.autotestframework.web_elements.elements.WebTable;

@Tag("@webElemElements")
class TypifiedFieldDecoratorTest {

    @Test
    void getSuperClassNamesTest() {
        List<String> expectedSuperClassNames = List.of("BaseWebTable", "TypifiedWebElement", "BaseElement");
        List<String> superClassNames = TypifiedFieldDecorator.getSuperClassNames(WebTable.class);
        Assertions.assertEquals(expectedSuperClassNames, superClassNames);
    }

    @Test
    void decorateTest() throws NoSuchFieldException {
        ElementLocatorFactory elementLocatorFactory = Mockito.spy(ElementLocatorFactory.class);
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        TypifiedFieldDecorator typifiedFieldDecorator =
                new TypifiedFieldDecorator(elementLocatorFactory, DynamicTablePageForUnits.class);
        ClassLoader classLoader = dynamicTablePage.getClass().getClassLoader();
        Field declaredField = dynamicTablePage.getClass().getDeclaredField("table");
        ElementLocator elementLocator = Mockito.mock(ElementLocator.class);
        Mockito.when(elementLocatorFactory.createLocator(declaredField)).thenReturn(elementLocator);
        Object decorate = typifiedFieldDecorator.decorate(classLoader, declaredField);
        Assertions.assertTrue(decorate instanceof WebTable);
        Assertions.assertEquals("Table", ((WebTable) decorate).getTitle());
    }
}
