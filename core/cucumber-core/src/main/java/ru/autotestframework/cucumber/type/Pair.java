package ru.autotestframework.cucumber.type;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pair.
 */
@Data
@AllArgsConstructor(staticName = "of")
public class Pair {

    private final String first;
    private final String second;
}
