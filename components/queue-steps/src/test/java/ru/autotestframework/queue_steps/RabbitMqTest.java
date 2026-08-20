package ru.autotestframework.queue_steps;

import static org.mockito.Mockito.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.queue_steps.clients.RabbitMqClient;
import ru.autotestframework.queue_steps.configuration.rabbit_mq.RabbitMqProperties;
import ru.autotestframework.util.generator.FakerRU;

/**
 * The type Rabbit mq test.
 */
@Tag("@QueueDemo")
class RabbitMqTest {

    private RabbitMqClient client;
    private Connection connection;
    private Channel channel;
    private String queue;

    /**
     * The Message.
     */
    String message = FakerRU.instance().programmingLanguage().name();

    /**
     * Sets .
     *
     * @throws Exception the exception
     */
    @BeforeEach
    public void setup() throws Exception {
        queue = "test";
        Map<String, String> properties = Map.of("host", "localhost", "queue", queue);

        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        connection = mock(Connection.class);
        channel = mock(Channel.class);

        client = new RabbitMqClient(new RabbitMqProperties());
        client.init(properties);
        client.setConnectionFactory(connectionFactory);
        Mockito.lenient().when(connectionFactory.load(properties, null)).thenReturn(connectionFactory);
        Mockito.lenient().when(connectionFactory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        when(channel.isOpen()).thenReturn(true);
    }

    /**
     * Send test.
     *
     * @throws IOException      the io exception
     * @throws TimeoutException the timeout exception
     */
    @Test
    void sendTest() throws IOException, TimeoutException {
        client.sendMessage(message);
        verify(channel).basicPublish("", queue, null, message.getBytes());
        Mockito.verify(channel, Mockito.times(1)).close();
        Mockito.verify(channel, Mockito.times(1)).basicPublish("", queue, null, message.getBytes());
    }

    /**
     * Send message test.
     *
     * @throws IOException      the io exception
     * @throws TimeoutException the timeout exception
     */
    @Test
    void sendMessageTest() throws IOException, TimeoutException {
        client.sendMessage(message);
        verify(channel).basicPublish("", queue, null, message.getBytes());
        Mockito.verify(channel, Mockito.times(1)).close();
        Mockito.verify(channel, Mockito.times(1)).basicPublish("", queue, null, message.getBytes());
    }

    /**
     * Send message with io exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void sendMessageWithIOExceptionTest() throws IOException {
        client.sendMessage(message);
        doThrow(new IOException()).when(channel).basicPublish("", queue, null, message.getBytes());
        Assertions.assertThrows(AutotestException.class, () -> client.sendMessage(message));
    }

    /**
     * Create channel with io exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void createChannelWithIOExceptionTest() throws IOException {
        client.sendMessage(message);
        doThrow(new IOException()).when(connection).createChannel();
        Assertions.assertThrows(AutotestException.class, () -> client.sendMessage(message));
    }

    /**
     * Create channel with timeout exception test.
     *
     * @throws IOException the io exception
     */
    @Test
    void createChannelWithTimeoutExceptionTest() throws IOException {
        client.sendMessage(message);
        doAnswer(invocation -> {
                    throw new TimeoutException();
                })
                .when(connection)
                .createChannel();
        Assertions.assertThrows(AutotestException.class, () -> client.sendMessage(message));
    }

    /**
     * Close channel with io exception test.
     *
     * @throws IOException      the io exception
     * @throws TimeoutException the timeout exception
     */
    @Test
    void closeChannelWithIOExceptionTest() throws IOException, TimeoutException {
        client.sendMessage(message);
        doThrow(new IOException()).when(channel).close();
        Assertions.assertThrows(AutotestException.class, () -> client.sendMessage(message));
    }

    /**
     * Close channel with timeout exception test.
     *
     * @throws IOException      the io exception
     * @throws TimeoutException the timeout exception
     */
    @Test
    void closeChannelWithTimeoutExceptionTest() throws IOException, TimeoutException {
        client.sendMessage(message);
        doThrow(new TimeoutException()).when(channel).close();
        Assertions.assertThrows(AutotestException.class, () -> client.sendMessage(message));
    }
}
