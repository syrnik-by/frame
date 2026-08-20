package ru.autotestframework.ui_core.typified_elements.enums;

import java.util.Arrays;
import java.util.stream.Stream;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * an enum for storing the value of a multiphase element (for example, a checkbox or a checkbox with partial inclusion).
 */
public enum FixState {
    /**
     * On fix state.
     */
    // Первые варианты On, Off, Indeterminate должны оставаться первыми, так как обеспечивают совместимость
    // с desktop элементами
    ON("On", "true", "да", "включен"),
    /**
     * Off fix state.
     */
    OFF("Off", "false", "нет", "выключен"),
    /**
     * Indeterminate fix state.
     */
    INDETERMINATE("Indeterminate", "partial", "частично", "частично включен");

    private final String[] variants;

    FixState(final String... variants) {
        this.variants = variants;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return variants[0];
    }

    /**
     * Return FixState Enum with given name.
     *
     * @param name the name
     * @return FixState fix state
     */
    public static FixState determine(final String name) {
        return Stream.of(values())
                .filter(v -> {
                    String[] variants = v.variants;
                    Arrays.sort(variants);
                    return Arrays.binarySearch(variants, name) > -1;
                })
                .findFirst()
                .orElseThrow(() -> new ElementInteractionException("Undefined name '{}' for FixState enum", name));
    }
}
