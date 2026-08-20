package ru.autotestframework.queue_steps.clients.kafka.service;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Interface for working with ConsumerRecord coming from Kafka
 */
public interface KafkaRecordConsumingService extends KafkaConsumingMessageService {

    /**
     * Adds ConsumerRecord to storage
     *
     * @param record a record read from Kafka
     */
    void addRecordToStore(ConsumerRecord<String, KafkaInputMessage> record);

    /**
     * Returns all saved ConsumerRecord
     *
     * @return list of ConsumerRecord
     */
    List<ConsumerRecord<String, KafkaInputMessage>> getAllRecords();

    /**
     * Returns 1 entry by filter
     *
     * @param data filter
     * @return entry from kafka topic
     */
    ConsumerRecord<String, KafkaInputMessage> getRecordByValues(Map<String, ?> data);

    /**
     * Returns a list of entries by filter
     *
     * @param data filter
     * @return list of kafka topic entries
     */
    List<ConsumerRecord<String, KafkaInputMessage>> getRecordsByValues(Map<String, ?> data);
}
