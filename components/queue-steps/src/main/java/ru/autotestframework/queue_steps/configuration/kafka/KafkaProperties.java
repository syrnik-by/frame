package ru.autotestframework.queue_steps.configuration.kafka;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Kafka properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework")
@PropertySource(value = "classpath:kafka.properties", ignoreResourceNotFound = true)
public class KafkaProperties {

    @Value("${spring.kafka.bootstrap-servers:}")
    private List<String> servers;

    @Value("${spring.kafka.consumer.group-id:}")
    private String groupId;

    @Value("${spring.kafka.consumer.key-deserializer:}")
    private Class<?> keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer:}")
    private Class<?> valueDeserializer;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest:}")
    private String autoOffsetReset;

    @Value("${spring.kafka.listener.poll-timeout:}")
    private Long pollTimeout;

    @Value("${spring.kafka.producer.key-serializer:}")
    private Class<?> keySerializer;

    @Value("${spring.kafka.producer.value-serializer:}")
    private Class<?> valueSerializer;

    @Value("${spring.kafka.security.protocol:#{null}}")
    private String securityProtocol;

    @Value("${spring.kafka.ssl.key-store-type:#{null}}")
    private String keyStoreType;

    @Value("${spring.kafka.ssl.key-store-location:#{null}}")
    private String keyStoreLocation;

    @Value("${spring.kafka.ssl.key-store-password:#{null}}")
    private String keyStorePassword;

    @Value("${spring.kafka.ssl.trust-store-type:#{null}}")
    private String trustStoreType;

    @Value("${spring.kafka.ssl.trust-store-location:#{null}}")
    private String trustStoreLocation;

    @Value("${spring.kafka.ssl.trust-store-password:#{null}}")
    private String trustStorePassword;
}
