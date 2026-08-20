package ru.autotestframework.ui_core_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.support.PageFactory;
import ru.autotestframework.pages.local.DynamicTablePageForUnits;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.services.element_locator.IElementLocatorFactory;
import ru.autotestframework.ui_core.services.element_manager.TypifiedFieldDecorator;
import ru.autotestframework.web_elements.elements.Button;
import ru.autotestframework.web_elements.elements.TextInput;
import ru.autotestframework.web_elements.elements.WebTable;

@Tag("@webElemElements")
class ElementFactoryTest {

    @Test
    void initElementsTest() {
        var driverContainer = Mockito.mock(DriverContainerImpl.class);

        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        PageFactory.initElements(
                new TypifiedFieldDecorator(
                        new IElementLocatorFactory(driverContainer.get()), DynamicTablePageForUnits.class),
                dynamicTablePage);

        Assertions.assertTrue(dynamicTablePage.getElementByTitle("Table") instanceof WebTable);
        Assertions.assertTrue(dynamicTablePage.getElementByTitle("Table Data") instanceof Button);
        Assertions.assertTrue(dynamicTablePage.getElementByTitle("Text Area") instanceof TextInput);
        Assertions.assertTrue(dynamicTablePage.getElementByTitle("Caption") instanceof TextInput);
        Assertions.assertTrue(dynamicTablePage.getElementByTitle("Refresh Table") instanceof Button);
    }
}
