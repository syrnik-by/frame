package ru.autotestframework.ui_core.typified_elements;

import com.codeborne.selenide.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import ru.autotestframework.Constants;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMutlipleValueReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;

/**
 * TODO убрать данный класс и перевести на HamCrest Matcher c ожиданиями
 * The class is designed to store the results of checking the value (values, states) of a web element and the current value
 * (values, states)
 * All standard methods for comparing expected and actual values have already been implemented, but you can pass your own
 * custom method.
 */
@Getter
@Setter
public class Verifier {
    private boolean correct = false;
    private String singleActual;
    private List<String> listActual;
    private FixState stateActual;
    private List<FixState> stateListActual;

    private Verifier() {}

    /**
     * Of verifier.
     *
     * @param verifyFunction the verify function
     * @return the verifier
     */
    public static Verifier of(final Consumer<Verifier> verifyFunction) {
        final var verifier = new Verifier();
        verifyFunction.accept(verifier);
        return verifier;
    }

    /**
     * Of verifier.
     *
     * @param element  the element
     * @param expected the expected
     * @return the verifier
     */
    public static Verifier of(final IReadable element, final String expected) {
        final var verifier = new Verifier();
        long startLoadingTime = System.currentTimeMillis();
        while (!verifier.correct && (System.currentTimeMillis() - startLoadingTime < Configuration.timeout)) {
            verifier.singleActual = element.readValue();
            verifier.correct = expected.equals(verifier.singleActual);
        }
        return verifier;
    }

    /**
     * Of verifier.
     *
     * @param element  the element
     * @param expected the expected
     * @return the verifier
     */
    public static Verifier of(final IReadable element, final FixState expected) {
        final var verifier = new Verifier();
        verifier.stateActual = element.readState();
        verifier.correct = expected == verifier.getStateActual();
        return verifier;
    }

    /**
     * Of verifier.
     *
     * @param element        the element
     * @param fullCheck      the full check
     * @param expectedValues the expected values
     * @return the verifier
     */
    public static Verifier of(
            final IMutlipleValueReadable element, final boolean fullCheck, final Collection<String> expectedValues) {
        final var verifier = new Verifier();
        verifier.listActual = new ArrayList<>(element.readMultipleValues());
        if (fullCheck) {
            verifier.correct = verifier.listActual.equals(expectedValues);
        } else {
            verifier.correct = verifier.listActual.containsAll(expectedValues);
        }
        return verifier;
    }

    /**
     * Of state list verifier.
     *
     * @param element        the element
     * @param fullCheck      the full check
     * @param expectedValues the expected values
     * @return the verifier
     */
    public static Verifier ofStateList(
            final IMutlipleValueReadable element, final boolean fullCheck, final Collection<FixState> expectedValues) {
        final var verifier = new Verifier();
        verifier.stateListActual = new ArrayList<>(element.readMultipleStates());
        if (fullCheck) {
            verifier.correct = verifier.stateListActual.equals(expectedValues);
        } else {
            verifier.correct = verifier.stateListActual.containsAll(expectedValues);
        }
        return verifier;
    }

    @Override
    public String toString() {
        if (Objects.nonNull(singleActual)) {
            return singleActual;
        }
        if (Objects.nonNull(listActual)) {
            return String.join(Constants.ARRAY_STRING_DELIMETER, listActual);
        }
        if (Objects.nonNull(stateActual)) {
            return stateActual.name();
        }
        if (Objects.nonNull(stateListActual)) {
            return stateListActual.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(Constants.ARRAY_STRING_DELIMETER));
        }
        return "";
    }
}
