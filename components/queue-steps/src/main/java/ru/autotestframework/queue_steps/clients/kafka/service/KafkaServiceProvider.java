package ru.autotestframework.queue_steps.clients.kafka.service;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Provides functionality for searching Kafka services by a specified identifier
 */
@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class KafkaServiceProvider {
    private final List<KafkaProducingMessageService> producingServices;
    private final List<KafkaConsumingMessageService> consumingServices;

    /**
     * Returns KafkaProducingMessageService by the specified ID
     *
     * @param identifier Kafka topic name, simple service name, or simple message type class name
     * @return {@link KafkaProducingMessageService}
     */
    public KafkaProducingMessageService getProducingService(String identifier) {
        return findService(producingServices, identifier);
    }

    /**
     * Returns KafkaConsumingMessageService by the specified ID
     *
     * @param identifier Kafka topic name, simple service name, or simple message type class name
     * @return {@link KafkaConsumingMessageService}
     */
    public KafkaConsumingMessageService getConsumingService(String identifier) {
        return findService(consumingServices, identifier);
    }

    /**
     * Find service t.
     *
     * @param <T>        the type parameter
     * @param services   the services
     * @param identifier the identifier
     * @return the t
     */
    public <T extends KafkaMessageService> T findService(List<T> services, String identifier) {
        return services.stream()
                .filter(service -> matchByName(service, identifier)
                        || matchByTopicName(service, identifier)
                        || matchByTopicPropertyName(service, identifier)
                        || matchByMessageType(service, identifier))
                .reduce((a, b) -> {
                    throw new AutotestException("Найдено более одного сервиса для параметра '{}'", identifier);
                })
                .orElseThrow(() -> new AutotestException("Не найден сервис для параметра '{}'", identifier));
    }

    private <T extends KafkaMessageService> boolean matchByName(T service, String serviceName) {
        return service.getClass().getSimpleName().equalsIgnoreCase(serviceName);
    }

    private <T extends KafkaMessageService> boolean matchByTopicName(T service, String topicName) {
        return service.getTopicName().equalsIgnoreCase(topicName);
    }

    private <T extends KafkaMessageService> boolean matchByTopicPropertyName(T service, String topicPropertyName) {
        return service.getTopicPropertyName().equalsIgnoreCase(topicPropertyName);
    }

    private <T extends KafkaMessageService> boolean matchByMessageType(T service, String messageType) {
        return service.getMessageType().getSimpleName().equalsIgnoreCase(messageType);
    }
}
