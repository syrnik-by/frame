package ru.autotestframework.queue_steps.clients.kafka.service.generic;

import java.util.Map;
import ru.autotestframework.queue_steps.clients.kafka.KafkaProducer;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaOutputMessage;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaProducingMessageService;

/**
 * A generalized Kafka message sending service that implements {@link KafkaProducingMessageService}.
 * Designed to send messages of a certain type to a specified topic.
 *
 * <p><b>Example of adding a new service via the {@code @Bean} annotation:</b></p>
 *
 * <pre>
 * {@code
 * /@Bean
 * public KafkaProducingMessageService myMessageProducingService(KafkaProducer <MyKafkaMessage> kafkaProducer) {
 * return new GenericKafkaProducingMessageService(
 * KafkaProducer, // Kafka Producer
 * "MY_KAF_KA_TOPIC", // Topic name
 * MyKafkaMessage.class // Message class
 *     );
 * }
 * }*
 * </pre>
 *
 * @param <T> message type inheriting {@link KafkaOutputMessage}
 */
public class GenericKafkaProducingMessageService<T extends KafkaOutputMessage> implements KafkaProducingMessageService {
    private final KafkaProducer<T> kafkaProducer;
    private final String topicName;
    private final String topicProperty;
    private final Class<T> messageType;

    /**
     * Instantiates a new Generic kafka producing message service.
     *
     * @param kafkaProducer the kafka producer
     * @param topicName     the topic name
     * @param messageType   the message type
     * @param topicProperty the topic property
     */
    public GenericKafkaProducingMessageService(
            KafkaProducer<T> kafkaProducer, String topicName, Class<T> messageType, String topicProperty) {
        this.topicProperty = topicProperty;
        this.kafkaProducer = kafkaProducer;
        this.topicName = topicName;
        this.messageType = messageType;
    }

    @Override
    public void sendMessage(KafkaOutputMessage msg) {
        kafkaProducer.send((T) msg);
    }

    @Override
    public void sendMessage(KafkaOutputMessage msg, Map<String, String> headers) {
        kafkaProducer.send((T) msg, headers);
    }

    @Override
    public String getTopicName() {
        return topicName;
    }

    @Override
    public Class<?> getMessageType() {
        return messageType;
    }

    @Override
    public String getTopicPropertyName() {
        return topicProperty;
    }
}
