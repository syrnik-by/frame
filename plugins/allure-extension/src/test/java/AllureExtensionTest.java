import static ru.autotestframework.core.context.ContextFunctionFactory.SECRET_DUMMY;

import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.Step;
import io.qameta.allure.AllureLifecycle;
import java.util.Collections;
import java.util.List;
import net.datafaker.Faker;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
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

@Tag("@Allure")
class AllureExtensionTest {
    private final DataTableArgument emptyTableArgument = new DataTableArgument() {
        @Override
        public List<List<String>> cells() {
            return Collections.emptyList();
        }

        @Override
        public int getLine() {
            return 0;
        }
    };
    private String p1;
    private String v1;
    private String p2;
    private String v2;
    private String v2s;
    private String p3;
    private String v3;
    private String v3s;
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
    private final FrameworkProperties frameworkProperties = new FrameworkProperties();
    private final FileLoader fileLoader =
            new FileLoaderImpl(new PlaceholderResolverImpl(new StringSubstitutor()), frameworkProperties);
    private final ContextFunctionsSupplier funcSupplier =
            new PlaceholderResolverConfig().defaultDataGeneratorsSupplier(fileLoader);
    private final Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
    private final StringSubstitutor stringSubstitutor =
            new PlaceholderResolverConfig().placeholderResolver(context, List.of(funcSupplier));
    private final PlaceholderResolverImpl placeholderResolver = new PlaceholderResolverImpl(stringSubstitutor);
    private final AllureAspects allureAspects =
            new AllureAspects(frameworkProperties, new StepInfoContainer(), placeholderResolver);

    @BeforeEach
    public void setup() {
        p1 = Faker.instance().app().name();
        v1 = Faker.instance().app().name();
        p2 = Faker.instance().app().name();
        v2 = Faker.instance().app().name();
        v3 = Faker.instance().app().name();
        p3 = Faker.instance().app().name();
        v2s = "${{secret:${{".concat(p2).concat("}}}}");
        v3s = "${{".concat(p3).concat("}}");
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
    void processEmptyTableTest() {
        frameworkProperties.setUnmaskingVariablesEnabled(true);
        String result = allureAspects.resolvedDataTableAttachment(emptyTableArgument);
        Assertions.assertEquals(0, result.length());
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

    @Test
    void dontUnmaskTest() {
        var container = new StepInfoContainer();
        StepInfoContainer stepInfoContainer = Mockito.spy(container);
        frameworkProperties.setUnmaskingVariablesEnabled(false);
        ReflectionTestUtils.setField(allureAspects, "stepInfoContainer", stepInfoContainer);

        allureAspects.addStepInfo();

        Mockito.verifyNoInteractions(stepInfoContainer);
    }

    @Test
    void updateStepTest() {
        var container = new StepInfoContainer();
        Step step = Mockito.mock(Step.class);
        StepInfoContainer stepInfoContainer = Mockito.spy(container);
        frameworkProperties.setUnmaskingVariablesEnabled(true);
        ReflectionTestUtils.setField(allureAspects, "stepInfoContainer", stepInfoContainer);
        Mockito.when(stepInfoContainer.getCurrentStep()).thenReturn(step);
        ReflectionTestUtils.setField(allureAspects, "lifecycle", new AllureLifecycle());

        allureAspects.addStepInfo();

        Mockito.verify(stepInfoContainer, Mockito.atLeastOnce()).getCurrentStep();
    }

    @Test
    void updateStepWithDataTableTest() {
        var container = new StepInfoContainer();
        Step step = Mockito.mock(Step.class);
        StepInfoContainer stepInfoContainer = Mockito.spy(container);
        Step spyedStep = Mockito.spy(step);
        frameworkProperties.setUnmaskingVariablesEnabled(true);
        ReflectionTestUtils.setField(allureAspects, "stepInfoContainer", stepInfoContainer);

        Mockito.when(spyedStep.getArgument()).thenReturn(dataTableArgument);

        Mockito.when(stepInfoContainer.getCurrentStep()).thenReturn(spyedStep);
        ReflectionTestUtils.setField(allureAspects, "lifecycle", new AllureLifecycle());

        allureAspects.addStepInfo();

        Mockito.verify(stepInfoContainer, Mockito.atLeastOnce()).getCurrentStep();
    }
}
