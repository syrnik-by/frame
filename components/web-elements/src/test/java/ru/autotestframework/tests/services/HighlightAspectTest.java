package ru.autotestframework.tests.services;

import static ru.autotestframework.web_elements.services.HighlightAspectService.HIGHLIGHTING_ATTRIBUTE;
import static ru.autotestframework.web_elements.services.HighlightAspectService.STYLE_BORDER;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.services.JSExecutor;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.web_elements.services.HighlightAspectService;

@Tag("@webElemElements")
class HighlightAspectTest {
    HighlightAspectService highlightAspect = new HighlightAspectService();
    JSExecutor jsExecutor = Mockito.mock(JSExecutor.class);
    WebElement iElement = Mockito.mock(WebElement.class);
    ArgumentCaptor<String> scriptCaptor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        ReflectionTestUtils.setField(highlightAspect, "jsExecutor", jsExecutor);
    }

    @Test
    void highlightOffTest() {
        highlightAspect.afterAnyWebElementCall(
                iElement, Mockito.mock(Method.class), new Object[0], Mockito.mock(Object.class));
        Mockito.when(jsExecutor.executeJavaScript(Mockito.anyString(), Mockito.any(IElement.class)))
                .thenReturn("jsResult");
        Mockito.verify(jsExecutor, Mockito.times(1))
                .executeJavaScript(Mockito.anyString(), Mockito.any(WebElement.class));
        Mockito.verify(iElement, Mockito.times(1)).getAttribute(HIGHLIGHTING_ATTRIBUTE);
    }

    @Test
    void highlightOnTest() {
        Mockito.when(jsExecutor.executeJavaScript(Mockito.anyString(), Mockito.any(IElement.class)))
                .thenReturn("jsResult");
        highlightAspect.beforeAnyWebElementCall(iElement, Mockito.mock(Method.class), new Object[0]);
        Mockito.verify(jsExecutor, Mockito.times(1)).executeJavaScript(STYLE_BORDER, iElement);
        Mockito.verify(jsExecutor, Mockito.times(2))
                .executeJavaScript(scriptCaptor.capture(), Mockito.any(WebElement.class));
        Assertions.assertTrue(scriptCaptor.getValue().contains("red"));
    }
}
