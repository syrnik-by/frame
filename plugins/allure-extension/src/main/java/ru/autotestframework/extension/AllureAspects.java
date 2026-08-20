package ru.autotestframework.extension;

import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.StepArgument;
import io.cucumber.spring.ScenarioScope;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.StepResult;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.test_scope_info.StepInfoContainer;

@Slf4j
@Order(1)
@Aspect
@ScenarioScope
@Component
@RequiredArgsConstructor
public class AllureAspects {

    private final FrameworkProperties properties;
    private final StepInfoContainer stepInfoContainer;
    private final PlaceholderResolverImpl placeholderResolver;
    private final AllureLifecycle lifecycle = Allure.getLifecycle();

    /**
     * unmasks info in steps
     */
    @SneakyThrows
    @After("execution(@(@io.cucumber.java.StepDefinitionAnnotation *) * *(..))")
    public void addStepInfo() {
        if (properties.isUnmaskingVariablesEnabled()) {
            unmasking();
        }
    }

    private void unmasking() {
        getLifecycle()
                .updateStep(x ->
                        x.setName(placeholderResolver.resolve(x.getName(), properties.isUnmaskingVariablesEnabled())));

        StepArgument argument = stepInfoContainer.getCurrentStep().getArgument();
        if (argument instanceof DataTableArgument) {
            final var dataTableArgument = (DataTableArgument) argument;
            unmaskDataTableAttachment(dataTableArgument);
        }
    }

    private void updateStep(StepResult stepResult) {
        var dataTableAttach = stepResult.getAttachments().stream()
                .filter(att -> att.getName().equals("Data table"))
                .findFirst();
        if (dataTableAttach.isPresent()) {
            stepResult.getAttachments().remove(0);
        }
    }

    private void unmaskDataTableAttachment(final DataTableArgument argument) {
        getLifecycle().updateStep(this::updateStep);
        var dataTableString = resolvedDataTableAttachment(argument);

        final String attachmentSource =
                getLifecycle().prepareAttachment("Data table", "text/tab-separated-values", "csv");
        getLifecycle()
                .writeAttachment(
                        attachmentSource, new ByteArrayInputStream(dataTableString.getBytes(StandardCharsets.UTF_8)));
    }

    private AllureLifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * unmasks info in dataTables
     * @param dataTableArgument
     * @return
     */
    public String resolvedDataTableAttachment(final DataTableArgument dataTableArgument) {
        final List<List<String>> rowsInTable = dataTableArgument.cells();
        final var dataTableCsv = new StringBuilder();
        for (List<String> columns : rowsInTable) {
            if (!columns.isEmpty()) {
                for (var i = 0; i < columns.size(); i++) {
                    String resolvedCell =
                            placeholderResolver.resolve(columns.get(i), properties.isUnmaskingVariablesEnabled());
                    if (i == columns.size() - 1) {
                        dataTableCsv.append(resolvedCell);
                    } else {
                        dataTableCsv.append(resolvedCell);
                        dataTableCsv.append('\t');
                    }
                }
                dataTableCsv.append('\n');
            }
        }
        return dataTableCsv.toString();
    }
}
