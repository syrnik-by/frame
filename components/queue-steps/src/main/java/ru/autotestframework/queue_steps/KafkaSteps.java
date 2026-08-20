package ru.autotestframework.queue_steps;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.ScenarioScope;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.AuthorizationException;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameters;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.queue_steps.clients.kafka.ConsumerConfigBean;
import ru.autotestframework.queue_steps.clients.kafka.service.*;
import ru.autotestframework.queue_steps.helpers.DataHelper;
import ru.autotestframework.queue_steps.helpers.JsonHelper;
import ru.autotestframework.queue_steps.helpers.ResolverHelper;

/**
 * The type Kafka steps.
 */
@AllArgsConstructor
@ScenarioScope
@Slf4j
public class KafkaSteps {

    private final Context context;
    private final ConsumerConfigBean consumerConfig;
    private final ObjectMapper objectMapper;
    private final KafkaMessageQueryService kafkaMessageQueryService;
    private final KafkaServiceProvider kafkaServiceProvider;
    private final JsonHelper jsonHelper;
    private final ResolverHelper resolverHelper;

    /**
     * Create output message.
     *
     * @param kafkaTopic the kafka topic
     * @param data       the data
     */
    @Given("сгенерировать сообщение для топика {string} с атрибутами:")
    @Sample("генерация сообщения для заданного топика")
    @Parameters({@Parameter(type = "string", name = "имя топика"), @Parameter(type = ":", name = "список переменных")})
    @Example(example = "Также сгенерировать сообщение для топика 'SEND_USER_VISA' с атрибутами:\n" + "| id | 123 |")
    public void createOutputMessage(String kafkaTopic, ResolvableMap data) {
        createOutputMessage("", kafkaTopic, "outputMessage", data);
    }

    /**
     * Send output message.
     *
     * @param kafkaTopic the kafka topic
     */
    @When("отправить сгенерированное сообщение в топик {string}")
    @Sample("отправить сгенерированное сообщение в заданный топик")
    @Parameter(type = "string", name = "имя топика")
    @Example(example = "Когда отправить сгенерированное сообщение в топик 'SEND_USER_VISA'")
    public void sendOutputMessage(String kafkaTopic) {
        sendMessage("outputMessage");
    }

    /**
     * Find message by values.
     *
     * @param kafkaTopic the kafka topic
     * @param data       the data
     */
    @And("найти запись в топике {resolvable_string} по значениям:")
    @Sample("Найти запись в заданном топике по значениям полей сообщения")
    @Parameters({
        @Parameter(type = "resolvable_string", name = "имя топика"),
        @Parameter(type = ":", name = "список переменных")
    })
    @Example(example = "Тогда найти запись в топике 'KEYCLOAK_JOURNAL_TOPIC' по значениям:\n" + "| level | INFO |")
    public void findMessageByValues(String kafkaTopic, ResolvableMap data) {
        var service = kafkaServiceProvider.getConsumingService(kafkaTopic);
        KafkaInputMessage message = kafkaMessageQueryService.findMessage(service, data);
        context.set(message.getClass().getSimpleName(), message);
    }

    /**
     * Check output message values.
     *
     * @param modelName the model name
     * @param data      the data
     */
    @Then("проверить значения полей записи {string}:")
    @Sample("Сравнение полей сообщения с заданными значениями")
    @Parameters({@Parameter(type = "string", name = "имя модели"), @Parameter(type = ":", name = "список переменных")})
    @Example(example = "И проверить значения полей записи 'sendVisaAbsMessage':\n" + "| docId | 1234 |")
    public void checkOutputMessageValues(String modelName, ResolvableMap data) {
        var model = context.getObj(modelName);
        DataHelper.compareFieldValuesByPath(data, model);
    }

    /**
     * Create output message.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param data            the data
     */
    @Given(
            "сгенерировать сообщение по {identifier} {resolvable_string} с атрибутами и сохранить в контекст с ключом {string}:")
    @Sample("генерация сообщения")
    @Parameters({
        @Parameter(
                type = "identifier",
                name = "имя топика из KafkaTopic/адрес топика из property файла/имени класса сообщения"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "string", name = "ключ объекта в контексте"),
        @Parameter(type = ":", name = "список атрибутов")
    })
    @Example(
            example =
                    "Затем сгенерировать сообщение по типу сообщения 'ClientNotificationEvent' с атрибутами и сохранить в контекст с ключом 'message':\n"
                            + "| notificationClientId | 2 |")
    @Example(
            example =
                    "Затем сгенерировать сообщение по имени топика 'client-notification-topic' с атрибутами и сохранить в контекст с ключом 'message':\n"
                            + "| notificationClientId | 2 |")
    public void createOutputMessage(String identifier, String identifierValue, String key, ResolvableMap data) {
        var service = kafkaServiceProvider.getProducingService(identifierValue);
        KafkaOutputMessage message = (KafkaOutputMessage) objectMapper.convertValue(
                jsonHelper.resolvePathMap(resolverHelper.resolve(data), service.getMessageType()),
                service.getMessageType());
        context.set(key, message);
    }

    /**
     * Send message.
     *
     * @param key     the key
     * @param headers the headers
     */
    @When("отправить в топик сообщение с ключом {resolvable_string} и хидерами:")
    @Sample("получить сообщение с указанным ключом из контекста и отправить с указанными хидерами")
    @Parameters({
        @Parameter(type = "resolvable_string", name = "ключ сообщения в контексте"),
        @Parameter(type = ":", name = "список атрибутов")
    })
    @Example(
            example = "Тогда отправить в топик сообщение с ключом 'outputMessage' и хидерами:\n"
                    + "|jwt|eyJhbGciOiJSUzI1NiIsInR5cCI...|")
    public void sendMessage(String key, ResolvableMap headers) {
        KafkaOutputMessage message = context.getObj(key);
        KafkaProducingMessageService service =
                kafkaServiceProvider.getProducingService(message.getClass().getSimpleName());
        service.sendMessage(message, headers);
    }

    /**
     * Send message.
     *
     * @param key the key
     */
    @When("отправить в топик сообщение с ключом {resolvable_string}")
    @Sample("получить сообщение с указанным ключом из контекста и отправить")
    @Parameter(type = "resolvable_string", name = "ключ сообщения в контексте")
    @Example(example = "Тогда отправить в топик сообщение с ключом 'outputMessage'")
    public void sendMessage(String key) {
        KafkaOutputMessage message = context.getObj(key);
        KafkaProducingMessageService service =
                kafkaServiceProvider.getProducingService(message.getClass().getSimpleName());
        service.sendMessage(message);
    }

    /**
     * Find message by values.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param data            the data
     */
    @And(
            "найти запись по {identifier} {resolvable_string} по значениям и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти запись по идентификатору по значениям и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ объекта в контексте"),
        @Parameter(type = ":", name = "список значений")
    })
    @Example(
            example =
                    "Затем найти запись по типу сообщения 'NotificationEventMessage' по значениям и сохранить в контекст с ключом 'notificationEvent':\n"
                            + "| notificationState | FILE_ERROR |")
    @Example(
            example =
                    "Затем найти запись по имени топика 'notification-event-topic' по значениям и сохранить в контекст с ключом 'notificationEvent':\n"
                            + "| notificationState | FILE_ERROR |")
    public void findMessageByValues(String identifier, String identifierValue, String key, ResolvableMap data) {
        KafkaConsumingMessageService service = kafkaServiceProvider.getConsumingService(identifierValue);
        KafkaInputMessage message = kafkaMessageQueryService.findMessage(service, data);
        context.set(key, message);
    }

    /**
     * Find messages by values.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param data            the data
     */
    @And(
            "найти записи по {identifier} {resolvable_string} по значениям и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти записи по идентификатору по значениям и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ списка в контексте"),
        @Parameter(type = ":", name = "список значений")
    })
    @Example(
            example =
                    "Затем найти записи по типу сообщения 'NotificationEventMessage' по значениям и сохранить в контекст с ключом 'notificationEvents':\n"
                            + "| notificationState | FILE_ERROR |")
    @Example(
            example =
                    "Затем найти записи по имени сервиса 'notificationService' по значениям и сохранить в контекст с ключом 'notificationEvents':\n"
                            + "| notificationState | FILE_ERROR |")
    public void findMessagesByValues(String identifier, String identifierValue, String key, ResolvableMap data) {
        KafkaConsumingMessageService service = kafkaServiceProvider.getConsumingService(identifierValue);
        List<KafkaInputMessage> messages = kafkaMessageQueryService.findMessages(service, data);
        context.set(key, messages);
    }

    /**
     * Find messages by filters.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param rows            the rows
     */
    @And(
            "найти записи по {identifier} {resolvable_string} по фильтру и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти записи по идентификатору по фильтру и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ списка в контексте"),
        @Parameter(type = ":", name = "список фильтров")
    })
    @Example(
            example =
                    "И найти записи по типу сообщения 'ReportingLogMessage' по фильтру и сохранить в контекст с ключом 'reportingLogs':\n"
                            + "| sourceSystem | == | Отчётность |")
    @Example(
            example =
                    "И найти записи по имени топика 'reporting-log-topic' по фильтру и сохранить в контекст с ключом 'reportingLogs':\n"
                            + "| sourceSystem | == | Отчётность |")
    public void findMessagesByFilters(String identifier, String identifierValue, String key, List<Triple> rows) {
        KafkaConsumingMessageService service = kafkaServiceProvider.getConsumingService(identifierValue);
        List<KafkaInputMessage> messages = kafkaMessageQueryService.findMessagesByFilter(service, rows);
        context.set(key, messages);
    }

    /**
     * Find message by filters.
     *
     * @param identifier      the identifier
     * @param identifierValue the identifier value
     * @param key             the key
     * @param rows            the rows
     */
    @And(
            "найти запись по {identifier} {resolvable_string} по фильтру и сохранить в контекст с ключом {resolvable_string}:")
    @Sample("Найти запись по идентификатору по фильтру и сохранить в контекст")
    @Parameters({
        @Parameter(type = "identifier", name = "идентификатор"),
        @Parameter(type = "resolvable_string", name = "значение идентификатора"),
        @Parameter(type = "resolvable_string", name = "ключ объекта в контексте"),
        @Parameter(type = ":", name = "список фильтров")
    })
    @Example(
            example =
                    "И найти запись по типу сообщения 'ReportingLogMessage' по фильтру и сохранить в контекст с ключом 'reportingLog':\n"
                            + "| sourceSystem | == | Отчётность |")
    @Example(
            example =
                    "И найти запись по имени сервиса 'reportingService' по фильтру и сохранить в контекст с ключом 'reportingLog':\n"
                            + "| sourceSystem | == | Отчётность |")
    public void findMessageByFilters(String identifier, String identifierValue, String key, List<Triple> rows) {
        KafkaConsumingMessageService service = kafkaServiceProvider.getConsumingService(identifierValue);
        List<KafkaInputMessage> messages = kafkaMessageQueryService.findMessagesByFilter(service, rows);
        if (messages.size() > 1)
            throw new AutotestException("Найдено больше одного сообщения соответствующего фильтрам");
        context.set(key, messages.get(0));
    }

    /**
     * Gets message from list.
     *
     * @param index      the index
     * @param listKey    the list key
     * @param messageKey the message key
     */
    @And("получить сообщение с индексом {int} из списка c ключом {string} и сохранить в контекст с ключом {string}")
    @Sample("получение сообщения из списка по индексу элемента")
    @Parameters({
        @Parameter(type = "int", name = "индекс элемента в списке"),
        @Parameter(type = "string", name = "ключ списка в контексте"),
        @Parameter(type = "string", name = "ключ объекта в контексте")
    })
    @Example(
            example =
                    "Также получить сообщение с индексом 0 из списка с ключом 'createTaskCommandMessages' и сохранить в контекст с ключом 'createTaskCommand'")
    public void getMessageFromList(int index, String listKey, String messageKey) {
        Object message = kafkaMessageQueryService.getMessageFromList(index, listKey);
        context.set(messageKey, message);
    }

    /**
     * Identifier string.
     *
     * @param value the value
     * @return the string
     */
    @ParameterType("имени топика|проперти топика|имени сервиса|типу сообщения")
    public String identifier(String value) {
        return value;
    }

    /**
     * Kafka check.
     */
    @Before("@kafka")
    public void kafkaCheck() {
        try (KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(consumerConfig.getConsumerProperties())) {
            consumer.listTopics();
        } catch (AuthenticationException | AuthorizationException e) {
            throw new AutotestException("Проблема соединения с кафкой", e);
        }
    }
}
