package ru.autotestframework.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.junit.BaseSpringJunitTest;

/**
 * Junit.
 */
@Tag("@BackendCore")
class Junit extends BaseSpringJunitTest {

    /**
     * The File loader.
     */
    @Autowired
    FileLoaderImpl fileLoader;

    /**
     * The Context.
     */
    @Autowired
    Context context;

    /**
     * Example test.
     */
    @Test
    void exampleTest() {
        String expected = "someValue";

        context.set("brb", expected);
        String actual = fileLoader.readFileAsString("data/file.txt");

        Assertions.assertEquals(expected, actual);
    }
}
