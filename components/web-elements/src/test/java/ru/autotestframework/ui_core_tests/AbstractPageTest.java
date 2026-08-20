package ru.autotestframework.ui_core_tests;

import com.codeborne.selenide.WebDriverRunner;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.pages.local.DragAndDropHtml5;
import ru.autotestframework.pages.local.DynamicTablePageForUnits;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.ElementFactory;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.web_elements.elements.Button;

@Tag("@webElemElements")
class AbstractPageTest {

    class TestPage extends AbstractPage {}

    DriverContainerImpl driverContainer = Mockito.mock(DriverContainerImpl.class);

    @Test
    void getTitlePositiveTest() {
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        Assertions.assertEquals("Dynamic HTML TABLE Tag", dynamicTablePage.getTitle());
    }

    @Test
    @Disabled("актуализация")
    void getTitleNegativeTest() {
        TestPage testPage = new TestPage();
        Assertions.assertThrows(InitializationException.class, testPage::getTitle);
    }

    @Test
    void getElementByTitlePositiveTest() {
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driverContainer.get()).thenReturn(webDriver);
        WebDriverRunner.setWebDriver(webDriver);
        DynamicTablePageForUnits dynamicTablePage =
                ElementFactory.initElements(driverContainer, DynamicTablePageForUnits.class);
        IElement table = dynamicTablePage.getElementByTitle("Table Data");
        Assertions.assertEquals("Table Data", table.getTitle());
    }

    @Test
    void getElementByTitleNegativeTest() {
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        Assertions.assertThrows(InitializationException.class, () -> dynamicTablePage.getElementByTitle("Data"));
    }

    @Disabled("актуализация")
    @Test
    void getElementsListByTitlePositiveTest() {
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driverContainer.get()).thenReturn(webDriver);
        DragAndDropHtml5 dragAndDropHtml5 = ElementFactory.initElements(driverContainer, DragAndDropHtml5.class);
        List<IElement> elementsListByTitle = dragAndDropHtml5.getElementsListByTitle("Drag Elements");
        Assertions.assertEquals(2, elementsListByTitle.size());
        elementsListByTitle.forEach(element -> Assertions.assertEquals("draggableList", element.getTitle()));
    }

    @Test
    void getElementsListByTitleNegativeTest() {
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        Assertions.assertThrows(InitializationException.class, () -> dynamicTablePage.getElementsListByTitle("Data"));
    }

    @Test
    void getElementPositiveTest() {
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driverContainer.get()).thenReturn(webDriver);
        WebDriverRunner.setWebDriver(webDriver);
        DynamicTablePageForUnits dynamicTablePage =
                ElementFactory.initElements(driverContainer, DynamicTablePageForUnits.class);
        Button table_data = dynamicTablePage.getElement("Table Data", Button.class);
        Assertions.assertEquals("Table Data", table_data.getTitle());
    }

    @Test
    @Disabled("актуализация")
    void getElementNegativeTest() {
        DynamicTablePageForUnits dynamicTablePage = new DynamicTablePageForUnits();
        Assertions.assertThrows(
                InitializationException.class, () -> dynamicTablePage.getElement("Table Data", Button.class));
    }

    @Test
    void getElementsByTypeTest() {
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driverContainer.get()).thenReturn(webDriver);
        WebDriverRunner.setWebDriver(webDriver);
        DynamicTablePageForUnits dynamicTablePage =
                ElementFactory.initElements(driverContainer, DynamicTablePageForUnits.class);
        Map<String, IVerifiable> elements = dynamicTablePage.getElementsByType(IVerifiable.class);
        Assertions.assertEquals(4, elements.size());
    }
}
