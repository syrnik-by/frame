package ru.autotestframework.core.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread safe-processing of Spring Scope
 */
@Slf4j
public class SpringTestContext {

    private static final ThreadLocal<SpringTestContext> localContext = ThreadLocal.withInitial(SpringTestContext::new);
    private static final AtomicInteger sessionCounter = new AtomicInteger(0);
    private final Map<String, Object> objects = new HashMap();
    private final Map<String, Runnable> callbacks = new HashMap();
    private Integer sessionId;

    private SpringTestContext() {}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    static SpringTestContext getInstance() {
        return (SpringTestContext) localContext.get();
    }

    /**
     * Start.
     */
    void start() {
        if (this.sessionId != null) {
            this.sessionId = sessionCounter.incrementAndGet();
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    String getId() {
        return "test_context_" + this.sessionId;
    }

    /**
     * Stop.
     */
    void stop() {
        var var1 = this.callbacks.values().iterator();

        while (var1.hasNext()) {
            Runnable callback = (Runnable) var1.next();
            callback.run();
        }

        localContext.remove();
        this.sessionId = null;
    }

    /**
     * Get object.
     *
     * @param name the name
     * @return the object
     */
    Object get(String name) {
        this.requireActiveScenario();
        return this.objects.get(name);
    }

    /**
     * Put.
     *
     * @param name   the name
     * @param object the object
     */
    void put(String name, Object object) {
        this.requireActiveScenario();
        this.objects.put(name, object);
    }

    /**
     * Remove object.
     *
     * @param name the name
     * @return the object
     */
    Object remove(String name) {
        this.requireActiveScenario();
        this.callbacks.remove(name);
        return this.objects.remove(name);
    }

    /**
     * Register destruction callback.
     *
     * @param name     the name
     * @param callback the callback
     */
    void registerDestructionCallback(String name, Runnable callback) {
        this.requireActiveScenario();
        this.callbacks.put(name, callback);
    }

    /**
     * Require active scenario.
     */
    void requireActiveScenario() {
        if (this.sessionId == null) {
            this.sessionId = sessionCounter.get();
            log.debug("Test context scoped beans can only be created while Test is running");
        }
    }
}
