package ru.autotestframework.ui_core.tests.page_manager;

import static org.mockito.ArgumentMatchers.anyString;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.junit.InjectedPage;
import ru.autotestframework.ui_core.page_manager.Element;

/**
 * Element resolver tests.
 */
@Tag("@UiCore")
public class ElementResolverTests {

    /**
     * Find by data id.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface FindByDataId {
        /**
         * Value string.
         *
         * @return the string
         */
        String value();
    }

    /**
     * Test custom page.
     */
    public class TestCustomPage extends InjectedPage {
        /**
         * The Elem.
         */
        @Element("Custom")
        @FindByDataId("./xxxx")
        public TextArea elem;
    }

    /**
     * Text area.
     */
    public static class TextArea extends BaseInput {
        /**
         * Instantiates a new Text area.
         *
         * @param inputElement the input element
         */
        public TextArea(SelenideElement inputElement) {
            super(inputElement);
        }

        /**
         * Instantiates a new Text area.
         *
         * @param inputElement the input element
         * @param title        the title
         */
        public TextArea(SelenideElement inputElement, String title) {
            super(inputElement, title);
        }

        @Override
        protected boolean isReadOnly() {
            return false;
        }
    }

    /**
     * Test.
     */
    @Test
    public void test() {
        TestCustomPage page;
        try (MockedStatic<WebDriverRunner> runner = Mockito.mockStatic(WebDriverRunner.class);
                MockedStatic<Selenide> selenide = Mockito.mockStatic(Selenide.class)) {
            runner.when(WebDriverRunner::hasWebDriverStarted).thenReturn(true);
            selenide.when(() -> Selenide.$x(anyString())).thenReturn(Mockito.mock(SelenideElement.class));
            page = new TestCustomPage();
        }
        Assertions.assertThrows(ElementInteractionException.class, () -> page.elem.click());
    }
}
