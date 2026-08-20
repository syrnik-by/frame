package ru.autotestframework.queue_steps.helpers;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type Json helper.
 */
@Component
@Slf4j
public class JsonHelper {

    /**
     * The constant PATH_REGEX_SEPARATOR.
     */
    public static final String PATH_REGEX_SEPARATOR = "\\.";

    /**
     * Converts a map with keys as paths to a JSON-like map.
     * <p>For example, the input card is:
     * <pre>
     * pathMap = { "a.b.c": "123" }
     * </pre>
     * <p>Will be converted to:
     * <pre>
     * { "a": { "b": { "c": "123" } } }
     * </pre>
     *
     * @param pathMap is a map where keys are represented as paths through a point.
     * @param clazz class, which is described by the received map.
     * @return Map {@code <String, Object>} corresponding to the JSON structure.
     */
    public Map<String, Object> resolvePathMap(Map<String, Object> pathMap, Class<?> clazz) {
        Map<String, Object> jsonMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : pathMap.entrySet()) {
            String[] keys = entry.getKey().split(PATH_REGEX_SEPARATOR);
            Map<String, Object> currentMap = jsonMap;
            for (int i = 0; i < keys.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.computeIfAbsent(keys[i], k -> new HashMap<>());
            }
            currentMap.put(keys[keys.length - 1], entry.getValue());
        }
        return jsonMap;
    }
}
