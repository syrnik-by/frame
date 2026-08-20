package ru.autotestframework.camunda_steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import ru.autotestframework.camunda_steps.components.Camunda;
import ru.autotestframework.camunda_steps.components.CamundaTask;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.util.Validator;

@Slf4j
@RequiredArgsConstructor
@Description("Camunda шаги")
@Getter
public class CamundaSteps {

    private final Camunda camunda = new Camunda();
    private List<CamundaTask> taskList;
    private CamundaTask task;
    private String countTask;

    private static final String CAMUNDA_URL_NOT_SET = "Camunda url not set";
    private static final String TASK_URL_NOT_SET = "Task not set";

    @When("установить Camunda url: {resolvable_string}")
    @Sample("Установить адрес сервера")
    @Parameter(type = "string", name = "Адрес")
    @Example(example = "установить Camunda url: http://camunda-01/")
    public void setUrl(final String url) {
        camunda.setUrlCamunda(url);
    }

    @When("получить список всех задач в Camunda")
    @Sample("получить список всех задач в Camunda (будут сохранены в контекст Модуля)")
    @Example(example = "получить список всех задач в Camunda")
    public void collectTaskList() {
        Assertions.assertNotNull(camunda.getUrlCamunda(), CAMUNDA_URL_NOT_SET);
        taskList = camunda.getAllExternalTasks();
    }

    @When("получить таску по {resolvable_string}, который равен {resolvable_string}")
    @Sample("Получить таску по паре ключ-значение")
    @Parameter(type = "string", name = "ключ")
    @Parameter(type = "string", name = "значение")
    @Example(example = "получить таску по 'id', который равен '12'")
    public void collectTask(final String key, final String value) {
        Assertions.assertNotNull(camunda.getUrlCamunda(), CAMUNDA_URL_NOT_SET);
        task = camunda.getExternalTask(key, value);
    }

    @Then("проверить наличие в Сamunda параметров по Task")
    @Sample("проверить наличие в Сamunda параметров")
    @Parameter(type = ":", name = "список параметров")
    @Example(example = "проверить наличие в Сamunda параметров по Task" + "| active |" + "| author |")
    public void checkKeyInTaskList(final List<String> param) {
        Assertions.assertNotNull(task, TASK_URL_NOT_SET);
        task.checkParameters(param);
    }

    @Then("проверить наличие в Сamunda значения для параметров по Task")
    @Sample("проверить наличие в Сamunda значения для параметров")
    @Parameter(type = ":", name = "список параметров и значений")
    @Example(example = "проверить наличие в Сamunda значения для параметров по Task")
    public void checkKeyAndValueInTaskList(final Map<String, String> param) {
        Assertions.assertNotNull(task, TASK_URL_NOT_SET);
        task.checkParameters(param);
    }

    @When("запрос количества активных процессов Camunda")
    @Sample("записать кол-во активных процессов в контекст Модуля")
    @Example(example = "запрос количества активных процессов Camunda")
    public void activeProcess() {
        Assertions.assertNotNull(camunda.getUrlCamunda(), CAMUNDA_URL_NOT_SET);
        countTask = camunda.getCountActiveProcess();
        log.info("Amount of active Camunda Tasks: {}", countTask);
    }

    @When("количество активных процессов в Camunda {resolvable_string}")
    @Sample("Проверить число активных процессов")
    @Parameter(type = "string", name = "число процессов")
    @Example(example = "количество активных процессов в Camunda '12'")
    public void checkActiveProcess(final String expectedCount) {
        Validator.assertThat(
                expectedCount.equals(countTask),
                "Amount of active Tasks: {} doesn't match the expected: {}",
                countTask,
                expectedCount);
    }
}
