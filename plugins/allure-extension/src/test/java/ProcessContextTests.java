import static ru.autotestframework.core.context.ContextFunctionFactory.SECRET_DUMMY;

import io.cucumber.plugin.event.DataTableArgument;
import java.util.List;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.configuration.PlaceholderResolverConfig;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.FileLoader;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.extension.AllureAspects;
import ru.autotestframework.test_scope_info.StepInfoContainer;

class ProcessContextTests {
    private final String p1 = "p1";
    private final String v1 = "v1";

    private final String p2 = "p2";
    private final String v2 = "v2";
    private final String v2s = "${{secret:${{p2}}}}";

    private final String p3 = "p3";
    private final String v3 = "v3";
    private final String v3s = "${{p3}}";

    private final DataTableArgument dataTableArgument = new DataTableArgument() {
        @Override
        public List<List<String>> cells() {
            return List.of(List.of(p1, v1), List.of(p2, v2s), List.of(p3, v3s));
        }

        @Override
        public int getLine() {
            return 0;
        }
    };
    private FrameworkProperties frameworkProperties = new FrameworkProperties();
    private final FileLoader fileLoader =
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), frameworkProperties);
    private final ContextFunctionsSupplier funcSupplier =
            new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
    private Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
    private final StringSubstitutor stringSubstitutor =
            new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
    private PlaceholderResolverImpl placeholderResolver = new PlaceholderResolverImpl(stringSubstitutor);
    private AllureAspects allureAspects =
            new AllureAspects(frameworkProperties, new StepInfoContainer(), placeholderResolver);

    {
        context.set(p1, v1);
        context.set(p2, v2);
        context.set(p3, v3);
    }

    @Test
    void processAllureMaskedDataTableTest() {
        frameworkProperties.setUnmaskingVariablesEnabled(true);
        String result = allureAspects.resolvedDataTableAttachment(dataTableArgument);
        String expected = p1.concat("\t")
                .concat(v1)
                .concat("\n")
                .concat(p2)
                .concat("\t")
                .concat(SECRET_DUMMY)
                .concat("\n")
                .concat(p3)
                .concat("\t")
                .concat(v3)
                .concat("\n");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void processAllureUnMaskedDataTableTest() {
        String result = allureAspects.resolvedDataTableAttachment(dataTableArgument);
        String expected = p1.concat("\t")
                .concat(v1)
                .concat("\n")
                .concat(p2)
                .concat("\t")
                .concat(v2)
                .concat("\n")
                .concat(p3)
                .concat("\t")
                .concat(v3)
                .concat("\n");
        Assertions.assertEquals(expected, result);
    }
}
