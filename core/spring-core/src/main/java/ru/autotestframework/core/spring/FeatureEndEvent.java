package ru.autotestframework.core.spring;

import org.springframework.context.ApplicationEvent;

/**
 * An event sent to Spring when the feature is completed.
 */
public class FeatureEndEvent extends ApplicationEvent {

    /**
     * Instantiates a new Feature end event.
     *
     * @param featureUri the feature uri
     */
    public FeatureEndEvent(final String featureUri) {
        super(featureUri);
    }
}
