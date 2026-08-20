package ru.autotestframework.queue_steps;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.queue_steps.clients.kafka.service.*;
import ru.autotestframework.queue_steps.helpers.JsonHelper;
import ru.autotestframework.queue_steps.helpers.ResolverHelper;

/**
 * The type Kafka steps test.
 */
@Tag("@QueueDemo")
@ExtendWith(MockitoExtension.class)
public class KafkaStepsTest {

    @Mock
    private Context context;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaServiceProvider kafkaServiceProvider;

    @Mock
    private KafkaMessageQueryService kafkaMessageQueryService;

    @Mock
    private JsonHelper jsonHelper;

    @Mock
    private ResolverHelper resolverHelper;

    @InjectMocks
    private KafkaSteps steps;

    /**
     * Should save generated message into context.
     */
    @Test
    public void shouldSaveGeneratedMessageIntoContext() {
        String kafkaTopic = "topic";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaOutputMessage message = mock(KafkaOutputMessage.class);
        KafkaProducingMessageService producingService = mock(KafkaProducingMessageService.class);

        when(kafkaServiceProvider.getProducingService(kafkaTopic)).thenReturn(producingService);
        doReturn(KafkaOutputMessage.class).when(producingService).getMessageType();
        when(objectMapper.convertValue(any(Map.class), any(Class.class))).thenReturn(message);

        steps.createOutputMessage(kafkaTopic, data);

        verify(context).set(eq("outputMessage"), eq(message));
    }

    /**
     * Should send message.
     */
    @Test
    public void shouldSendMessage() {

        String key = "outputMessage";
        ResolvableMap headers =
                new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaOutputMessage message = mock(KafkaOutputMessage.class);
        KafkaProducingMessageService producingService = mock(KafkaProducingMessageService.class);

        when(context.getObj(key)).thenReturn(message);
        when(kafkaServiceProvider.getProducingService(message.getClass().getSimpleName()))
                .thenReturn(producingService);

        steps.sendMessage(key, headers);

        verify(producingService).sendMessage(eq(message), eq(headers));
    }

    /**
     * Should find message by values and store it in context.
     */
    @Test
    public void shouldFindMessageByValuesAndStoreItInContext() {

        String kafkaTopic = "topic";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaInputMessage expectedMessage = mock(KafkaInputMessage.class);

        KafkaConsumingMessageService consumingService = mock(KafkaConsumingMessageService.class);
        when(kafkaServiceProvider.getConsumingService(kafkaTopic)).thenReturn(consumingService);
        when(kafkaMessageQueryService.findMessage(consumingService, data)).thenReturn(expectedMessage);

        steps.findMessageByValues(kafkaTopic, data);

        verify(context).set(eq(expectedMessage.getClass().getSimpleName()), eq(expectedMessage));
    }

    /**
     * Should compare fields successfully.
     */
    @Test
    public void shouldCompareFieldsSuccessfully() {

        String modelName = "model";
        ResolvableMap data = new ResolvableMap(
                new HashMap<>(),
                new PlaceholderResolverImpl(new StringSubstitutor())); // наполняем данными для сравнения

        Object model = new Object();

        when(context.getObj(modelName)).thenReturn(model);

        assertDoesNotThrow(() -> steps.checkOutputMessageValues(modelName, data));
    }

    /**
     * Should generate and store message in context.
     */
    @Test
    public void shouldGenerateAndStoreMessageInContext() {

        String identifier = "client-notification-topic";
        String identifierValue = "value";
        String key = "outputMessage";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaOutputMessage expectedMessage = mock(KafkaOutputMessage.class);

        KafkaProducingMessageService service = mock(KafkaProducingMessageService.class);
        when(kafkaServiceProvider.getProducingService(identifierValue)).thenReturn(service);
        doReturn(KafkaOutputMessage.class).when(service).getMessageType();
        when(objectMapper.convertValue(any(Map.class), any(Class.class))).thenReturn(expectedMessage);

        steps.createOutputMessage(identifier, identifierValue, key, data);

        verify(context).set(eq(key), eq(expectedMessage));
    }

    /**
     * Should send message with provided headers.
     */
    @Test
    public void shouldSendMessageWithProvidedHeaders() {

        String key = "outputMessage";
        ResolvableMap headers =
                new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaOutputMessage message = mock(KafkaOutputMessage.class);
        KafkaProducingMessageService service = mock(KafkaProducingMessageService.class);

        when(context.getObj(key)).thenReturn(message);
        when(kafkaServiceProvider.getProducingService(message.getClass().getSimpleName()))
                .thenReturn(service);

        steps.sendMessage(key, headers);

        verify(service).sendMessage(eq(message), eq(headers));
    }

    /**
     * Should send message without headers.
     */
    @Test
    public void shouldSendMessageWithoutHeaders() {

        String key = "outputMessage";

        KafkaOutputMessage message = mock(KafkaOutputMessage.class);
        KafkaProducingMessageService service = mock(KafkaProducingMessageService.class);

        when(context.getObj(key)).thenReturn(message);
        when(kafkaServiceProvider.getProducingService(message.getClass().getSimpleName()))
                .thenReturn(service);

        steps.sendMessage(key);

        verify(service).sendMessage(eq(message));
    }

    /**
     * Should find message by values and store in context.
     */
    @Test
    public void shouldFindMessageByValuesAndStoreInContext() {

        String identifier = "NotificationEventMessage";
        String identifierValue = "value";
        String key = "notificationEvent";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaInputMessage expectedMessage = mock(KafkaInputMessage.class);

        KafkaConsumingMessageService service = mock(KafkaConsumingMessageService.class);
        when(kafkaServiceProvider.getConsumingService(identifierValue)).thenReturn(service);
        when(kafkaMessageQueryService.findMessage(service, data)).thenReturn(expectedMessage);

        steps.findMessageByValues(identifier, identifierValue, key, data);

        verify(context).set(eq(key), eq(expectedMessage));
    }

    /**
     * Should find messages by filters and store in context.
     */
    @Test
    public void shouldFindMessagesByFiltersAndStoreInContext() {

        String identifier = "ReportingLogMessage";
        String identifierValue = "value";
        String key = "reportingLogs";
        List<Triple> filters = Arrays.asList(Triple.of("sourceSystem", "==", "Отчетность"));

        List<KafkaInputMessage> expectedMessages =
                Arrays.asList(mock(KafkaInputMessage.class), mock(KafkaInputMessage.class));

        KafkaConsumingMessageService service = mock(KafkaConsumingMessageService.class);
        when(kafkaServiceProvider.getConsumingService(identifierValue)).thenReturn(service);
        when(kafkaMessageQueryService.findMessagesByFilter(service, filters)).thenReturn(expectedMessages);

        steps.findMessagesByFilters(identifier, identifierValue, key, filters);

        verify(context).set(eq(key), eq(expectedMessages));
    }

    /**
     * Should find one message by filters and store in context.
     */
    @Test
    public void shouldFindOneMessageByFiltersAndStoreInContext() {

        String identifier = "ReportingLogMessage";
        String identifierValue = "value";
        String key = "reportingLog";
        List<Triple> filters = Arrays.asList(Triple.of("sourceSystem", "==", "Отчетность"));

        List<KafkaInputMessage> expectedMessages = Arrays.asList(mock(KafkaInputMessage.class));

        KafkaConsumingMessageService service = mock(KafkaConsumingMessageService.class);
        when(kafkaServiceProvider.getConsumingService(identifierValue)).thenReturn(service);
        when(kafkaMessageQueryService.findMessagesByFilter(service, filters)).thenReturn(expectedMessages);

        steps.findMessageByFilters(identifier, identifierValue, key, filters);

        verify(context).set(eq(key), eq(expectedMessages.get(0)));
    }

    /**
     * Should get message from list by index and store in context.
     */
    @Test
    public void shouldGetMessageFromListByIndexAndStoreInContext() {

        int index = 0;
        String list = "createTaskCommandMessages";
        String messageType = "createTaskCommand";

        KafkaInputMessage expectedMessage = mock(KafkaInputMessage.class);

        when(kafkaMessageQueryService.getMessageFromList(index, list)).thenReturn(expectedMessage);

        steps.getMessageFromList(index, list, messageType);

        verify(context).set(eq(messageType), eq(expectedMessage));
    }
}
