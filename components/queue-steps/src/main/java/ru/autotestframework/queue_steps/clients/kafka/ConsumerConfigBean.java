package ru.autotestframework.queue_steps.clients.kafka;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Consumer config bean.
 */
@Getter
@Setter
@AllArgsConstructor
public class ConsumerConfigBean {
    private Map<String, Object> consumerProperties;
}
