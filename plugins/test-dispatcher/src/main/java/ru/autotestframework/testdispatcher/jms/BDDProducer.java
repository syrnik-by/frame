package ru.autotestframework.testdispatcher.jms;

import javax.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

@Slf4j
public class BDDProducer implements AutoCloseable {

    private final ActiveMQConnectionFactory connectionFactory;
    private final JMSContext context;
    private final JMSProducer producer;
    private final Queue queue;

    public BDDProducer(String brokerUrl, String clientId, String queueName) {
        this.connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        this.context = connectionFactory.createContext();
        this.context.setClientID(clientId);
        this.producer = context.createProducer();
        this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        this.queue = context.createQueue(queueName);
        log.info("producer creation");
    }

    public void sendFeature(String feature, String runId) {
        MapMessage message = context.createMapMessage();
        try {
            message.setString("Feature", feature);
            message.setString("RunID", runId);
        } catch (JMSException e) {
            log.error(e.getMessage());
        }
        producer.send(queue, message);
    }

    public void sendBytes(byte[] bytes) {
        BytesMessage message = context.createBytesMessage();
        try {
            message.writeBytes(bytes);
        } catch (JMSException e) {
            log.error(e.getMessage());
        }
        producer.send(queue, message);
    }

    @Override
    public void close() {
        context.close();
        connectionFactory.close();
    }
}
