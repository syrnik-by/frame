package ru.autotestframework.queue_steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.junit.jupiter.api.Assertions;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameters;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaInputMessage;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaRecordConsumingService;
import ru.autotestframework.queue_steps.clients.kafka.service.KafkaServiceProvider;
import ru.autotestframework.queue_steps.helpers.DataHelper;

/**
 * The type Kafka record steps.
 */
@AllArgsConstructor
@ScenarioScope
@Slf4j
public class KafkaRecordSteps {
    private final KafkaServiceProvider kafkaServiceProvider;
    private final Context context;

    /**
     * Find record by values.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param data            the data
     */
    @When("найти запись по {identifier} {resolvable_string} и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти запись в топике по значениям и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ списка в контексте"),
        @Parameter(type = ":", name = "список значений")
    })
    @Example(
            example =
                    "Когда найти запись по типу сообщения 'CreateCommandTaskMessage' и сохранить в контекст с ключом 'commandTask':"
                            + "| uid | ${{getObjectField:applicationOpenAccountControlAccount:taskUuid}} |")
    public void findRecordByValues(String identifier, String identifierValue, String key, ResolvableMap data) {
        var recordService = (KafkaRecordConsumingService) kafkaServiceProvider.getConsumingService(identifierValue);
        log.info("Поиск записи...\nтопик: {}\nфильтр(ы): {}", recordService.getTopicPropertyName(), data);
        var record = recordService.getRecordByValues(data);
        log.info("Запись найдена:\nсообщение:{}\nзаголовок:{}", record.value(), convertHeader(record.headers()));
        context.set(key, record);
    }

    /**
     * Find records by values.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param data            the data
     */
    @And(
            "найти записи ConsumerRecord по {identifier} {resolvable_string} по значениям и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти записи по идентификатору по значениям и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ списка в контексте"),
        @Parameter(type = ":", name = "список значений")
    })
    @Example(
            example =
                    "Затем найти записи ConsumerRecord по типу сообщения 'NotificationEventMessage' по значениям и сохранить в контекст с ключом 'notificationEvents':\n"
                            + "| notificationState | FILE_ERROR |")
    @Example(
            example =
                    "Затем найти записи ConsumerRecord по имени сервиса 'notificationService' по значениям и сохранить в контекст с ключом 'notificationEvents':\n"
                            + "| notificationState | FILE_ERROR |")
    public void findRecordsByValues(String identifier, String identifierValue, String key, ResolvableMap data) {
        var recordService = (KafkaRecordConsumingService) kafkaServiceProvider.getConsumingService(identifierValue);
        List<ConsumerRecord<String, KafkaInputMessage>> messages = recordService.getRecordsByValues(data);
        context.set(key, messages);
    }

    /**
     * Compare record message values.
     *
     * @param key  the key
     * @param data the data
     */
    @Then("проверить значения записи из контекста с ключом {resolvable_string}:")
    @Sample("сравнение полей объекта, сохраненного в контекст, с заданными параметрами")
    @Parameters({
        @Parameter(type = "resolvable_string", name = "ключ объекта в контексте"),
        @Parameter(type = ":", name = "список переменных")
    })
    @Example(
            example = "Тогда проверить значения сообщения из контекста с ключом 'commandTask':"
                    + "| uid | ${{getObjectField:applicationOpenAccountControlAccount:taskUuid}} |")
    public void compareRecordMessageValues(String key, ResolvableMap data) {
        ConsumerRecord<String, KafkaInputMessage> record = context.getObj(key);
        DataHelper.compareFieldValuesByPath(data, record.value());
    }

    /**
     * Contain record header values.
     *
     * @param key  the key
     * @param data the data
     */
    @Then("проверить, что заголовки ConsumerRecord из контекста с ключом {resolvable_string} содержат значения:")
    @Sample("проверить наличие передаваемых заголовков у объекта ConsumerRecord из контекста с ключом")
    @Parameters({
        @Parameter(type = "resolvable_string", name = "ключ ConsumerRecord в контексте"),
        @Parameter(type = ":", name = "список переменных")
    })
    @Example(
            example = "проверить, что заголовки ConsumerRecord из контекста с ключом 'commonTask' содержат значения:"
                    + "| __TypeId__ | ru.tecforce.smgoz.tasklogistic.service.kafka.models.CreateNewTaskCommand |")
    public void containRecordHeaderValues(String key, ResolvableMap data) {
        ConsumerRecord<String, KafkaInputMessage> record = context.getObj(key);
        Map<String, String> header = convertHeader(record.headers());
        Assertions.assertTrue(
                header.entrySet().containsAll(data.entrySet()),
                String.format("Заголовок %s не содержит ожидаемых значений %s", header, data));
    }

    /**
     * Convert header map.
     *
     * @param headers the headers
     * @return the map
     */
    public Map<String, String> convertHeader(Headers headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (Header header : headers) {
            String key = header.key();
            String value = new String(header.value());
            headerMap.put(key, value);
        }
        return headerMap;
    }
}
