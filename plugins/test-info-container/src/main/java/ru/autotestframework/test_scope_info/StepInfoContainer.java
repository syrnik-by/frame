package ru.autotestframework.test_scope_info;

import io.cucumber.plugin.event.Step;
import io.cucumber.spring.ScenarioScope;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
@Data
public class StepInfoContainer {
    private Step currentStep;
    private List<String> annotations;
    private List<String> scenarioTags;
    private String scenarioName;
    private Object[] stepArgs;
}
