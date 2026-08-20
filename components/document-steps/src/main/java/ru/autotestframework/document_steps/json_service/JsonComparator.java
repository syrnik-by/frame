package ru.autotestframework.document_steps.json_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Class - fork of com.flipkart.zjsonpatch:zjsonpatch:0.4.4
 * with extended compare logic
 */
public class JsonComparator {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Compares two jsons
     * @param json1 first json
     * @param json2 second json
     * @return difference as node
     * @throws IOException
     */
    public static JsonNode compare(String json1, String json2) throws IOException {
        return JsonDiff.asJson(normalize(mapper.readTree(json1)), normalize(mapper.readTree(json2)));
    }

    private static JsonNode normalize(JsonNode node) {
        if (node.isObject()) {
            ObjectNode sorted = mapper.createObjectNode();
            node.fieldNames().forEachRemaining(field -> sorted.set(field, normalize(node.get(field))));
            return sortObjectNode(sorted);
        } else if (node.isArray()) {
            ArrayNode arrayNode = mapper.createArrayNode();
            for (JsonNode element : node) {
                arrayNode.add(normalize(element));
            }
            return sortArrayNode(arrayNode);
        }
        return node;
    }

    private static ObjectNode sortObjectNode(ObjectNode node) {
        TreeMap<String, JsonNode> map = new TreeMap<>();
        node.fields().forEachRemaining(field -> map.put(field.getKey(), field.getValue()));
        ObjectNode sorted = mapper.createObjectNode();
        map.forEach(sorted::set);
        return sorted;
    }

    private static ArrayNode sortArrayNode(ArrayNode arrayNode) {
        List<JsonNode> elements = new ArrayList<>();
        arrayNode.forEach(elements::add);
        elements.sort(Comparator.comparing(JsonNode::toString));
        ArrayNode sorted = mapper.createArrayNode();
        elements.forEach(sorted::add);
        return sorted;
    }
}
