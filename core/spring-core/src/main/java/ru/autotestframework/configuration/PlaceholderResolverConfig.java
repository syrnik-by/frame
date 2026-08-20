package ru.autotestframework.configuration;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextFunctionFactory;
import ru.autotestframework.core.context.ContextFunctionsSupplier;

/**
 * Placeholder resolver config.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlaceholderResolverConfig {

    /**
     * The constant NULL_PLACEHOLDER.
     */
    public static final String NULL_PLACEHOLDER = "${{null}}";
    /**
     * The constant EMPTY_STRING_PLACEHOLDER.
     */
    public static final String EMPTY_STRING_PLACEHOLDER = "${{empty}}";
    /**
     * The constant EMPTY_STRING_PLACEHOLDER_PROPERTY.
     */
    public static final String EMPTY_STRING_PLACEHOLDER_PROPERTY = "framework.variables.empty=";
    /**
     * The constant PREFIX.
     */
    // TODO возможность настроить
    public static final String PREFIX = "${{";
    /**
     * The constant SUFFIX.
     */
    public static final String SUFFIX = "}}";

    /**
     * Default data generators supplier context functions supplier.
     *
     * @param fileLoader the file loader
     * @return the context functions supplier
     */
    @Bean
    public ContextFunctionsSupplier defaultDataGeneratorsSupplier(final @Lazy FileLoader fileLoader) {
        return () -> {
            var contextFunctions = new HashMap<String, StringLookup>();
            contextFunctions.put("fakerRu", ContextFunctionFactory.FAKER_RU);
            contextFunctions.put("regex", ContextFunctionFactory.REGEX_GENERATOR);
            contextFunctions.put("date", StringLookupFactory.INSTANCE.dateStringLookup());
            contextFunctions.put("randomLong", ContextFunctionFactory.RANDOM_LONG_GENERATOR);
            contextFunctions.put("env", StringLookupFactory.INSTANCE.environmentVariableStringLookup());
            contextFunctions.put("file", fileLoader::readFileAsString);
            contextFunctions.put("base64Encoder", ContextFunctionFactory.BASE_64_ENCODER);
            contextFunctions.put("base64Decoder", ContextFunctionFactory.BASE_64_DECODER);
            contextFunctions.put("secret", ContextFunctionFactory.SECRET);
            contextFunctions.put("dummy", ContextFunctionFactory.SECRET_STUB);
            contextFunctions.put("escape", ContextFunctionFactory.ESCAPER);
            contextFunctions.put("calc", new ContextFunctionFactory.MakeArithmetic());
            contextFunctions.put("fileReadEnc", fileLoader::readFileAsString);

            return contextFunctions;
        };
    }

    /**
     * Placeholder resolver string substitutor.
     *
     * @param context                   the context
     * @param contextFunctionsSuppliers the context functions suppliers
     * @return the string substitutor
     */
    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public StringSubstitutor placeholderResolver(
            final Context context, final List<ContextFunctionsSupplier> contextFunctionsSuppliers) {
        var dataGenerators = new HashMap<String, StringLookup>();
        contextFunctionsSuppliers.forEach(supplier -> dataGenerators.putAll(supplier.get()));

        var contextLookup = StringLookupFactory.INSTANCE.functionStringLookup(context::get);
        var finalLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(dataGenerators, contextLookup, false);

        return new StringSubstitutor(finalLookup)
                .setEnableUndefinedVariableException(true)
                .setEnableSubstitutionInVariables(true)
                .setVariablePrefix(PREFIX)
                .setVariableSuffix(SUFFIX);
    }
}
