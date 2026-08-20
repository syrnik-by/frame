package ru.autotestframework.allure_elements.tests;

import com.codeborne.selenide.conditions.Visible;
import io.qameta.allure.Allure;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.TestResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.allure_elements.ElementsAspects;
import ru.autotestframework.ui_core.typified_elements.BaseElement;

public class ElementsAspectsTests {

    @Test
    void clickTest() {
        ElementsAspects elementsAspects = new ElementsAspects();
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        TestElement testElement = new TestElement("testElement");
        Mockito.when(joinPoint.getTarget()).thenReturn(testElement);
        elementsAspects.click(joinPoint);
        AllureStorage allureStorage = (AllureStorage) ReflectionTestUtils.getField(Allure.getLifecycle(), "storage");
        Map<String, Object> storage = (Map<String, Object>) ReflectionTestUtils.getField(allureStorage, "storage");
        List<Object> collect = storage.keySet().stream()
                .filter(k -> storage.get(k) instanceof TestResult)
                .map(storage::get)
                .collect(Collectors.toList());
        String name = ((TestResult) collect.get(0)).getSteps().get(0).getName();
        Assertions.assertEquals("Нажимает на testElement", name);
    }

    @Test
    void sendKeysTest() {
        ElementsAspects elementsAspects = new ElementsAspects();
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        TestElement testElement = new TestElement("testElement");
        Mockito.when(joinPoint.getTarget()).thenReturn(testElement);
        Object[] args = {"A", "B", "C"};
        Mockito.when(joinPoint.getArgs()).thenReturn(args);
        elementsAspects.sendKeys(joinPoint);
        AllureStorage allureStorage = (AllureStorage) ReflectionTestUtils.getField(Allure.getLifecycle(), "storage");
        Map<String, Object> storage = (Map<String, Object>) ReflectionTestUtils.getField(allureStorage, "storage");
        List<Object> collect = storage.keySet().stream()
                .filter(k -> storage.get(k) instanceof TestResult)
                .map(storage::get)
                .collect(Collectors.toList());
        String name = ((TestResult) collect.get(0)).getSteps().get(0).getName();
        Assertions.assertEquals("Отправляет A B C в testElement", name);
    }

    @Test
    void shouldBeNotReversedTest() {
        ElementsAspects elementsAspects = new ElementsAspects();
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        TestElement testElement = new TestElement("testElement");
        Mockito.when(joinPoint.getTarget()).thenReturn(testElement);
        Visible condition = new Visible();
        Object[] args = {condition, true};
        Mockito.when(joinPoint.getArgs()).thenReturn(args);
        elementsAspects.shouldBe(joinPoint);
        AllureStorage allureStorage = (AllureStorage) ReflectionTestUtils.getField(Allure.getLifecycle(), "storage");
        Map<String, Object> storage = (Map<String, Object>) ReflectionTestUtils.getField(allureStorage, "storage");
        List<Object> collect = storage.keySet().stream()
                .filter(k -> storage.get(k) instanceof TestResult)
                .map(storage::get)
                .collect(Collectors.toList());
        String name = ((TestResult) collect.get(0)).getSteps().get(0).getName();
        Assertions.assertEquals("Элемент testElement  должен быть visible", name);
    }

    @Test
    void shouldBeReversedTest() {
        ElementsAspects elementsAspects = new ElementsAspects();
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        TestElement testElement = new TestElement("testElement");
        Mockito.when(joinPoint.getTarget()).thenReturn(testElement);
        Visible condition = new Visible();
        Object[] args = {condition, false};
        Mockito.when(joinPoint.getArgs()).thenReturn(args);
        elementsAspects.shouldBe(joinPoint);
        AllureStorage allureStorage = (AllureStorage) ReflectionTestUtils.getField(Allure.getLifecycle(), "storage");
        Map<String, Object> storage = (Map<String, Object>) ReflectionTestUtils.getField(allureStorage, "storage");
        List<Object> collect = storage.keySet().stream()
                .filter(k -> storage.get(k) instanceof TestResult)
                .map(storage::get)
                .collect(Collectors.toList());
        String name = ((TestResult) collect.get(0)).getSteps().get(0).getName();
        Assertions.assertEquals("Элемент testElement не должен быть visible", name);
    }

    class TestElement extends BaseElement {

        private final String elementTitle;

        public TestElement(String elementTitle) {
            this.elementTitle = elementTitle;
        }

        @Override
        public String getTitle() {
            return elementTitle;
        }

        @Override
        public void doubleClick() {}

        @Override
        public void rightClick() {}

        @Override
        public void hover() {}

        @Override
        public void hasAttribute(String attribute) {}

        @Override
        public void click() {}

        @Override
        public void submit() {}

        @Override
        public void sendKeys(CharSequence... keysToSend) {}

        @Override
        public void clear() {}

        @Override
        public String getTagName() {
            return "";
        }

        @Override
        public String getAttribute(String name) {
            return "";
        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public List<WebElement> findElements(By by) {
            return List.of();
        }

        @Override
        public WebElement findElement(By by) {
            return null;
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }

        @Override
        public Point getLocation() {
            return null;
        }

        @Override
        public Dimension getSize() {
            return null;
        }

        @Override
        public Rectangle getRect() {
            return null;
        }

        @Override
        public String getCssValue(String propertyName) {
            return "";
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
            return null;
        }

        @Override
        public WebElement getWrappedElement() {
            return null;
        }

        @Override
        public Coordinates getCoordinates() {
            return null;
        }
    }
}
