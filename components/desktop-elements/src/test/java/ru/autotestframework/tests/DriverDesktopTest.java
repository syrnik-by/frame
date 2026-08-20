package ru.autotestframework.tests;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Rectangle;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.desktop_elements.driver_manager.drivers.DriverDesktop;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

@Tag("@DesktopElements")
class DriverDesktopTest {

    @BeforeAll
    static void setProperty() {
        System.setProperty("framework.ui.timeout", "5");
    }

    @Test
    void getElementRectPositiveTest() {
        TypifiedDesktopElement typifiedDesktopElement = Mockito.mock(TypifiedDesktopElement.class);
        Mockito.when(typifiedDesktopElement.withTimeout(0)).thenReturn(typifiedDesktopElement);
        SelenideElement selenideElement = Mockito.mock(SelenideElement.class);
        Mockito.when(typifiedDesktopElement.getSelenideElement()).thenReturn(selenideElement);
        Mockito.doCallRealMethod().when(typifiedDesktopElement).getRect();
        Mockito.when(selenideElement.getAttribute("BoundingRectangle")).thenReturn("10,20,30,40");
        Double x = 10d;
        Double y = 20d;
        Double width = 30d;
        Double height = 40d;
        Rectangle rectangle = DriverDesktop.getElementRect(typifiedDesktopElement);
        Assertions.assertEquals(rectangle.getX(), x);
        Assertions.assertEquals(rectangle.getY(), y);
        Assertions.assertEquals(rectangle.getWidth(), width);
        Assertions.assertEquals(rectangle.getHeight(), height);
    }

    @Test
    void getElementRectNegativeWidthTest() {
        TypifiedDesktopElement typifiedDesktopElement = Mockito.mock(TypifiedDesktopElement.class);
        Mockito.when(typifiedDesktopElement.withTimeout(0)).thenReturn(typifiedDesktopElement);
        SelenideElement selenideElement = Mockito.mock(SelenideElement.class);
        Mockito.when(typifiedDesktopElement.getSelenideElement()).thenReturn(selenideElement);
        Mockito.when(selenideElement.getAttribute("BoundingRectangle")).thenReturn("10,20,0,40");
        Mockito.doCallRealMethod().when(typifiedDesktopElement).getRect();
        Assertions.assertThrows(AutotestException.class, () -> DriverDesktop.getElementRect(typifiedDesktopElement));
    }

    @Test
    void getElementRectNegativeHeightTest() {
        TypifiedDesktopElement typifiedDesktopElement = Mockito.mock(TypifiedDesktopElement.class);
        Mockito.when(typifiedDesktopElement.withTimeout(0)).thenReturn(typifiedDesktopElement);
        SelenideElement selenideElement = Mockito.mock(SelenideElement.class);
        Mockito.when(typifiedDesktopElement.getSelenideElement()).thenReturn(selenideElement);
        Mockito.when(selenideElement.getAttribute("BoundingRectangle")).thenReturn("10,20,30,0");
        Mockito.doCallRealMethod().when(typifiedDesktopElement).getRect();
        Assertions.assertThrows(AutotestException.class, () -> DriverDesktop.getElementRect(typifiedDesktopElement));
    }
}
