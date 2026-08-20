package ru.autotestframework.core;

import java.util.Map;

/**
 * Represents an object that allows you to replace placeholders of the form '{@code ${{variable}}}' in string variables.
 */
public interface PlaceholderResolver {

    /**
     * Replaces placeholders like '{@code ${{variable}}}' with actual variable values.
     *
     * @param input source string
     * @return string with replaced placeholders
     */
    String resolve(String input);

    /**
     * Replaces placeholders like '{@code ${{variable}}}' with actual variable values.
     * <br>
     * Replaces both in keys and values
     *
     * @param inputMap source map
     * @return map with replaced placeholders
     */
    Map<String, String> resolve(Map<String, String> inputMap);
}
