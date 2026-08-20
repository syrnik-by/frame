package ru.autotestframework.cucumber.step_defs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.IModuledActions;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Moduled actions.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class ModuledActionsImpl implements IModuledActions {
    private final DriverContainer driverContainer;

    @Override
    public void checkVisibleText(String text, final Boolean isDisplayed) {
        driverContainer.getActiveDriver().checkVisibleText(text, isDisplayed);
    }

    @Override
    public void clickByText(String text) {
        driverContainer.getActiveDriver().clickByText(text);
    }

    @Override
    public void dragAndDrop(IElement elementFrom, IElement elementTo) {
        driverContainer.getActiveDriver().dragAndDrop(elementFrom, elementTo);
    }

    @Override
    public void visibleMultilineText(String textContent) {
        driverContainer.getActiveDriver().checkVisibleText(textContent, true);
    }

    @Override
    public void waitForElementByTextToDisappear(int seconds, String text) {
        driverContainer.getActiveDriver().waitForElementByTextToDisappear(seconds, text);
    }

    @Override
    public void waitForElementToDisappear(int seconds, IElement element) {
        driverContainer.getActiveDriver().waitForElementToDisappear(seconds, element);
    }
}
