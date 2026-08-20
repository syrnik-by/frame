package ru.autotestframework.queue_steps.configuration.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.autotestframework.queue_steps.clients.kafka.ConsumerConfigBean;

/**
 * The type Kafka config.
 */
@Slf4j
@Configuration
@EnableKafka
@AllArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    /**
     * Creates a KafkaListener factory for working with objects.
     *
     * @return the kafka listener container factory
     */
    @Bean
    @Scope("prototype")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>>
            kafkaListenerContainerFactory() {
        return createListenerFactory(objectConsumerFactory());
    }

    /**
     * Creates a KafkaListener factory for working with strings. It is used for a universal consumer.
     *
     * @return the concurrent kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory() {
        return createListenerFactory(stringConsumerFactory());
    }

    private <K, V> ConcurrentKafkaListenerContainerFactory<K, V> createListenerFactory(
            ConsumerFactory<K, V> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(kafkaProperties.getPollTimeout());
        return factory;
    }

    /**
     * Configuration of the ConsumerFactory for objects.
     *
     * @return the consumer factory
     */
    @Bean
    @Scope("prototype")
    public ConsumerFactory<String, Object> objectConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(buildConsumerProps(false));
    }

    /**
     * Configuration of the ConsumerFactory for strings.
     *
     * @return the consumer factory
     */
    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(buildConsumerProps(true));
    }

    private Map<String, Object> buildConsumerProps(boolean isStringConsumer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetReset());
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                isStringConsumer ? StringDeserializer.class : kafkaProperties.getKeyDeserializer());
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                isStringConsumer ? StringDeserializer.class : kafkaProperties.getValueDeserializer());
        props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, false);
        props.putAll(buildSecurityProps());
        return props;
    }

    /**
     * Configuration of the ProducerFactory.
     *
     * @return the consumer config bean
     */
    @Bean
    public ConsumerConfigBean getConsumerConfigBean() {
        return new ConsumerConfigBean(buildConsumerProps(false));
    }

    /**
     * Producer factory producer factory.
     *
     * @return the producer factory
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(buildProducerProps());
    }

    private Map<String, Object> buildProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.putAll(buildSecurityProps());
        return props;
    }

    /**
     * Creates KafkaTemplate.
     *
     * @param producerFactory the producer factory
     * @return the kafka template
     */
    @Bean
    @Scope("prototype")
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Security Settings.
     */
    private Map<String, Object> buildSecurityProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getSecurityProtocol());
        props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, kafkaProperties.getKeyStoreType());
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaProperties.getKeyStoreLocation());
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaProperties.getKeyStorePassword());
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, kafkaProperties.getTrustStoreType());
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaProperties.getTrustStoreLocation());
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaProperties.getTrustStorePassword());
        return props;
    }
}
