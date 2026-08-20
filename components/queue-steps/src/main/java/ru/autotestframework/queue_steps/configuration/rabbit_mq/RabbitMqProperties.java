package ru.autotestframework.queue_steps.configuration.rabbit_mq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Rabbit mq properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.queues.rabbitmq")
@PropertySource(value = "classpath:rabbitmq.properties", ignoreResourceNotFound = true)
public class RabbitMqProperties {

    @Value("${framework.queues.rabbitmq.host:}")
    private String host;

    @Value("${framework.queues.rabbitmq.port:}")
    private String port;

    @Value("${framework.queues.rabbitmq.username:}")
    private String username;

    @Value("${framework.queues.rabbitmq.password:}")
    private String password;

    @Value("${framework.queues.rabbitmq.queue.name:}")
    private String queueName;
}
