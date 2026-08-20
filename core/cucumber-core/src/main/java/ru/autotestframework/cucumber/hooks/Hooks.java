package ru.autotestframework.cucumber.hooks;

import static io.cucumber.junit.platform.engine.Constants.PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import ru.autotestframework.core.context.Cleanable;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.Savable;
import ru.autotestframework.core.spring.FeatureEndEvent;
import ru.autotestframework.core.spring.FeatureStartEvent;

/**
 * Hooks.
 */
@Slf4j
@RequiredArgsConstructor
public class Hooks {

    private static final ThreadLocal<String> PREV_FEATURE = new InheritableThreadLocal<>();

    private final List<Cleanable> cleanableBeans;
    private final List<Savable> savableBeans;

    private final Context context;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publish feature start event.
     *
     * @param scenario the scenario
     */
    @Before(order = Integer.MIN_VALUE)
    public void publishFeatureStartEvent(final Scenario scenario) {
        var currentFeature = scenario.getUri().toString() + scenario.getLine();
        synchronized (PREV_FEATURE) {
            if (!currentFeature.equals(PREV_FEATURE.get())) {
                PREV_FEATURE.set(currentFeature);
                eventPublisher.publishEvent(new FeatureStartEvent(currentFeature));
            }
        }
    }

    /**
     * Publish feature end event.
     *
     * @param scenario the scenario
     */
    @After(order = Integer.MAX_VALUE)
    public void publishFeatureEndEvent(final Scenario scenario) {
        var currentFeature = scenario.getUri().toString() + scenario.getLine();
        synchronized (PREV_FEATURE) {
            if (!currentFeature.equals(PREV_FEATURE.get())) {
                PREV_FEATURE.remove();
                eventPublisher.publishEvent(new FeatureEndEvent(currentFeature));
            }
        }
    }

    /**
     * Clean context after scenario.
     */
    @After("not @SaveContext")
    public synchronized void cleanContextAfterScenario() {
        var counterParallelism =
                Integer.parseInt(System.getProperty(PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, "1"));
        if (counterParallelism == 1) {
            log.info("Clearing Context after Scenario execution...");
            cleanableBeans.forEach(Cleanable::clean);
        }
    }

    /**
     * Save context after scenario.
     */
    @After("@SaveContext")
    public synchronized void saveContextAfterScenario() {
        log.info("Saving Context after Scenario execution...");
        savableBeans.forEach(Savable::save);
    }
}
