package ru.autotestframework.autoit_elements.step_defs;

import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import ru.autotestframework.autoit_junit.driver_manager.drivers.AutoItXDriver;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

/**
 * Auto it steps ui.
 */
@RequiredArgsConstructor
public class AutoItStepsUi {
    private final DriverContainer driverContainer;
    private final PageManager pageManager;

    /**
     * Open new app.
     *
     * @param app the app
     */
    @When("открыть приложение {resolvable_string}")
    @Sample("Открывает/перезапускает ранее открытое приложение")
    @Parameter(type = "resolvable_string", name = "наименование нового приложения")
    @Example(example = "И открыть приложение 'путь к приложению'")
    public void openNewApp(final String app) {
        ((AutoItXDriver) driverContainer.getActiveDriver().getDriver()).setNewApplication(app);
    }
}
