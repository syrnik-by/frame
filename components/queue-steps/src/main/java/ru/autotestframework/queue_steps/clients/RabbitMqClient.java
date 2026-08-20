package ru.autotestframework.queue_steps.clients;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.queue_steps.configuration.rabbit_mq.RabbitMqProperties;

/**
 * The type Rabbit mq client.
 */
@Slf4j
@Data
public class RabbitMqClient implements QueueClient {
    private final RabbitMqProperties rabbitMqProperties;
    private Channel channel;
    private Connection connection;
    private String queue;
    private ConnectionFactory connectionFactory;

    /**
     * Instantiates a new Rabbit mq client.
     *
     * @param rabbitMqProperties the rabbit mq properties
     */
    public RabbitMqClient(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Override
    public String getName() {
        return "RABBITMQ";
    }

    @Override
    public QueueClient init(Map<String, String> properties) {
        this.queue = properties.get("queue") == null ? rabbitMqProperties.getQueueName() : properties.get("queue");
        this.connectionFactory = new ConnectionFactory().load(setProps(properties), null);
        return this;
    }

    @Override
    public void sendMessage(String message) {
        connection();
        try {
            channel.basicPublish("", queue, null, message.getBytes());
        } catch (IOException e) {
            throw new AutotestException("Ошибка отправки сообщения к RabbitMq: \n {}", e);
        }
        log.info("Сообщение отправлено: " + message);
        closeChannel();
        closeConnection();
    }

    @Override
    public void findMessage(String searchMessage) {
        connection();
        try {
            GetResponse response = channel.basicGet(queue, false);
            while (response != null) {
                String message = new String(response.getBody());
                if (message.contains(searchMessage)) {
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                    log.info("Сообщение найдено в RabbitMq: " + searchMessage);
                    closeChannel();
                    closeConnection();
                }
                response = channel.basicGet(queue, false);
            }
        } catch (IOException ioe) {
            throw new AutotestException("Ошибка получения сообщений RabbitMq: \n {}", ioe);
        }
        throw new AutotestException("Сообщение не найдено RabbitMq: " + searchMessage);
    }

    private Map<String, String> setProps(Map<String, String> properties) {
        Map<String, String> mutableMap = new HashMap<>(properties);
        mutableMap.computeIfAbsent(ConnectionFactoryConfigurator.HOST, k -> rabbitMqProperties.getHost());
        mutableMap.computeIfAbsent(ConnectionFactoryConfigurator.PORT, k -> rabbitMqProperties.getPort());
        mutableMap.computeIfAbsent(ConnectionFactoryConfigurator.USERNAME, k -> rabbitMqProperties.getUsername());
        mutableMap.computeIfAbsent(ConnectionFactoryConfigurator.PASSWORD, k -> rabbitMqProperties.getPassword());
        return mutableMap;
    }

    private void connection() {
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            throw new AutotestException("Ошибка подключения к RabbitMq: \n {}", e);
        }
    }

    private void closeChannel() {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                throw new ExecutionException("Ошибка закрытия канала с RabbitMQ: \n {}", e);
            }
        }
    }

    private void closeConnection() {
        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (IOException e) {
                throw new ExecutionException("Ошибка закрытия соединения с RabbitMQ: \n {}", e);
            }
        }
    }
}
