package ru.autotestframework.queue_steps.clients.kafka.service.generic;

import static ru.autotestframework.queue_steps.helpers.DataHelper.isAllFieldsEqualToValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaInputMessage;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaRecordConsumingService;

/**
 * A service that extends the capabilities of {@link GenericKafkaConsumingMessageService},
 * to additionally save and process ConsumerRecord records.
 *
 * @param <T> the type parameter
 */
public class GenericKafkaConsumingRecordService<T extends KafkaInputMessage>
        extends GenericKafkaConsumingMessageService<T> implements KafkaRecordConsumingService {

    private final List<ConsumerRecord<String, KafkaInputMessage>> recordStore =
            Collections.synchronizedList(new ArrayList<>());

    /**
     * Basic constructor — we use the logic of the parent service.
     *
     * @param topicTitle is the "human-readable" name of the topic
     * @param topicName is the real name of the topic in Kafka
     * @param messageType class implementing KafkaInputMessage
     */
    public GenericKafkaConsumingRecordService(String topicTitle, String topicName, Class<T> messageType) {
        super(topicTitle, topicName, messageType, true);
    }

    /**
     * Instantiates a new Generic kafka consuming record service.
     *
     * @param topicTitle           the topic title
     * @param topicName            the topic name
     * @param messageType          the message type
     * @param useUniversalConsumer the use universal consumer
     */
    public GenericKafkaConsumingRecordService(
            String topicTitle, String topicName, Class<T> messageType, boolean useUniversalConsumer) {
        super(topicTitle, topicName, messageType, useUniversalConsumer);
    }

    /**
     * Implementation of the method for adding Record
     */
    @Override
    public void addRecordToStore(ConsumerRecord<String, KafkaInputMessage> record) {
        recordStore.add(record);
    }

    /**
     * Returning all saved ConsumerRecord
     */
    @Override
    public List<ConsumerRecord<String, KafkaInputMessage>> getAllRecords() {
        return recordStore;
    }

    /**
     * Additionally, we clean the Records storage
     */
    @Override
    public void cleanStorage() {
        super.cleanStorage();
        recordStore.clear();
    }

    @Override
    public List<ConsumerRecord<String, KafkaInputMessage>> getRecordsByValues(Map<String, ?> data) {
        synchronized (recordStore) {
            Map<String, ?> withoutHeaders = filterDataWithoutHeaders(data);
            Map<String, ?> headerData = filterDataWithHeaders(data);

            return recordStore.stream()
                    .filter(record -> isRecordMatching(record, withoutHeaders, headerData))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ConsumerRecord<String, KafkaInputMessage> getRecordByValues(Map<String, ?> data) {
        List<ConsumerRecord<String, KafkaInputMessage>> matchedRecords = getRecordsByValues(data);

        if (matchedRecords.size() > 1) {
            throw new AutotestException("Найдено больше одного ConsumerRecord соответствующего фильтру");
        }

        return matchedRecords.stream()
                .findFirst()
                .orElseThrow(() -> new AutotestException("Не найден ConsumerRecord со значениями: " + data));
    }

    /**
     * Filtering data for headers (starting with "headers.").
     */
    private Map<String, ?> filterDataWithHeaders(Map<String, ?> data) {
        return data.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("headers."))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Filtering data for fields without headers.
     */
    private Map<String, ?> filterDataWithoutHeaders(Map<String, ?> data) {
        return data.entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith("headers."))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Checking that the record matches the data (fields and headers).
     */
    private boolean isRecordMatching(
            ConsumerRecord<String, KafkaInputMessage> record,
            Map<String, ?> withoutHeaders,
            Map<String, ?> headerData) {
        return isAllFieldsEqualToValue(record, withoutHeaders) && isAllHeadersEqualToValue(record, headerData);
    }

    private boolean isAllHeadersEqualToValue(
            ConsumerRecord<String, KafkaInputMessage> record, Map<String, ?> headerData) {
        return headerData.entrySet().stream().allMatch(entry -> {
            String headerName = entry.getKey().substring("headers.".length());
            String expectedHeaderValue = entry.getValue().toString();
            String actualHeaderValue = getHeaderValue(record, headerName);
            return expectedHeaderValue.equals(actualHeaderValue);
        });
    }

    private String getHeaderValue(ConsumerRecord<String, KafkaInputMessage> record, String headerName) {
        return record.headers().lastHeader(headerName) != null
                ? new String(record.headers().lastHeader(headerName).value())
                : null;
    }
}
