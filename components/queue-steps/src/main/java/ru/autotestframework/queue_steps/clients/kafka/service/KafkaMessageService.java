package ru.autotestframework.queue_steps.clients.kafka.service;

/**
 * A common interface for services to send and receive messages
 */
public interface KafkaMessageService {
    /**
     * Returns the name of the topic that the implementing service is working with
     *
     * @return topic name
     */
    String getTopicName();

    /**
     * Returns the name of the kafka topic that we use to access the queue service.
     * For example: auto8.tf-smgoz-jvm-service-noticegz.out.logs
     *
     * @return returns an empty string by default (For legacy modules). When redefined, it returns the full name of the topic.
     */
    default String getTopicPropertyName() {
        return "";
    }

    /**
     * Returns the message class that the implementing service is working with
     *
     * @return {@code Class<?>} message class
     */
    Class<?> getMessageType();
}
