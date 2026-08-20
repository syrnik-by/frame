package ru.autotestframework.junit.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.autotestframework.junit.BaseSpringJunitTest;

@Tag("@prll-spring")
@Tag("@junit")
@Execution(ExecutionMode.CONCURRENT)
class SimpleSpringParallelTests extends BaseSpringJunitTest {

    @Test
    void itsTrueReallyTrue() {
        step1();
        Assertions.assertTrue(true);
    }

    @Test
    void itsTrueReallyTrue2() {
        step1();
        Assertions.assertTrue(true);
    }

    @Test
    void itsTrueReallyTrue3() {
        step1();
        Assertions.assertTrue(true);
    }

    private void step1() {
        step2();
        Assertions.assertTrue(true);
    }

    private void step2() {
        Assertions.assertTrue(true);
    }
}
