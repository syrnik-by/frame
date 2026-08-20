package ru.autotestframework.queue_steps.clients.kafka.service;

import java.util.Map;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Token interface for outgoing messages
 */
public interface KafkaProducingMessageService extends KafkaMessageService {
    /**
     * Sends an outgoing message to the topic
     *
     * @param msg KafkaOutputMessage outgoing message
     */
    void sendMessage(KafkaOutputMessage msg);

    /**
     * Sends an outgoing message to a topic with hiders.
     * Currently default, so as not to break the logic of legacy modules.
     *
     * @param msg KafkaOutputMessage outgoing message
     * @param headers Map{@code <String, String> } a map containing hiders
     */
    default void sendMessage(KafkaOutputMessage msg, Map<String, String> headers) {
        throw new AutotestException("Перед использованием реализуйте метод в своем сервисе: " + this.getClass());
    }
}
