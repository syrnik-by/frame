package ru.autotestframework.queue_steps.clients.kafka.service;

import java.util.List;
import java.util.Map;

/**
 * Interface for message receiving methods
 */
public interface KafkaConsumingMessageService extends KafkaMessageService {
    /**
     * Adds an incoming message to the storage
     *
     * @param message KafkaInputMessage incoming message
     */
    void addMessageToStore(KafkaInputMessage message);

    /**
     * Returns all saved storage messages
     *
     * @return message list
     */
    List<? extends KafkaInputMessage> getAllMessages();

    /**
     * Receives a message based on the specified parameters
     *
     * @param <T>  the type parameter
     * @param data {@code Map<String, T>} parameters for searching for a message, where key = field name, value = field value
     * @return KafkaInputMessage message by values
     */
    <T> KafkaInputMessage getMessageByValues(Map<String, T> data);

    /**
     * Clearing the storage
     */
    void cleanStorage();

    /**
     * Using the universal consumer. If necessary, use a custom one
     * the consumer, then redefine to false and create your own consumer
     *
     * @return returns true by default.
     */
    default boolean useUniversalConsumer() {
        return true;
    }
}
