package ru.autotestframework.camunda_steps.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import ru.autotestframework.core.exception.ExecutionException;

@Slf4j
public class CamundaTask {
    private final Map<String, String> taskMap;

    public CamundaTask(final Map<String, String> taskMap) {
        this.taskMap = taskMap;
    }

    /**
     * returns task map
     * @return
     */
    public Map<String, String> getTaskMap() {
        return taskMap;
    }

    /**
     * returns business key
     * @return
     */
    public String getBusinessKey() {
        return taskMap.get("businessKey");
    }

    /**
     * Validate a task against given parameters and its values.
     * @param param expected params to validate Task
     */
    public void checkParameters(final Map<String, String> param) {
        checkParameters(new ArrayList<>(param.keySet()));
        final List<Triple<String, String, String>> wrongParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (!taskMap.get(entry.getKey()).equals(entry.getValue())) {
                wrongParams.add(ImmutableTriple.of(entry.getKey(), entry.getValue(), taskMap.get(entry.getKey())));
            }
            log.info("Found matching parameter: {} ", entry.getKey());
        }
        if (!wrongParams.isEmpty()) {
            final String message = wrongParams.stream()
                    .map(t -> String.format(
                            "For parametr '%s' actual value is '%s' but expected is '%s'",
                            t.getLeft(), t.getMiddle(), t.getRight()))
                    .collect(Collectors.joining("\n"));
            throw new ExecutionException(message);
        }
    }
    /**
     * Validate a task against given parameters.
     * @param param expected params to validate Task
     */
    public void checkParameters(final List<String> param) {
        final List<String> wrongParams = new ArrayList<>();
        for (String item : param) {
            if (taskMap.containsKey(item)) {
                log.info("Found matching parameter: {}", item);
            } else {
                wrongParams.add(item);
                log.info("Matching parameter wasn't found: {} ", item);
            }
        }
        if (!wrongParams.isEmpty()) {
            throw new ExecutionException("Parameters are not present in task:\n{}", String.join("\t\t\n", wrongParams));
        }
    }
}
