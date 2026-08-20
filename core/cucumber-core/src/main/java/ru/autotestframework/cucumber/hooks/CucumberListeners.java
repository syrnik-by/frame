package ru.autotestframework.cucumber.hooks;

import io.cucumber.spring.ScenarioScope;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.context.Cleanable;
import ru.autotestframework.core.spring.FeatureEndEvent;

/**
 * Cucumber listeners.
 */
@Slf4j
@Component
@ScenarioScope
@RequiredArgsConstructor
public class CucumberListeners {

    private final List<Cleanable> cleanableBeans;

    /**
     * Clean context when feature ends.
     */
    @EventListener(classes = {FeatureEndEvent.class})
    public void cleanContextWhenFeatureEnds() {
        log.info("Cleaning Context after processing Tests within the Feature file ...");
        cleanableBeans.clear();
    }
}
