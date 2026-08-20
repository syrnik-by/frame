package ru.autotestframework.debug_steps;

import static ru.autotestframework.util.Validator.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;

/**
 * Debug steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("Отладка")
public class DebugSteps {

    private final Context context;

    /**
     * Log variables.
     */
    @When("вывести в лог переменные из контекста")
    @Sample("Вывод в лог переменных контекста")
    @Example(example = "И вывести в лог переменные из контекста")
    public void logVariables() {
        log.info("Context contain variables: \n {}", context.getAll());
    }

    /**
     * Log step.
     *
     * @param string the string
     */
    @When("вывести в лог строку {resolvable_string}")
    @Sample("Вывод в лог переданной строки")
    @Parameter(type = "resolvable_string", name = "строка которую надо отобразить в логе")
    @Example(example = "И вывести в лог строку 'тестовая строка'")
    public void logStep(final String string) {
        assertThat(!string.equals("fail"), "debug assert");
        log.info("Printed string: {}", string);
    }

    /**
     * Log step.
     *
     * @param dataTable the data table
     */
    @When("тестим таблицу:")
    @Sample("Вывод в лог переданной таблицы")
    @Parameter(type = ":", name = "горизонтальная таблица для вывода в лог")
    @Example(example = "И тестим таблицу:" + "| variableName | value |")
    public void logStep(final DataTable dataTable) {
        log.info("Printed string: \n{}", dataTable);
    }
}
