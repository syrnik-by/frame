package ru.autotestframework.ui_core.services;

import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Element grabber.
 */
@Slf4j
@UtilityClass
public class ElementGrabber {

    /**
     * Gets element list.
     *
     * @param driver the driver
     * @return the element list
     */
    public static List<WebElement> getElementList(final WebDriver driver) {
        return driver.findElements(By.xpath("//*"));
    }

    /**
     * Gets element list.
     *
     * @param element the element
     * @return the element list
     */
    public static List<WebElement> getElementList(final WebElement element) {
        return element.findElements(By.xpath("//*"));
    }

    /**
     * Element bounding rectangle to json file.
     *
     * @param elements     the elements
     * @param elementsName the elements name
     * @param fileName     the file name
     * @throws IOException the io exception
     */
    public static void elementBoundingRectangleToJSONFile(
            final List<WebElement> elements, final String elementsName, final String fileName) throws IOException {

        var completeObject = new JSONObject();
        var jsonArray = new JSONArray();
        var writeObject = new JSONObject();
        var gson = new GsonBuilder().setPrettyPrinting().create();

        for (WebElement e : elements) {
            var jsonObject = new JSONObject();
            String[] s1 = e.getAttribute("BoundingRectangle").split(", ");
            jsonObject.put("x", s1[0]);
            jsonObject.put("y", s1[1]);
            jsonObject.put("h", s1[2]);
            jsonObject.put("w", s1[3]);
            completeObject.put(e.getAttribute("ClassName"), jsonObject);
        }

        jsonArray.add(completeObject);
        writeObject.put(elementsName, jsonArray);

        try (var file = new FileWriter(fileName)) {
            file.write(gson.toJson(writeObject));
            log.info("Successfully Copied JSON Object to File...");
            file.flush();
        }
    }

    /**
     * Element bounding rectangle to json file.
     *
     * @param elements     the elements
     * @param elementsName the elements name
     * @throws IOException the io exception
     */
    public static void elementBoundingRectangleToJSONFile(final List<WebElement> elements, final String elementsName)
            throws IOException {
        elementBoundingRectangleToJSONFile(elements, elementsName, "elements.json");
    }
}
