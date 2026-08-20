package ru.autotestframework.camunda_steps;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.mockito.Mockito.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.camunda_steps.components.Camunda;
import ru.autotestframework.camunda_steps.components.CamundaTask;
import ru.autotestframework.core.exception.ExecutionException;

@Tag("@CamundaDemo")
@WireMockTest(httpPort = CamundaTest.WIRE_MOCK_SERVER_PORT)
class CamundaTest {
    public static final int WIRE_MOCK_SERVER_PORT = 12345;
    private static final String CAMUNDA_URL = "http://localhost";
    private static final String URL_METHOD = "/engine-rest/external-task/";
    private final CamundaSteps steps = new CamundaSteps();

    @BeforeAll
    static void initServer() {
        RestAssured.port = WIRE_MOCK_SERVER_PORT;
    }

    @BeforeEach
    void before() {
        steps.setUrl(CAMUNDA_URL);
    }

    @Test
    void setUrl() {
        Assertions.assertEquals(CAMUNDA_URL, steps.getCamunda().getUrlCamunda());
    }

    @Test
    void collectTaskList() {
        stubUrl(URL_METHOD, "get_task_list_report.json");
        steps.collectTaskList();
        Assertions.assertEquals(
                "content2", steps.getTaskList().get(1).getTaskMap().get("id2"));
    }

    @Test
    void collectTask() {
        stubUrl(URL_METHOD + "id1", "get_task_list_report.json");
        steps.collectTask("id", "id1");
        Assertions.assertEquals("content2", steps.getTask().getTaskMap().get("id2"));
    }

    @Test
    void getBusinessKeyTest() {
        stubUrl(URL_METHOD + "id1", "get_task_list_report.json");
        steps.collectTask("id", "id1");
        Assertions.assertEquals("content3", steps.getTask().getBusinessKey());
    }

    @Test
    void collectTaskByBusinessKeyTest() {
        Camunda camunda = mock(Camunda.class);
        when(camunda.getExternalTask("businessKey", "content3")).thenCallRealMethod();
        when(camunda.getExternalTaskOnBusinessKey("content3")).thenThrow(Error.class);
        Assertions.assertThrows(Error.class, () -> camunda.getExternalTask("businessKey", "content3"));
    }

    @Test
    void collectTaskByNotFoundValueTest() {
        Camunda camunda = mock(Camunda.class);
        when(camunda.getExternalTask("noKey", "noContent")).thenCallRealMethod();
        Assertions.assertThrows(ExecutionException.class, () -> camunda.getExternalTask("noKey", "noContent"));
    }

    @Test
    void getExternalTaskOnBusinessKeyTest() {
        Camunda camunda = mock(Camunda.class);
        when(camunda.getExternalTaskOnBusinessKey("content3")).thenCallRealMethod();
        when(camunda.getAllExternalTasks()).thenThrow(Error.class);
        Assertions.assertThrows(Error.class, () -> camunda.getExternalTaskOnBusinessKey("content3"));
    }

    @Test
    void getExternalTaskOnBusinessKeyExistValueTest() {
        Camunda camunda = mock(Camunda.class);
        CamundaTask camundaTask = mock(CamundaTask.class);
        List<CamundaTask> camundaTaskList = List.of(camundaTask);
        when(camunda.getExternalTaskOnBusinessKey("content3")).thenCallRealMethod();
        when(camunda.getAllExternalTasks()).thenReturn(camundaTaskList);
        when(camundaTask.getBusinessKey()).thenReturn("content3");
        Assertions.assertEquals(camundaTask, camunda.getExternalTaskOnBusinessKey("content3"));
    }

    @Test
    void getExternalTaskOnBusinessKeyNoValueTest() {
        Camunda camunda = mock(Camunda.class);
        CamundaTask camundaTask = mock(CamundaTask.class);
        List<CamundaTask> camundaTaskList = List.of(camundaTask);
        when(camunda.getExternalTaskOnBusinessKey("content3")).thenCallRealMethod();
        when(camunda.getAllExternalTasks()).thenReturn(camundaTaskList);
        when(camundaTask.getBusinessKey()).thenReturn("NoValue");
        Assertions.assertNull(camunda.getExternalTaskOnBusinessKey("content3"));
    }

    @Test
    void checkParametersWrongParamsTest() {
        CamundaTask camundaTask = new CamundaTask(Map.of("id1", "value1", "id2", "value2"));
        List<String> param = List.of("param1");
        Assertions.assertThrows(ExecutionException.class, () -> camundaTask.checkParameters(param));
    }

    @Test
    void checkParametersWrongParamsMapTest() {
        CamundaTask camundaTask = new CamundaTask(Map.of("id1", "value1", "id2", "value2"));
        Map<String, String> param = Map.of("id1", "param3");
        Assertions.assertThrows(ExecutionException.class, () -> camundaTask.checkParameters(param));
    }

    @Test
    void checkKeyInTaskList() {
        stubUrl(URL_METHOD + "id1", "get_task_list_report.json");
        steps.collectTask("id", "id1");
        steps.checkKeyInTaskList(List.of("id1", "id2"));
    }

    @Test
    void checkKeyAndValueInTaskList() {
        stubUrl(URL_METHOD + "id1", "get_task_list_report.json");
        steps.collectTask("id", "id1");
        steps.checkKeyAndValueInTaskList(Map.of("id1", "content1", "id2", "content2"));
    }

    @Test
    void activeProcess() {
        stubUrl(URL_METHOD + "count", "get_count_active_process.json");
        steps.activeProcess();
        Assertions.assertDoesNotThrow(() -> steps.checkActiveProcess("11"));
        Assertions.assertThrows(AssertionError.class, () -> steps.checkActiveProcess("12"));
    }

    private void stubUrl(final String url, final String dataFile) {
        stubFor(WireMock.get(url)
                .willReturn(WireMock.okJson(getFileContent("data/demo/wiremock/mappings/" + dataFile))));
    }

    private String getFileContent(final String path) {
        try (InputStream is =
                        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path));
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr)) {
            return br.lines().collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new ExecutionException("Unable to read the file '{}'", e, path);
        }
    }
}
