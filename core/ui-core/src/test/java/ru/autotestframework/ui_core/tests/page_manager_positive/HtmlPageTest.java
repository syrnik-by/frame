package ru.autotestframework.ui_core.tests.page_manager_positive;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.page_manager.AbstractPage;

/**
 * Html page test.
 */
@Tag("@PageManagerTest")
class HtmlPageTest {

    /**
     * Throwing get title.
     */
    @Test
    void throwingGetTitle() {
        final TestPageWithoutAnnotation page = new TestPageWithoutAnnotation();
        assertThrows(InitializationException.class, page::getTitle);
    }

    /**
     * Test page without annotation.
     */
    // страница без аннотации PageEntry
    public static class TestPageWithoutAnnotation extends AbstractPage {}
}
