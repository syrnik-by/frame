package ru.autotestframework.core.context;

import java.util.HashMap;
import org.apache.commons.text.lookup.StringLookup;

/**
 * Represents an object that supplies context functions.
 */
public interface ContextFunctionsSupplier extends java.util.function.Supplier<HashMap<String, StringLookup>> {}
