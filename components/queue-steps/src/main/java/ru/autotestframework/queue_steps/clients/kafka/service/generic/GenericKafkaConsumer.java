package ru.autotestframework.queue_steps.clients.kafka.service.generic;

import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaConsumingMessageService;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaInputMessage;

/**
 * A generalized Kafka consumer for processing messages from multiple topics and message types.
 * Uses a universal listener to consume messages and distribute them to the appropriate services.
 */
@Component
@Slf4j
@Data
public class GenericKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final Map<String, List<Class<?>>> topicToMessageTypes = new HashMap<>();
    private final Map<Class<?>, KafkaConsumingMessageService> messageTypeToService = new HashMap<>();
    private final String PROCESSING_ERROR_MSG = "Не удалось обработать сообщение из топика {}: {}";

    /**
     * Instantiates a new Generic kafka consumer.
     *
     * @param objectMapper      the object mapper
     * @param consumingServices the consuming services
     */
    public GenericKafkaConsumer(ObjectMapper objectMapper, List<KafkaConsumingMessageService> consumingServices) {
        this.objectMapper = objectMapper;

        for (KafkaConsumingMessageService service : consumingServices) {
            if (service.useUniversalConsumer()) {
                String topic = service.getTopicPropertyName();
                Class<?> messageType = service.getMessageType();

                topicToMessageTypes
                        .computeIfAbsent(topic, k -> new ArrayList<>())
                        .add(messageType);
                messageTypeToService.put(messageType, service);
            }
        }
    }

    /**
     * Listen.
     *
     * @param record the record
     */
    @KafkaListener(
            autoStartup = "#{@universalConsumerTopicsProvider.isNotEmpty()}",
            topics = "#{@universalConsumerTopicsProvider.getUniversalConsumerTopics()}",
            containerFactory = "concurrentKafkaListenerContainerFactory",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=java.lang.String", "spring.json.use.type.headers=false"})
    public void listen(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String messageJson = record.value();

        List<Class<?>> messageTypes = topicToMessageTypes.getOrDefault(topic, Collections.emptyList());
        boolean messageProcessed = false;

        for (Class<?> messageType : messageTypes) {
            try {
                KafkaInputMessage message = (KafkaInputMessage) objectMapper.readValue(messageJson, messageType);
                KafkaConsumingMessageService service = Objects.requireNonNull(
                        messageTypeToService.get(messageType),
                        () -> "Сервис не найден для типа сообщения " + messageType);
                if (service instanceof GenericKafkaConsumingRecordService) {
                    ((GenericKafkaConsumingRecordService<?>) service)
                            .addRecordToStore(transformConsumerRecord(record, message));
                }
                service.addMessageToStore(message);
                log.info("Получено сообщение типа {} из топика {}: {}", messageType.getSimpleName(), topic, message);
                messageProcessed = true;
                break;
            } catch (Exception e) {
                log.info(PROCESSING_ERROR_MSG, topic, messageJson);
            }
        }

        if (!messageProcessed) {
            log.warn(PROCESSING_ERROR_MSG, topic, messageJson);
        }
    }

    private <T> ConsumerRecord<String, T> transformConsumerRecord(ConsumerRecord<String, String> record, T message) {
        return new ConsumerRecord<>(
                record.topic(),
                record.partition(),
                record.offset(),
                record.timestamp(),
                record.timestampType(),
                record.serializedKeySize(),
                record.serializedValueSize(),
                record.key(),
                message,
                record.headers(),
                record.leaderEpoch());
    }
}
