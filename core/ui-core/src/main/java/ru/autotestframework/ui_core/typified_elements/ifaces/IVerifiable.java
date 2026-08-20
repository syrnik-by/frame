package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements that provide the ability to verify a value.
 */
public interface IVerifiable extends IValueTypeable {

    /**
     * Verify verifier.
     *
     * @param expected expected value to check
     * @return verification result
     */
    default Verifier verify(String expected) {
        throw new ElementInteractionException(
                "'verify' function not implemented for type '{}'", getClass().getSimpleName());
    }

    /**
     * Verify verifier.
     *
     * @param expected expected value to check
     * @param fullCheck is a stub for implementing checks on elements with multiple values
     * @return verification result
     */
    default Verifier verify(String expected, boolean fullCheck) {
        Verifier verifier;
        if (isFixStateValue()) {
            verifier = verifyFixState(FixState.determine(expected));
        } else {
            verifier = verify(expected);
        }
        return verifier;
    }

    /**
     * Verify fix state verifier.
     *
     * @param expected the expected
     * @return the verifier
     */
    default Verifier verifyFixState(FixState expected) {
        throw new ElementInteractionException(
                "'verifyFixState' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
