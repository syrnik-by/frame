package ru.autotestframework.ui_core.tests.services;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.services.ElementGrabber;

/**
 * Element grabber test.
 */
@Tag("@UiCore")
class ElementGrabberTest {

    /**
     * Gets element list by web driver test.
     */
    @Test
    void getElementListByWebDriverTest() {
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        WebElement webElement1 = Mockito.mock(WebElement.class);
        WebElement webElement2 = Mockito.mock(WebElement.class);
        List<WebElement> webElements = List.of(webElement1, webElement2);
        Mockito.when(webDriver.findElements(Mockito.any(By.class))).thenReturn(webElements);
        List<WebElement> webElementList = ElementGrabber.getElementList(webDriver);
        Assertions.assertEquals(webElements, webElementList);
    }

    /**
     * Gets element list by web element test.
     */
    @Test
    void getElementListByWebElementTest() {
        WebElement webElement = Mockito.mock(WebElement.class);
        WebElement webElement1 = Mockito.mock(WebElement.class);
        WebElement webElement2 = Mockito.mock(WebElement.class);
        List<WebElement> webElements = List.of(webElement1, webElement2);
        Mockito.when(webElement.findElements(Mockito.any(By.class))).thenReturn(webElements);
        List<WebElement> webElementList = ElementGrabber.getElementList(webElement);
        Assertions.assertEquals(webElements, webElementList);
    }

    /**
     * Element bounding rectangle to json file test.
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    void elementBoundingRectangleToJSONFileTest() throws IOException, ParseException {
        WebElement webElement = Mockito.mock(WebElement.class);
        List<WebElement> webElements = List.of(webElement);
        Mockito.when(webElement.getAttribute("BoundingRectangle")).thenReturn("1, 2, 3, 4");
        Mockito.when(webElement.getAttribute("ClassName")).thenReturn("testRectangle");
        ElementGrabber.elementBoundingRectangleToJSONFile(webElements, "elementName");
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader("elements.json")) {
            JSONObject data = (JSONObject) parser.parse(fileReader);
            Assertions.assertEquals(
                    "{\"elementName\":[{\"testRectangle\":{\"w\":\"4\",\"x\":\"1\",\"h\":\"3\",\"y\":\"2\"}}]}",
                    data.toJSONString());
        }
        Files.delete(Path.of("elements.json"));
    }
}
