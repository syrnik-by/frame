package ru.autotestframework.queue_steps.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.autotestframework.queue_steps.clients.QueueClient;
import ru.autotestframework.queue_steps.clients.RabbitMqClient;
import ru.autotestframework.queue_steps.configuration.rabbit_mq.RabbitMqProperties;

/**
 * The type Queue configuration.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueueConfiguration {

    private final RabbitMqProperties rabbitMqProperties;

    /**
     * Clients list.
     *
     * @return the list
     */
    @Bean
    public List<QueueClient> clients() {
        return List.of(new RabbitMqClient(rabbitMqProperties));
    }
}
