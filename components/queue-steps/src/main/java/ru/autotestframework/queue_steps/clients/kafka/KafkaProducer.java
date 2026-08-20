package ru.autotestframework.queue_steps.clients.kafka;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import ru.autotestframework.core.exception.AutotestException;

/**
 * The type Kafka producer.
 *
 * @param <T> the type parameter
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Slf4j
public class KafkaProducer<T> {

    private final String topic;
    private final KafkaTemplate<String, T> kafkaTemplate;
    private Headers preconfiguredHeaders;

    /**
     * Sending a message with the ability to reconfigure headers
     *
     * @param value the value
     */
    public void send(T value) {
        ProducerRecord<String, T> record;
        record = new ProducerRecord<>(topic, 0, null, "auto-test", value, preconfiguredHeaders);
        kafkaTemplate.send(record);
        kafkaTemplate.flush();
        log.info("Сообщение {} отправлено в топик {}", value, topic);
    }

    /**
     * Sending a message with custom headers
     *
     * @param value   the value
     * @param headers the headers
     */
    public void send(T value, Map<String, String> headers) {
        ProducerRecord<String, T> record = getProducerRecord(value, headers);
        kafkaTemplate.send(record);
        kafkaTemplate.flush();
        log.info("Сообщение {} отправлено в топик {} с хидерами {}", value, topic, headers);
    }

    private ProducerRecord<String, T> getProducerRecord(T value, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            throw new AutotestException(
                    "Пришли пустые хидеры. Используйте метод send(T value), если сообщение не имеет хидеров");
        }
        ProducerRecord<String, T> record = new ProducerRecord<>(topic, value);
        headers.forEach((key, val) -> record.headers().add(new RecordHeader(key, val.getBytes())));
        return record;
    }
}
