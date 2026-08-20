package ru.autotestframework.queue_steps.helpers;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.cucumber.messages.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;

/**
 * A helper class that provides methods for working with object data
 */
@Slf4j
@Component
public class DataHelper {

    private static ObjectMapper objectMapper;

    /**
     * Instantiates a new Data helper.
     *
     * @param objectMapper the object mapper
     */
    public DataHelper(ObjectMapper objectMapper) {
        DataHelper.objectMapper = objectMapper;
    }

    /**
     * Adjusts the state of the target object based on the source object. Ignores null fields of the source object.
     *
     * @param <T>          the type parameter
     * @param sourceObject the source object from which the field values are taken
     * @param targetObject the target object where the field values are set
     * @return object with set field values
     * @throws IllegalAccessException the illegal access exception
     */
    public static <T> T setObjectFields(T sourceObject, Supplier<T> targetObject) throws IllegalAccessException {
        T result = targetObject.get();
        Field[] fields = getAccessibleFields(sourceObject);

        for (Field f : fields) {
            if (f.get(sourceObject) != null) {
                f.set(result, f.get(sourceObject));
            }
        }

        return result;
    }

    /**
     * Adjusts the state of the target object based on the map. Provides the ability to set null values
     *
     * @param <T>          the type parameter
     * @param data data map, where key: field name, value: field value
     * @param targetObject the target object where the field values are set
     * @return object with set field values
     */
    public static <T> T setObjectFieldsFromMap(Map<String, String> data, Supplier<T> targetObject) {
        T result = targetObject.get();
        return setObjectFieldsFromMap(data, result);
    }

    /**
     * Adjusts the state of the target object based on the map. Provides the ability to set null values
     *
     * @param <T>          the type parameter
     * @param data data map, where key: field name, value: field value
     * @param targetObject the target object where the field values are set
     * @return object with set field values
     */
    public static <T> T setObjectFieldsFromMap(Map<String, String> data, T targetObject) {
        Field[] fields = getAccessibleFields(targetObject);

        data.keySet().forEach(key -> {
            var value = convertStringIfContainsNull(data.get(key));
            var field = Arrays.stream(fields)
                    .filter(f -> f.getName().equals(key))
                    .findFirst()
                    .orElseThrow();

            try {
                var convertedValue = objectMapper.convertValue(value, field.getType());
                field.set(targetObject, convertedValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return targetObject;
    }

    /**
     * Returns the field value of the source object by the field name
     *
     * @param <T>          the type parameter
     * @param fieldName field name
     * @param sourceObject source object
     * @return field value
     */
    public static <T> Object getFieldValue(String fieldName, T sourceObject) {
        Field field = Arrays.stream(getAccessibleFields(sourceObject))
                .filter(i -> i.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new AutotestException(
                        "Не найдено поле '{}' для объекта {}",
                        fieldName,
                        sourceObject.getClass().getSimpleName()));
        try {
            return field.get(sourceObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the value of the source object field along the specified path
     *
     * @param <T>          the type parameter
     * @param fieldPath field path
     * @param sourceObject source object
     * @return field value
     */
    public static <T> Object getFieldValueByPath(String fieldPath, T sourceObject) {
        try {
            String str = objectMapper.writeValueAsString(sourceObject);
            return JsonPath.read(str, fieldPath);

        } catch (JsonProcessingException e) {
            throw new AutotestException("Не найдено поле {} объекта {}", e, fieldPath, sourceObject);
        } catch (PathNotFoundException e) {
            return null;
        }
    }

    /**
     * Returns the fields of the source object by field names
     *
     * @param <T>          the type parameter
     * @param fields a set with field names
     * @param sourceObject source object
     * @return map with the field name and its value
     */
    public static <T> HashMap<String, Object> getFieldsValues(Set<String> fields, T sourceObject) {
        HashMap<String, Object> result = new HashMap<>();

        fields.forEach(field -> {
            var value = getFieldValue(field, sourceObject);
            result.put(field, value);
        });

        return result;
    }

    private static <T> Field[] getAccessibleFields(T sourceObject) {
        Field[] fields;
        fields = sourceObject.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }

        return fields;
    }

    /**
     * Compares object fields with values from the map
     *
     * @param <T>    the type parameter
     * @param data ResolvableMap with field name and expected value
     * @param object the object from which the actual field values are taken
     */
    public static <T> void compareFieldValues(ResolvableMap data, T object) {
        var actual = getFieldsValues(data.keySet(), object);
        data.keySet()
                .forEach(key -> Assertions.assertEquals(
                        data.get(key),
                        String.valueOf(actual.get(key)),
                        String.format(
                                "Неверное значение %s для объекта %s",
                                key, object.getClass().getSimpleName())));
    }

    /**
     * Compares object fields with values from the map and returns boolean
     *
     * @param <T>            the type parameter
     * @param expectedValues ResolvableMap with field name and expected value
     * @param object the object from which the actual field values are taken
     * @return the boolean
     */
    public static <T> boolean isObjectFieldValuesEqualToMap(ResolvableMap expectedValues, T object) {
        var actualValues = getFieldsValues(expectedValues.keySet(), object);

        for (String key : expectedValues.keySet()) {
            if (!expectedValues.get(key).equals(String.valueOf(actualValues.get(key)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the object fields found on the specified path with the values from the map
     *
     * @param <T>    the type parameter
     * @param data ResolvableMap with field path and expected value
     * @param object the object from which the actual field values are taken
     */
    public static <T> void compareFieldValuesByPath(ResolvableMap data, T object) {
        data.keySet().forEach(key -> {
            var actual = getFieldValueByPath(key, object);
            Assertions.assertEquals(
                    data.get(key),
                    String.valueOf(actual),
                    String.format(
                            "Неверное значение %s для объекта %s",
                            key, object.getClass().getSimpleName()));
        });
    }

    /**
     * Checks whether the date falls within the specified interval
     *
     * @param dateToCheckStr date to be checked
     * @param beginDateStr interval start date
     * @param endDateStr date of the end of the interval
     * @param name name of the date being checked
     */
    public static void checkDateInInterval(String dateToCheckStr, String beginDateStr, String endDateStr, String name) {
        LocalDateTime dateToCheck = LocalDateTime.parse(dateToCheckStr);
        LocalDateTime beginDate = LocalDateTime.parse(beginDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);
        Assertions.assertTrue(
                dateToCheck.isAfter(beginDate) && dateToCheck.isBefore(endDate),
                String.format(
                        "Дата %s = %s должна попадать в интервал %s - %s", name, dateToCheck, beginDate, endDate));
    }

    /**
     * Checks that the object contains all the specified fields with the corresponding values
     *
     * @param <T>           the type parameter
     * @param currentObject current object
     * @param data table containing the paths to the fields to be checked inside the Json object and the expected values
     * @return true - if all fields are found and match the expected values, false - otherwise
     */
    public static <T> boolean isAllFieldsEqualToValue(Object currentObject, Map<String, T> data) {
        for (Map.Entry<String, T> entry : data.entrySet()) {
            if (!isObjectFieldEqualToValue(currentObject, entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that the object contains all the specified fields with the corresponding values or containing the specified value
     *
     * @param currentObject current object
     * @param data is a three-column table containing the paths to the fields to be checked inside the Json object and the expected values
     * @return true - if all fields are found and match the expected values, false - otherwise
     */
    public static boolean isAllFieldsMatchToValue(Object currentObject, List<Triple> data) {
        for (Triple triple : data) {
            if (triple.getSecond().trim().equalsIgnoreCase("содержит")) {
                String actual = Objects.requireNonNull(getFieldValueByPath(triple.getFirst(), currentObject))
                        .toString();
                log.info("Найденное сообщение: {}", actual);
                if (!actual.contains(triple.getThird())) {
                    return false;
                }
            }
            if (triple.getSecond().trim().equalsIgnoreCase("==")) {
                if (!isObjectFieldEqualToValue(currentObject, triple.getFirst(), triple.getThird())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether the field value matches the expected value
     *
     * @param <T>           the type parameter
     * @param currentObject the current object in which we will search for the specified field
     * @param fieldPath is the path to the field in the Json object
     * @param fieldValue expected value
     * @return true - if the value matches the expected value, false - otherwise
     */
    public static <T> boolean isObjectFieldEqualToValue(Object currentObject, String fieldPath, T fieldValue) {
        var actualValue = getFieldValueByPath(fieldPath, currentObject);
        fieldValue = convertStringIfContainsNull(fieldValue);
        if (actualValue != null) {
            var expectedValue = objectMapper.convertValue(fieldValue, actualValue.getClass());
            log.info(String.format("Актуальное значение: %s Ожидаемое значение: %s", actualValue, expectedValue));

            return actualValue.equals(expectedValue);
        } else {
            return fieldValue == null;
        }
    }

    /**
     * Checks the occurrence of a string in a record found on the specified path
     *
     * @param <T>    the type parameter
     * @param data expected lines
     * @param path is the path to the field in the Json object
     * @param object the object from which the actual field values are taken
     */
    public static <T> void checkStringContainsValue(ResolvableList data, String path, T object) {
        String actual =
                Objects.requireNonNull(getFieldValueByPath(path, object)).toString();
        data.forEach(str -> Assertions.assertTrue(
                actual.contains(str), String.format("Не найдено вхождение строки %s для объекта %s", str, path)));
    }

    /**
     * Convert string if contains null t.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return the t
     */
    public static <T> T convertStringIfContainsNull(T object) {
        return object.equals("null") ? null : object;
    }
}
