package ru.autotestframework.queue_steps.clients;

import java.util.Map;

/**
 * The interface Queue client.
 */
public interface QueueClient {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Init queue client.
     *
     * @param properties the properties
     * @return the queue client
     */
    QueueClient init(Map<String, String> properties);

    /**
     * Send message.
     *
     * @param message the message
     */
    void sendMessage(final String message);

    /**
     * Find message.
     *
     * @param message the message
     */
    void findMessage(final String message);
}
