package ru.autotestframework.ui_core.tests;

import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMutlipleValueReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;

/**
 * Verifier test.
 */
@Tag("@UiCore")
class VerifierTest {

    /**
     * Of verify function positive test.
     */
    @Test
    void ofVerifyFunctionPositiveTest() {
        Consumer<Verifier> consumer = verifier -> {
            verifier.setSingleActual("singleActual");
        };
        Verifier verifier = Verifier.of(consumer);
        Assertions.assertEquals("singleActual", verifier.getSingleActual());
    }

    /**
     * Of verify function negative test.
     */
    @Test
    void ofVerifyFunctionNegativeTest() {
        Consumer<Verifier> consumer = verifier -> {
            verifier.setSingleActual("singleActual");
        };
        Verifier verifier = Verifier.of(consumer);
        Assertions.assertNotEquals("singleWrong", verifier.getSingleActual());
    }

    /**
     * Of string positive test.
     */
    @Test
    void ofStringPositiveTest() {
        IReadable iReadable = Mockito.mock(IReadable.class);
        String expected = "expectedString";
        Mockito.when(iReadable.readValue()).thenReturn(expected);
        Verifier verifier = Verifier.of(iReadable, expected);
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of string negative test.
     */
    @Test
    void ofStringNegativeTest() {
        IReadable iReadable = Mockito.mock(IReadable.class);
        String expected = "expectedString";
        String returned = "wrongString";
        Mockito.when(iReadable.readValue()).thenReturn(returned);
        Verifier verifier = Verifier.of(iReadable, expected);
        Assertions.assertFalse(verifier.isCorrect());
    }

    /**
     * Of fix state positive test.
     */
    @Test
    void ofFixStatePositiveTest() {
        IReadable iReadable = Mockito.mock(IReadable.class);
        FixState fixState = FixState.ON;
        Mockito.when(iReadable.readState()).thenReturn(fixState);
        Verifier verifier = Verifier.of(iReadable, fixState);
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of fix state negative test.
     */
    @Test
    void ofFixStateNegativeTest() {
        IReadable iReadable = Mockito.mock(IReadable.class);
        FixState fixState = FixState.ON;
        FixState returned = FixState.OFF;
        Mockito.when(iReadable.readState()).thenReturn(returned);
        Verifier verifier = Verifier.of(iReadable, fixState);
        Assertions.assertFalse(verifier.isCorrect());
    }

    /**
     * Of collection full check positive test.
     */
    @Test
    void ofCollectionFullCheckPositiveTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<String> list = List.of("1", "2", "3");
        Mockito.when(iMutlipleValueReadable.readMultipleValues()).thenReturn(list);
        Verifier verifier = Verifier.of(iMutlipleValueReadable, true, list);
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of collection full check negative test.
     */
    @Test
    void ofCollectionFullCheckNegativeTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<String> list = List.of("1", "2", "3");
        List<String> returned = List.of("3", "2", "1");
        Mockito.when(iMutlipleValueReadable.readMultipleValues()).thenReturn(returned);
        Verifier verifier = Verifier.of(iMutlipleValueReadable, true, list);
        Assertions.assertFalse(verifier.isCorrect());
    }

    /**
     * Of collection not full check positive test.
     */
    @Test
    void ofCollectionNotFullCheckPositiveTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<String> list = List.of("1", "2", "3");
        Mockito.when(iMutlipleValueReadable.readMultipleValues()).thenReturn(list);
        Verifier verifier = Verifier.of(iMutlipleValueReadable, false, List.of("1", "3"));
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of collection not full check negative test.
     */
    @Test
    void ofCollectionNotFullCheckNegativeTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<String> list = List.of("1", "2");
        List<String> returned = List.of("1", "3", "4");
        Mockito.when(iMutlipleValueReadable.readMultipleValues()).thenReturn(returned);
        Verifier verifier = Verifier.of(iMutlipleValueReadable, false, list);
        Assertions.assertFalse(verifier.isCorrect());
    }

    /**
     * Of state list full check positive test.
     */
    @Test
    void ofStateListFullCheckPositiveTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<FixState> fixStates = List.of(FixState.ON, FixState.INDETERMINATE, FixState.OFF);
        Mockito.when(iMutlipleValueReadable.readMultipleStates()).thenReturn(fixStates);
        Verifier verifier = Verifier.ofStateList(iMutlipleValueReadable, true, fixStates);
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of state list full check negative test.
     */
    @Test
    void ofStateListFullCheckNegativeTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<FixState> fixStates = List.of(FixState.ON, FixState.INDETERMINATE, FixState.OFF);
        List<FixState> returned = List.of(FixState.OFF, FixState.INDETERMINATE, FixState.ON);
        Mockito.when(iMutlipleValueReadable.readMultipleStates()).thenReturn(returned);
        Verifier verifier = Verifier.ofStateList(iMutlipleValueReadable, true, fixStates);
        Assertions.assertFalse(verifier.isCorrect());
    }

    /**
     * Of state list not full check positive test.
     */
    @Test
    void ofStateListNotFullCheckPositiveTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<FixState> fixStates = List.of(FixState.ON, FixState.INDETERMINATE, FixState.OFF);
        Mockito.when(iMutlipleValueReadable.readMultipleStates()).thenReturn(fixStates);
        Verifier verifier = Verifier.ofStateList(iMutlipleValueReadable, false, List.of(FixState.ON, FixState.OFF));
        Assertions.assertTrue(verifier.isCorrect());
    }

    /**
     * Of state list not full check negative test.
     */
    @Test
    void ofStateListNotFullCheckNegativeTest() {
        IMutlipleValueReadable iMutlipleValueReadable = Mockito.mock(IMutlipleValueReadable.class);
        List<FixState> fixStates = List.of(FixState.ON, FixState.OFF);
        List<FixState> returned = List.of(FixState.INDETERMINATE, FixState.INDETERMINATE, FixState.OFF);
        Mockito.when(iMutlipleValueReadable.readMultipleStates()).thenReturn(returned);
        Verifier verifier = Verifier.ofStateList(iMutlipleValueReadable, false, fixStates);
        Assertions.assertFalse(verifier.isCorrect());
    }
}
