package ru.autotestframework.testdispatcher.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.jms.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

@Slf4j
public class BDDConsumer implements AutoCloseable {

    private final ActiveMQConnectionFactory connectionFactory;
    private final JMSContext context;
    private final JMSConsumer consumer;
    private final QueueBrowser queueBrowser;

    @SneakyThrows
    public BDDConsumer(String brokerUrl, String clientId, String queueName, String messageSelector) {
        this.connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        this.context = connectionFactory.createContext();
        this.context.setClientID(clientId);
        log.info("consumer creation");
        Queue queue = context.createQueue(queueName);

        this.consumer = context.createConsumer(queue, messageSelector);
        this.queueBrowser = context.createBrowser(queue);
    }

    @Override
    public void close() throws Exception {
        consumer.close();
        context.close();
        connectionFactory.close();
    }

    public byte[] receiveBytes(long timeout) {
        Message message = consumer.receive(timeout);

        log.info("receive bytes");
        byte[] bytes = new byte[0];
        if (message != null) {
            BytesMessage bytesMessage = (BytesMessage) message;
            try {
                bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
            } catch (JMSException e) {
                log.error("jmse interr", e);
            }
        }
        return bytes;
    }

    public Map<String, String> receiveMap(long timeout) {
        log.info("В очереди было " + getQueueSize() + " сообщений");

        Message message = consumer.receive(timeout);

        // todo срусл цшер 2.0 JMS and then delete excessive
        // String s = consumer.receiveBody(String.class);

        // log.info(s);

        log.info("В очереди осталось " + getQueueSize() + " сообщений");

        if (message != null) {
            MapMessage mapMessage = (MapMessage) message;
            return mapMessageToMap(mapMessage);
        }
        return new HashMap<>();
    }

    @SneakyThrows
    private int getQueueSize() {
        Enumeration enumeration = queueBrowser.getEnumeration();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
            i++;
        }
        return i;
    }

    private Map<String, String> mapMessageToMap(MapMessage mapMessage) {
        Map<String, String> map = new HashMap<>();
        try {
            Enumeration en = mapMessage.getMapNames();
            while (en.hasMoreElements()) {
                String property = (String) en.nextElement();
                String mapObject = mapMessage.getString(property);
                map.put(property, mapObject);
            }
        } catch (JMSException e) {
            log.error("jmse interr", e);
        }
        log.info(map.toString());
        return map;
    }
}
