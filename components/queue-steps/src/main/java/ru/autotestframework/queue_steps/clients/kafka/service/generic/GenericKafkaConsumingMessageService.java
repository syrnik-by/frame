package ru.autotestframework.queue_steps.clients.kafka.service.generic;

import static ru.autotestframework.queue_steps.helpers.DataHelper.isAllFieldsEqualToValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaConsumingMessageService;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaInputMessage;

/**
 * Generalized Kafka message consumption service implementing {@link KafkaConsumingMessageService}.
 * Designed for storing and processing incoming messages of a certain type.
 *
 * <p><b>Example of adding a new service via the {@code @Bean} annotation:</b></p>
 *
 * <pre>
 * {@code
 * /@Bean
 * public KafkaConsumingMessageService myMessageConsumingService() {
 * return new GenericKafkaConsumingMessageService(
 * "MY_KAF_KA_TOPIC", // Topic name
 * MyKafkaMessage.class // Message class
 * * useUniversalConsumer // If automatic creation of a consumer is not required, you can pass false.
 *     );
 * }
 * }* </pre>
 *
 * @param <T> message type inheriting {@link KafkaInputMessage}
 */
public class GenericKafkaConsumingMessageService<T extends KafkaInputMessage> implements KafkaConsumingMessageService {
    private final List<T> messageStore = Collections.synchronizedList(new ArrayList<>());
    private final String topicName;
    private final String topicPropertyName;
    private final Class<T> messageType;
    private final boolean useUniversalConsumer;

    /**
     * Instantiates a new Generic kafka consuming message service.
     *
     * @param topicPropertyName the topic property name
     * @param topicName         the topic name
     * @param messageType       the message type
     */
    public GenericKafkaConsumingMessageService(String topicPropertyName, String topicName, Class<T> messageType) {
        this(topicPropertyName, topicName, messageType, true);
    }

    /**
     * Instantiates a new Generic kafka consuming message service.
     *
     * @param topicPropertyName    the topic property name
     * @param topicName            the topic name
     * @param messageType          the message type
     * @param useUniversalConsumer the use universal consumer
     */
    public GenericKafkaConsumingMessageService(
            String topicPropertyName, String topicName, Class<T> messageType, boolean useUniversalConsumer) {
        this.topicName = topicName;
        this.topicPropertyName = topicPropertyName;
        this.messageType = messageType;
        this.useUniversalConsumer = useUniversalConsumer;
    }

    @Override
    public void addMessageToStore(KafkaInputMessage message) {
        messageStore.add((T) message);
    }

    @Override
    public List<? extends KafkaInputMessage> getAllMessages() {
        return messageStore;
    }

    @Override
    public <V> KafkaInputMessage getMessageByValues(Map<String, V> data) {
        synchronized (messageStore) {
            return messageStore.stream()
                    .filter(m -> isAllFieldsEqualToValue(m, data))
                    .reduce((a, b) -> {
                        throw new AutotestException(
                                "Найдено больше одного сообщения соответствующего фильтру: {}, {}", a, b);
                    })
                    .orElseThrow(() -> new AutotestException(
                            "Не найдено сообщение в топике '{}' со значениями: {}", topicPropertyName, data));
        }
    }

    @Override
    public void cleanStorage() {
        messageStore.clear();
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
    public boolean useUniversalConsumer() {
        return useUniversalConsumer;
    }

    @Override
    public String getTopicPropertyName() {
        return this.topicPropertyName;
    }
}
