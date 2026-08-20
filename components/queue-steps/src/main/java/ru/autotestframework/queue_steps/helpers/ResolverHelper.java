package ru.autotestframework.queue_steps.helpers;

import io.cucumber.messages.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.messages.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;

/**
 * A class for working with the context of test scenarios and converting data from feature files.
 * <p>
 * Provides the following functions:
 * <ul>
 *   <li>Automatic substitution of values from the test execution context</li>
 *   <li>Parsing JSON structures by prefixes list: and map: in string values</li>
 *   <li>Complex processing of scenario parameters with cascading transformations</li>
 * </ul>
 *
 * <p><b>Support for prefixes in feature files:</b>
 * <ul>
 *   <li><b>list:</b> - converts a subsequent JSON string into a list of objects.
 * Example: "list:[1, 2, 3]" → List&lt;Integer&gt; [1, 2, 3]
 * Example: "list:["person"] where person is the key to the object in the context of the bank</li>
 *   <li><b>map:</b> - converts a subsequent JSON string into an associative array.
 * Example: "map:{\"name\":\"John\"}" → Map&lt;String, String&gt; {name=John}</li>
 * </ul>
 *
 * <p><b>Substitution of values from the context:</b><br>
 * For string values that do not contain special prefixes, a search is performed
 * the corresponding key in the context of the test. If the value is found, it is replaced,
 * otherwise, the original string is returned.
 * Example: "user.login" → value from context.getObj("user.login")
 */
@Component
@RequiredArgsConstructor
public class ResolverHelper {
    private static final String LIST_PREFIX = "list:";
    private static final String MAP_PREFIX = "map:";

    private final Context context;
    private final ObjectMapper objectMapper;

    /**
     * Complex ResolvableList processing:
     * <ol>
     *   <li>Converting values with the prefix list: to lists</li>
     *   <li>Converting values with the prefix map: to maps</li>
     *   <li>Substitution of the remaining string values from the context</li>
     * </ol>
     *
     * <ul>
     *    <li>Example: "list:[1, 2, 3]" → List&lt;Integer&gt; [1, 2, 3]</li>
     *    <li>Example: "list:["person1", "person2"] where person1 and person2 are the keys to the objects in the context of the bank</li>
     * </ul>
     *
     * @param sourceList the original list of values from the feature file
     * @return is a fully processed list with converted values. Returns an empty list if SourceList == null or empty
     */
    public List<Object> resolve(ResolvableList sourceList) {
        return processCollection(sourceList, this::processElement);
    }

    /**
     * Complex ResolvableMap processing:
     * <ol>
     *   <li>Converting values with the prefix list: to lists</li>
     *   <li>Converting values with the prefix map: to maps</li>
     *   <li>Substitution of the remaining string values from the context</li>
     * </ol>
     *
     * @param sourceMap source map of values from the feature file
     * @return a fully processed map with transformed values. Returns an empty map if sourceMap == null or empty
     */
    public Map<String, Object> resolve(ResolvableMap sourceMap) {
        return processMap(sourceMap, this::processElement);
    }

    private <T> List<Object> processCollection(T source, Function<String, Object> processor) {
        return Optional.ofNullable(source)
                .filter(this::isNotEmpty)
                .map(this::convertToList)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(String::valueOf)
                .map(processor)
                .collect(Collectors.toList());
    }

    private <T> Map<String, Object> processMap(T source, Function<String, Object> processor) {
        Map<String, String> convertedMap = Optional.ofNullable(source)
                .filter(this::isNotEmpty)
                .map(this::convertToMap)
                .orElseGet(Collections::emptyMap);

        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : convertedMap.entrySet()) {
            String value = entry.getValue();
            Object processedValue = processor.apply(value);
            result.put(entry.getKey(), processedValue);
        }

        return result;
    }

    private Object processElement(String value) {
        return Optional.ofNullable(value)
                .map(this::parseList)
                .map(this::tryParseMap)
                .map(this::resolveFromContext)
                .orElse(value);
    }

    private Object getValueOrOriginal(String key) {
        if (StringUtils.isBlank(key)) {
            return key;
        } else return Optional.of(key).map(context::getObj).orElse(key);
    }

    private Object parseWithPrefix(String value, String prefix, TypeReference<?> type) {
        if (value != null && value.startsWith(prefix)) {
            String json = value.substring(prefix.length());
            try {
                json = resolveFromContext(json);
                return objectMapper.readValue(json, type);
            } catch (JsonProcessingException e) {
                throw new AutotestException("Ошибка парсинга JSON для префикса '" + prefix + "': " + e.getMessage(), e);
            }
        }
        return value;
    }

    private Object resolveFromContext(Object obj) {
        return obj instanceof String ? getValueOrOriginal((String) obj) : obj;
    }

    private String resolveFromContext(String json) throws JsonProcessingException {
        Object data = objectMapper.readValue(json, Object.class);
        Object replacedData = replaceValues(data);
        return objectMapper.writeValueAsString(replacedData);
    }

    private Object replaceValues(Object data) {
        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            Map<String, Object> newMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                newMap.put(entry.getKey().toString(), replaceValues(entry.getValue()));
            }
            return newMap;
        } else if (data instanceof List) {
            List<?> list = (List<?>) data;
            List<Object> newList = new ArrayList<>();
            for (Object item : list) {
                newList.add(replaceValues(item));
            }
            return newList;
        } else if (data instanceof String) {
            return getValueOrOriginal((String) data);
        } else {
            return data;
        }
    }

    private Object parseList(String value) {
        return parseWithPrefix(value, LIST_PREFIX, new TypeReference<List<Object>>() {});
    }

    private Object tryParseMap(Object obj) {
        return obj instanceof String ? parseMap((String) obj) : obj;
    }

    private Object parseMap(String value) {
        return parseWithPrefix(value, MAP_PREFIX, new TypeReference<Map<String, Object>>() {});
    }

    private boolean isNotEmpty(Object collection) {
        if (collection instanceof ResolvableList) return !((ResolvableList) collection).isEmpty();
        if (collection instanceof ResolvableMap) return !((ResolvableMap) collection).isEmpty();
        return false;
    }

    private List<String> convertToList(Object source) {
        return (source instanceof ResolvableList) ? (ResolvableList) source : Collections.emptyList();
    }

    private Map<String, String> convertToMap(Object source) {
        return (source instanceof ResolvableMap) ? (ResolvableMap) source : Collections.emptyMap();
    }
}
