package ru.autotestframework.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * Thread scope implementation.
 *
 * @author David Winterfeldt
 */
@Slf4j
public class ThreadScope implements Scope {

    public Object get(String name, ObjectFactory<?> objectFactory) {
        var context = SpringTestContext.getInstance();
        Object obj = context.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
            context.put(name, obj);
        }
        return obj;
    }

    public Object remove(String name) {
        var context = SpringTestContext.getInstance();
        return context.remove(name);
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        var context = SpringTestContext.getInstance();
        context.registerDestructionCallback(name, callback);
    }

    public Object resolveContextualObject(String key) {
        return null;
    }

    public String getConversationId() {
        var context = SpringTestContext.getInstance();
        return context.getId();
    }
}
