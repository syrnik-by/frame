package ru.autotestframework.queue_steps;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.text.StringSubstitutor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.Before;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.queue_steps.clients.kafka.service.*;

/**
 * The type Kafka record steps test.
 */
@Tag("@QueueDemo")
@ExtendWith(MockitoExtension.class)
public class KafkaRecordStepsTest {

    @Mock
    private KafkaServiceProvider kafkaServiceProvider;

    @Mock
    private Context context;

    @Mock
    private KafkaRecordConsumingService recordService;

    @Mock
    private ConsumerRecord<String, KafkaInputMessage> record;

    @Mock
    private Headers headers;

    @InjectMocks
    private KafkaRecordSteps steps;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        steps = new KafkaRecordSteps(kafkaServiceProvider, context);
    }

    /**
     * Should find record by values and store in context.
     */
    @Test
    public void shouldFindRecordByValuesAndStoreInContext() {
        String identifier = "recordIdentifier";
        String identifierValue = "value";
        String key = "recordKey";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));
        Header header = new RecordHeader("headerKey", "headerValue".getBytes());

        KafkaInputMessage inputMessage = mock(KafkaInputMessage.class);
        ConsumerRecord<String, KafkaInputMessage> record = new ConsumerRecord<>("topic", 0, 0L, "key", inputMessage);
        record.headers().add(header);

        when(kafkaServiceProvider.getConsumingService(identifierValue)).thenReturn(recordService);
        when(recordService.getRecordByValues(data)).thenReturn(record);

        steps.findRecordByValues(identifier, identifierValue, key, data);

        verify(context).set(eq(key), eq(record));
    }

    /**
     * Should find records by values and store in context.
     */
    @Test
    public void shouldFindRecordsByValuesAndStoreInContext() {
        String identifier = "recordsIdentifier";
        String identifierValue = "value";
        String key = "recordsKey";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        List<ConsumerRecord<String, KafkaInputMessage>> records =
                Arrays.asList(mock(ConsumerRecord.class), mock(ConsumerRecord.class));

        when(kafkaServiceProvider.getConsumingService(identifierValue)).thenReturn(recordService);
        when(recordService.getRecordsByValues(data)).thenReturn(records);

        steps.findRecordsByValues(identifier, identifierValue, key, data);

        verify(context).set(eq(key), eq(records));
    }

    /**
     * Should compare record message values correctly.
     */
    @Test
    public void shouldCompareRecordMessageValuesCorrectly() {
        String key = "recordKey";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        KafkaInputMessage inputMessage = mock(KafkaInputMessage.class);
        ConsumerRecord<String, KafkaInputMessage> record = new ConsumerRecord<>("topic", 0, 0L, "key", inputMessage);

        when(context.getObj(key)).thenReturn(record);

        steps.compareRecordMessageValues(key, data);
    }

    /**
     * Should check record headers contain expected values.
     */
    @Test
    public void shouldCheckRecordHeadersContainExpectedValues() {
        String key = "recordKey";
        ResolvableMap data = new ResolvableMap(new HashMap<>(), new PlaceholderResolverImpl(new StringSubstitutor()));

        Header header = new RecordHeader("headerKey", "headerValue".getBytes());

        KafkaInputMessage inputMessage = mock(KafkaInputMessage.class);
        ConsumerRecord<String, KafkaInputMessage> record = new ConsumerRecord<>("topic", 0, 0L, "key", inputMessage);
        record.headers().add(header);

        when(context.getObj(key)).thenReturn(record);

        steps.containRecordHeaderValues(key, data);
    }
}
