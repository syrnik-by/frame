package ru.autotestframework.ui_core.tests.services;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.services.CoreReflections;
import ru.autotestframework.ui_core.tests.page_manager.PageManagerTestPage;

/**
 * Core reflections test.
 */
@Tag("@UiCore")
class CoreReflectionsTest {

    /**
     * The Field.
     */
    static Field field;
    /**
     * The Find by.
     */
    static FindBy findBy;

    /**
     * Sets up.
     */
    @BeforeAll
    static void setUp() {
        field = Mockito.mock(Field.class);
        Mockito.when(field.isAnnotationPresent(FindBy.class)).thenReturn(true);
        findBy = Mockito.mock(FindBy.class);
        Mockito.when(field.getAnnotation(FindBy.class)).thenReturn(findBy);
    }

    /**
     * Gets xpath null test.
     */
    @Test
    void getXpathNullTest() {
        Mockito.when(findBy.xpath()).thenReturn(null);
        Assertions.assertEquals("", CoreReflections.getXpath(field));
    }

    /**
     * Gets xpath not null test.
     */
    @Test
    void getXpathNotNullTest() {
        Mockito.when(findBy.xpath()).thenReturn("notNull");
        Assertions.assertEquals("notNull", CoreReflections.getXpath(field));
    }

    /**
     * Gets id null test.
     */
    @Test
    void getIdNullTest() {
        Mockito.when(findBy.id()).thenReturn(null);
        Assertions.assertEquals("", CoreReflections.getId(field));
    }

    /**
     * Gets id not null test.
     */
    @Test
    void getIdNotNullTest() {
        Mockito.when(findBy.id()).thenReturn("notNull");
        Assertions.assertEquals("notNull", CoreReflections.getId(field));
    }

    /**
     * Gets css null test.
     */
    @Test
    void getCssNullTest() {
        Mockito.when(findBy.css()).thenReturn(null);
        Assertions.assertEquals("", CoreReflections.getCss(field));
    }

    /**
     * Gets css not null test.
     */
    @Test
    void getCssNotNullTest() {
        Mockito.when(findBy.css()).thenReturn("notNull");
        Assertions.assertEquals("notNull", CoreReflections.getCss(field));
    }

    /**
     * Gets class name null test.
     */
    @Test
    void getClassNameNullTest() {
        Mockito.when(findBy.className()).thenReturn(null);
        Assertions.assertEquals("", CoreReflections.getClassName(field));
    }

    /**
     * Gets class name not null test.
     */
    @Test
    void getClassNameNotNullTest() {
        Mockito.when(findBy.className()).thenReturn("notNull");
        Assertions.assertEquals("notNull", CoreReflections.getClassName(field));
    }

    /**
     * Gets field by title positive test.
     */
    @Test
    void getFieldByTitlePositiveTest() {
        Field iTable = CoreReflections.getFieldByTitle(new PageManagerTestPage(), "iTable");
        Assertions.assertEquals("iTable", iTable.getName());
    }

    /**
     * Gets field by title negative test.
     */
    @Test
    void getFieldByTitleNegativeTest() {
        Assertions.assertThrows(
                InitializationException.class,
                () -> CoreReflections.getFieldByTitle(new PageManagerTestPage(), "iNotTable"));
    }

    /**
     * Gets fields test.
     */
    @Test
    void getFieldsTest() {
        List<Field> fields = CoreReflections.getFields(new PageManagerTestPage());
        Assertions.assertEquals(2, fields.size());
    }
}
