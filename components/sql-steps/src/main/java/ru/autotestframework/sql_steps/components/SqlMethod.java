package ru.autotestframework.sql_steps.components;

/**
 * Methods executable sql
 */
public enum SqlMethod {
    EXECUTE,
    SELECT;

    public static final String REGEX = "(EXECUTE|SELECT)";
}
