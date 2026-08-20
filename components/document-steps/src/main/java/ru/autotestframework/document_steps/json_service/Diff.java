package ru.autotestframework.document_steps.json_service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Getter;

class Diff {
    @Getter
    private final Operation operation;

    @Getter
    private final List<Object> path;

    @Getter
    private final JsonNode value;

    @Getter
    private List<Object> toPath; // only to be used in move operation

    @Getter
    private final JsonNode srcValue; // only used in replace operation

    Diff(Operation operation, List<Object> path, JsonNode value) {
        this.operation = operation;
        this.path = path;
        this.value = value;
        this.srcValue = null;
    }

    Diff(Operation operation, List<Object> fromPath, List<Object> toPath) {
        this.operation = operation;
        this.path = fromPath;
        this.toPath = toPath;
        this.value = null;
        this.srcValue = null;
    }

    Diff(Operation operation, List<Object> path, JsonNode srcValue, JsonNode value) {
        this.operation = operation;
        this.path = path;
        this.value = value;
        this.srcValue = srcValue;
    }

    /**
     * generates diff
     * @param replace operation
     * @param path path
     * @param target jsonNode as target
     * @return
     */
    public static Diff generateDiff(Operation replace, List<Object> path, JsonNode target) {
        return new Diff(replace, path, target);
    }

    /**
     * generates diff
     * @param replace operation
     * @param path path
     * @param source jsonNode as source
     * @param target jsonNode as target
     * @return
     */
    public static Diff generateDiff(Operation replace, List<Object> path, JsonNode source, JsonNode target) {
        return new Diff(replace, path, source, target);
    }
}
