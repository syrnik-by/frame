package ru.autotestframework.core.spring;

import org.springframework.context.ApplicationEvent;

/**
 * An event sent to Spring when the feature is launched.
 */
public class FeatureStartEvent extends ApplicationEvent {

    /**
     * Instantiates a new Feature start event.
     *
     * @param featureUri the feature uri
     */
    public FeatureStartEvent(final String featureUri) {
        super(featureUri);
    }
}
