package ru.autotestframework.ui_core.tests.interfaces_tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.exceptions.InitializationException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueSelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueTypeable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueWritable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMutlipleValueReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Ifaces tests.
 */
@Tag("@PageManagerTest")
class IfacesTests {

    private static final String DELIMETER = ":";
    private static final String TEST_VALUES = "one".concat(DELIMETER).concat("two");
    private static final List<String> INIT_VALUES = List.of("two", "three");

    /**
     * Read read multiple.
     */
    @Test
    void readReadMultiple() {
        TestMultipleValueElement testElement = new TestMultipleValueElement();
        String result = ((IReadable) testElement).readValue();
        Assertions.assertEquals("two".concat(DELIMETER).concat("three"), result);
    }

    /**
     * Test write multiple.
     */
    @Test
    void testWriteMultiple() {
        TestMultipleValueElement testElement = new TestMultipleValueElement();
        ((IWritable) testElement).write(TEST_VALUES);
        String result = ((IReadable) testElement).readValue();
        Assertions.assertEquals(TEST_VALUES, result);
    }

    /**
     * Test append multiple.
     */
    @Test
    void testAppendMultiple() {
        TestMultipleValueElement testElement = new TestMultipleValueElement();
        ((IWritable) testElement).append(TEST_VALUES);
        String result = ((IReadable) testElement).readValue();
        Assertions.assertEquals("twoone".concat(DELIMETER).concat("threetwo"), result);
    }

    /**
     * Test multiple value select.
     */
    @Test
    void testMultipleValueSelect() {
        TestMultipleValueElement testElement = new TestMultipleValueElement();
        Assertions.assertThrows(InitializationException.class, () -> ((ISelectable) testElement).select("true"));
    }

    /**
     * Test multiple value verify.
     */
    @Test
    void testMultipleValueVerify() {
        TestMultipleValueElement testElement = new TestMultipleValueElement();
        boolean result = ((IVerifiable) testElement)
                .verify(String.join(DELIMETER, INIT_VALUES), true)
                .isCorrect();
        Assertions.assertTrue(result);
    }

    /**
     * Test fix state verify.
     */
    @Test
    void testFixStateVerify() {
        TestFixStateElement testElement = new TestFixStateElement();
        Assertions.assertThrows(ConfigurationException.class, () -> ((IVerifiable) testElement).verify("true", true));
    }

    /**
     * Test multiple value element.
     */
    public static class TestMultipleValueElement
            implements IMultipleValueTypeable,
                    IMultipleValueSelectable,
                    IMultipleValueWritable,
                    IMultipleValueVerifiable,
                    IMutlipleValueReadable {

        private List<String> innerListValues = INIT_VALUES;

        @Override
        public boolean isFixStateValue() {
            return false;
        }

        @Override
        public Collection<String> readMultipleValues() {
            return innerListValues;
        }

        @Override
        public void appendMultiple(final Collection<String> values) {
            Iterator iterPreviousValues = innerListValues.iterator();
            Iterator iterAppendedValues = values.iterator();
            List<String> newValues = new ArrayList();
            while (iterPreviousValues.hasNext() && iterAppendedValues.hasNext()) {
                newValues.add(iterPreviousValues
                        .next()
                        .toString()
                        .concat(iterAppendedValues.next().toString()));
            }
            innerListValues = newValues;
        }

        @Override
        public void writeMultiple(final Collection<String> values) {
            innerListValues = (List) values;
        }

        @Override
        public String getStringArrayDelimiter() {
            return DELIMETER;
        }

        @Override
        public void selectMultiple(final Collection<?> values) {
            if (values != null) {
                throw new InitializationException("test");
            }
        }

        @Override
        public Verifier verifyMultiple(final Collection<String> expected, final boolean fullCheck) {
            return Verifier.of(this, fullCheck, expected);
        }

        @Override
        public boolean isEditable() {
            return false;
        }
    }

    /**
     * Test fix state element.
     */
    public static class TestFixStateElement
            implements IMultipleValueTypeable, IMultipleValueSelectable, IMultipleValueVerifiable {

        @Override
        public boolean isFixStateValue() {
            return true;
        }

        @Override
        public String getStringArrayDelimiter() {
            return DELIMETER;
        }

        @Override
        public Verifier verifyMultipleFixStates(final Collection<FixState> expected, final boolean fullCheck) {
            throw new ConfigurationException("textException");
        }
    }
}
