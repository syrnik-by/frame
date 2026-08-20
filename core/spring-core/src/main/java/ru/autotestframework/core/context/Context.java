package ru.autotestframework.core.context;

import java.util.Map;

/**
 * The interface represents an object containing context variables for a specific test.
 */
public interface Context extends Cleanable {

    /**
     * Getting a context variable.
     *
     * @param <T>     the type parameter
     * @param varName - name of the context variable
     * @return value of the context variable
     */
    <T extends Object> T getObj(String varName);

    /**
     * Get string.
     *
     * @param varName the var name
     * @return the string
     */
    String get(String varName);

    /**
     * Gets all.
     *
     * @return everything is a context variable.
     */
    Map<String, Object> getAll();

    /**
     * Adding a variable to the context.
     *
     * @param varName is the key, it is also the name of the variable
     * @param value - the value of the variable that will be converted to a string
     */
    void set(String varName, Object value);

    /**
     * Adding all variables to the context.
     *
     * @param variables - variables that need to be added to the context. The map values will be converted to a string.
     */
    void set(Map<String, ?> variables);

    /**
     * Set.
     *
     * @param firstParameterName      the first parameter name
     * @param firstParameterValue     the first parameter value
     * @param parameterNameValuePairs the parameter name value pairs
     */
    void set(String firstParameterName, Object firstParameterValue, java.lang.Object... parameterNameValuePairs);
}
