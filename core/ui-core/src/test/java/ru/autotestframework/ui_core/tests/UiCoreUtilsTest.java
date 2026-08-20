package ru.autotestframework.ui_core.tests;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.UiCoreUtils;

/**
 * Ui core utils test.
 */
@Tag("@UiCore")
class UiCoreUtilsTest {

    /**
     * Parse value list test.
     */
    @Test
    void parseValueListTest() {
        String value = "abc : def: ghi ";
        String delimiter = ":";
        List<String> parsedValue = UiCoreUtils.parseValueList(value, delimiter);
        Assertions.assertEquals(List.of("abc", "def", "ghi"), parsedValue);
    }
}
