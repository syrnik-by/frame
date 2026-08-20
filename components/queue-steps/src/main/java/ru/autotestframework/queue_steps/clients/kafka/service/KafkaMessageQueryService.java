package ru.autotestframework.queue_steps.clients.kafka.service;

import static ru.autotestframework.queue_steps.helpers.DataHelper.isAllFieldsEqualToValue;
import static ru.autotestframework.queue_steps.helpers.DataHelper.isAllFieldsMatchToValue;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;

/**
 * The type Kafka message query service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageQueryService {

    private final Context context;

    /**
     * Searches for one message in the specified consumer service that corresponds to the specified field values.
     *
     * @param service Kafka consumer search service
     * @param data field values for comparison
     * @return found message {@link KafkaInputMessage}
     * @throws AutotestException if the message is not found
     */
    public KafkaInputMessage findMessage(KafkaConsumingMessageService service, ResolvableMap data) {
        log.info("Поиск сообщения в топике '{}' по данным: {}", service.getTopicPropertyName(), data);
        return service.getMessageByValues(data);
    }

    /**
     * Searches for a unique message in the specified consumer service that matches the specified filters.
     *
     * @param service Kafka consumer search service
     * @param rows list of filtering conditions
     * @return found message {@link KafkaInputMessage}
     * @throws AutotestException if a message is not found or more than one message is found.
     */
    public KafkaInputMessage findMessageByFilter(KafkaConsumingMessageService service, List<Triple> rows) {
        List<KafkaInputMessage> filteredMessages = findMessagesByFilter(service, rows);
        return handleSingleMatch(filteredMessages, service.getTopicPropertyName(), rows);
    }

    /**
     * Searches for all messages in the specified consumer service that match the specified field values.
     *
     * @param service Kafka consumer search service
     * @param data field values for comparison
     * @return list of found messages {@link KafkaInputMessage}
     */
    public List<KafkaInputMessage> findMessages(KafkaConsumingMessageService service, ResolvableMap data) {
        log.info("Поиск сообщений в топике '{}' по данным: {}", service.getTopicName(), data);
        return findMessagesWithPredicate(service, x -> isAllFieldsEqualToValue(x, data));
    }

    /**
     * Searches for all messages in the specified consumer service that match the specified filters.
     *
     * @param service Kafka consumer search service
     * @param rows list of filtering conditions
     * @return list of found messages {@link KafkaInputMessage}
     */
    public List<KafkaInputMessage> findMessagesByFilter(KafkaConsumingMessageService service, List<Triple> rows) {
        log.info("Поиск сообщений в топике '{}' по фильтрам: {}", service.getTopicName(), rows);
        return findMessagesWithPredicate(service, x -> isAllFieldsMatchToValue(x, rows));
    }

    /**
     * Retrieves a message from a list stored in the context by its index.
     *
     * @param index the index of the message in the list
     * @param listKey is the key of the list in the context
     * @return message object
     * @throws AutotestException if the list is not found, the index goes outside the list or the object is not a Kafka message
     */
    public KafkaInputMessage getMessageFromList(int index, String listKey) {
        List<KafkaInputMessage> messagesList = context.getObj(listKey);
        try {
            return messagesList.get(index);
        } catch (NullPointerException e) {
            throw new AutotestException("Сообщение под индексом {} в списке {} отсутствует", e, index, listKey);
        }
    }

    private List<KafkaInputMessage> findMessagesWithPredicate(
            KafkaConsumingMessageService service, Predicate<KafkaInputMessage> predicate) {
        synchronized (service.getAllMessages()) {
            return service.getAllMessages().stream().filter(predicate).collect(Collectors.toList());
        }
    }

    private KafkaInputMessage handleSingleMatch(List<KafkaInputMessage> messages, String topicName, List<Triple> rows) {
        if (messages.size() != 1) {
            throw new AutotestException(
                    "В топике '{}' найдено '{}' сообщений, соответствующих фильтру {}. Уточните критерии поиска",
                    topicName,
                    messages.size(),
                    rows);
        }
        return messages.get(0);
    }
}
