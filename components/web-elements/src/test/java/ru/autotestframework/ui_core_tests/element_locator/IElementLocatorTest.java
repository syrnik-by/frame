package ru.autotestframework.ui_core_tests.element_locator;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.services.element_locator.IElementLocator;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

@Tag("@webElemElements")
class IElementLocatorTest {

    static IElementLocator iElementLocator;
    static Annotations abstractAnnotations;
    static SearchContext searchContext;
    static By by;

    @BeforeAll
    static void setUp() {
        searchContext = Mockito.spy(SearchContext.class);
        Driver driver = Mockito.mock(Driver.class);
        abstractAnnotations = Mockito.mock(Annotations.class);
        Field field = Mockito.mock(Field.class);
        Mockito.doReturn(TypifiedWebElement.class).when(field).getType();
        Element element = Mockito.spy(Element.class);
        Mockito.when(field.getAnnotation(Element.class)).thenReturn(element);
        Mockito.when(element.value()).thenReturn("elementTitle");
        Mockito.when(field.isAnnotationPresent(Element.class)).thenReturn(true);
        by = Mockito.spy(By.class);
        Mockito.when(abstractAnnotations.buildBy()).thenReturn(by);
        iElementLocator = new IElementLocator(searchContext, abstractAnnotations, field);
    }

    @AfterEach
    void remove() {
        ReflectionTestUtils.setField(iElementLocator, "cachedElement", null);
        ReflectionTestUtils.setField(iElementLocator, "cachedElementList", null);
    }

    @Test
    void findElementShouldCacheAndCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", true);
        WebElement cachedElement = Mockito.mock(WebElement.class);
        ReflectionTestUtils.setField(iElementLocator, "cachedElement", cachedElement);
        Assertions.assertEquals(cachedElement, iElementLocator.findElement());
    }

    @Test
    void findElementShouldCacheAndNotCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", true);
        WebElement cachedElement = Mockito.mock(WebElement.class);
        Mockito.when(searchContext.findElement(by)).thenReturn(cachedElement);
        Assertions.assertEquals(cachedElement, iElementLocator.findElement());
        WebElement element = (WebElement) ReflectionTestUtils.getField(iElementLocator, "cachedElement");
        Assertions.assertEquals(cachedElement, element);
    }

    @Test
    void findElementShouldNotCacheAndNotCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", false);
        WebElement cachedElement = Mockito.mock(WebElement.class);
        Mockito.when(searchContext.findElement(by)).thenReturn(cachedElement);
        Assertions.assertEquals(cachedElement, iElementLocator.findElement());
        WebElement element = (WebElement) ReflectionTestUtils.getField(iElementLocator, "cachedElement");
        Assertions.assertNull(element);
    }

    @Test
    void findElementsShouldCacheAndCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", true);
        WebElement cachedElement1 = Mockito.mock(WebElement.class);
        WebElement cachedElement2 = Mockito.mock(WebElement.class);
        List<WebElement> cachedElements = List.of(cachedElement1, cachedElement2);
        ReflectionTestUtils.setField(iElementLocator, "cachedElementList", cachedElements);
        Assertions.assertEquals(cachedElements, iElementLocator.findElements());
    }

    @Test
    void findElementsShouldCacheAndNotCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", true);
        WebElement cachedElement1 = Mockito.mock(WebElement.class);
        WebElement cachedElement2 = Mockito.mock(WebElement.class);
        List<WebElement> cachedElements = List.of(cachedElement1, cachedElement2);
        Mockito.when(searchContext.findElements(by)).thenReturn(cachedElements);
        List<WebElement> elementsList = iElementLocator.findElements();
        Assertions.assertEquals(2, elementsList.size());
        elementsList.forEach(
                element -> Assertions.assertEquals("elementTitle", ((TypifiedWebElement) element).getTitle()));
        List<WebElement> elements =
                (List<WebElement>) ReflectionTestUtils.getField(iElementLocator, "cachedElementList");
        Assertions.assertEquals(elementsList, elements);
    }

    @Test
    void findElementsShouldNotCacheAndNotCachedTest() {
        ReflectionTestUtils.setField(iElementLocator, "shouldCache", false);
        WebElement cachedElement1 = Mockito.mock(WebElement.class);
        WebElement cachedElement2 = Mockito.mock(WebElement.class);
        List<WebElement> cachedElements = List.of(cachedElement1, cachedElement2);
        Mockito.when(searchContext.findElements(by)).thenReturn(cachedElements);
        List<WebElement> elementsList = iElementLocator.findElements();
        Assertions.assertEquals(2, elementsList.size());
        elementsList.forEach(
                element -> Assertions.assertEquals("elementTitle", ((TypifiedWebElement) element).getTitle()));
        List<WebElement> elements =
                (List<WebElement>) ReflectionTestUtils.getField(iElementLocator, "cachedElementList");
        Assertions.assertNull(elements);
    }
}
