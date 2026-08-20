package ru.autotestframework.camunda_steps.components;

import static io.restassured.RestAssured.given;

import io.restassured.path.json.config.JsonPathConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.ExecutionException;

@Slf4j
@Data
public class Camunda {

    private String urlCamunda;

    private static final String URL_METHOD = "/engine-rest/external-task/";

    /**
     * returns count of active processes
     * @return
     */
    public String getCountActiveProcess() {
        return given().baseUri(urlCamunda + URL_METHOD + "count")
                .when()
                .get()
                .jsonPath(new JsonPathConfig("/"))
                .getString("count");
    }

    /**
     * returns list of tasks from camunda body
     * @return
     */
    public List<CamundaTask> getAllExternalTasks() {
        List<String> tempList = new ArrayList<>(
                Arrays.asList(getBodyCamunda(urlCamunda + URL_METHOD).split("},")));
        return getTaskListFromListString(tempList);
    }

    /**
     * Get an appropriate task from service for given params or throw.
     * @param key   key
     * @param value value
     * @return CamundaTask
     */
    public CamundaTask getExternalTask(final String key, final String value) {
        switch (key) {
            case "id":
                return getExternalTaskOnIdCamunda(value);
            case "businessKey":
                return getExternalTaskOnBusinessKey(value);
            default:
                throw new ExecutionException(
                        "The task was not found by parameters: key - {} and value - {}", key, value);
        }
    }

    /**
     * returns camunda task by id
     * @param idTask
     * @return
     */
    public CamundaTask getExternalTaskOnIdCamunda(final String idTask) {
        return parseTaskString(getBodyCamunda(urlCamunda + URL_METHOD + idTask));
    }
    /**
     * Return Task on given business key.
     *
     * @param businessKey Task key
     * @return appropriate task from service for given params
     */
    public CamundaTask getExternalTaskOnBusinessKey(final String businessKey) {
        List<CamundaTask> camundaTaskList = getAllExternalTasks();
        for (CamundaTask camundaTask : camundaTaskList) {
            if (camundaTask.getBusinessKey().equals(businessKey)) {
                return camundaTask;
            }
        }
        return null;
    }

    private String getBodyCamunda(final String url) {
        return given().baseUri(url).when().get().getBody().asString();
    }

    private CamundaTask parseTaskString(final String taskStr) {
        Map<String, String> tempHashMap = new HashMap<>();
        for (String str : taskStr.replaceAll("[\\[{}\\]\"]", "").split(",")) {
            String[] arr = str.trim().split(":");
            tempHashMap.put(arr[0].trim(), arr[1].trim());
        }
        return new CamundaTask(tempHashMap);
    }

    private List<CamundaTask> getTaskListFromListString(final List<String> strList) {
        List<CamundaTask> camundaTaskList = new ArrayList<>();
        for (String taskStr : strList) {
            camundaTaskList.add(parseTaskString(taskStr));
        }
        return camundaTaskList;
    }
}
