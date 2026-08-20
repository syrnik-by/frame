package ru.autotestframework.ui_core.typified_elements.ifaces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import ru.autotestframework.ui_core.UiCoreUtils;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements that provide the ability to verify a value.
 */
public interface IMultipleValueVerifiable extends IVerifiable, IMultipleValueTypeable {

    /**
     * @param expected expected value to check
     * @param fullCheck true if you want to perform a full reconciliation of all values, false - check only for occurrence
     * @return verification result
     */
    @Override
    default Verifier verify(final String expected, final boolean fullCheck) {
        Verifier verifier;
        if (isFixStateValue()) {
            List<FixState> valueList = Arrays.stream(expected.split(getStringArrayDelimiter()))
                    .map(String::trim)
                    .map(FixState::determine)
                    .collect(Collectors.toList());
            verifier = verifyMultipleFixStates(valueList, fullCheck);
        } else {
            List<String> valueList = UiCoreUtils.parseValueList(expected, getStringArrayDelimiter());
            verifier = verifyMultiple(valueList, fullCheck);
        }
        return verifier;
    }

    /**
     * Verify multiple verifier.
     *
     * @param expected list of expected values to check
     * @param fullCheck true if you want to perform a full reconciliation of all values, false - check only for occurrence
     * @return verification result
     */
    default Verifier verifyMultiple(final Collection<String> expected, final boolean fullCheck) {
        throw new ElementInteractionException(
                "'verifyMultiple' function not implemented for type '{}'",
                getClass().getSimpleName());
    }

    /**
     * Verify multiple fix states verifier.
     *
     * @param expected  the expected
     * @param fullCheck the full check
     * @return the verifier
     */
    default Verifier verifyMultipleFixStates(final Collection<FixState> expected, final boolean fullCheck) {
        throw new ElementInteractionException(
                "'verifyMultipleFixStates' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
