package ru.autotestframework.queue_steps.clients.kafka.service.generic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaConsumingMessageService;

/**
 * This provider is used to avoid looping when creating a bean {@link GenericKafkaConsumer}
 */
@Component
public class UniversalConsumerTopicsProvider {
    private final String[] universalConsumerTopics;
    private boolean isNotEmpty = true;

    /**
     * Instantiates a new Universal consumer topics provider.
     *
     * @param consumingServices the consuming services
     */
    public UniversalConsumerTopicsProvider(List<KafkaConsumingMessageService> consumingServices) {
        Set<String> topicsSet = new HashSet<>();
        for (KafkaConsumingMessageService service : consumingServices) {
            if (service.useUniversalConsumer()
                    && !service.getTopicPropertyName().isEmpty()) {
                topicsSet.add(service.getTopicPropertyName());
            }
        }
        if (topicsSet.isEmpty()) {
            topicsSet.add("emptyTopic");
            isNotEmpty = false;
        }
        this.universalConsumerTopics = topicsSet.toArray(new String[0]);
    }

    /**
     * Get universal consumer topics string [ ].
     *
     * @return the string [ ]
     */
    public String[] getUniversalConsumerTopics() {
        return universalConsumerTopics;
    }

    /**
     * Is not empty boolean.
     *
     * @return the boolean
     */
    public boolean isNotEmpty() {
        return isNotEmpty;
    }
}
