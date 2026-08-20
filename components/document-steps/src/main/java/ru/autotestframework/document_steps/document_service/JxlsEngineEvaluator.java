package ru.autotestframework.document_steps.document_service;

import static ru.autotestframework.configuration.PlaceholderResolverConfig.PREFIX;
import static ru.autotestframework.configuration.PlaceholderResolverConfig.SUFFIX;

import java.util.Map;
import org.jxls.expression.EvaluationException;
import org.jxls.expression.JexlExpressionEvaluator;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;

/**
 * Jxls engine integration with frame context through StringSubstitutor
 */
public class JxlsEngineEvaluator extends JexlExpressionEvaluator {
    private final PlaceholderResolver resolver;
    private final Context context;

    public JxlsEngineEvaluator(PlaceholderResolver resolver, Context context) {
        super(false, false);
        this.resolver = resolver;
        this.context = context;
    }

    @Override
    public Object evaluate(String s, Map<String, Object> map) {
        Object result = null;
        try {
            result = super.evaluate(s, map);
        } catch (EvaluationException ignored) {
        }
        if (result == null || result == s) {
            result = context.getObj(s);
            return result == null ? resolve(s) : result;
        } else {
            return result;
        }
    }

    private String resolve(String s) {
        return resolver.resolve(PREFIX + s.replaceAll("\\$\\{", PREFIX).replaceAll("}", SUFFIX) + SUFFIX);
    }

    @Override
    public Object evaluate(Map<String, Object> map) {
        return super.evaluate(map);
    }
}
