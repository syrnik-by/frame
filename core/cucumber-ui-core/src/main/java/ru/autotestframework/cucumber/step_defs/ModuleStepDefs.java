package ru.autotestframework.cucumber.step_defs;

import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.driver_manager.IModuledActions;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Module step defs.
 */
@RequiredArgsConstructor
@Slf4j
@Description("Html UI Steps")
public class ModuleStepDefs {

    private final IModuledActions iModuledActions;
    private final PageManager pageManager;

    /**
     * Click by text.
     *
     * @param text the text
     */
    @When("нажать на элемент с текстом {resolvable_string}")
    @Sample("Выполнить клик по элементу с текстом")
    @Parameter(type = "resolvable_string", name = "текст элемента")
    @Example(example = "И нажать на элемент с текстом 'файлы cookie'")
    public void clickByText(String text) {
        iModuledActions.clickByText(text);
    }
    ;

    /**
     * Drag and drop.
     *
     * @param elementNameFrom the element name from
     * @param elementNameTo   the element name to
     */
    @When("переместить элемент {resolvable_string} на место элемента {resolvable_string}")
    @Sample("Выполнить клик по элементу")
    @Parameter(type = "resolvable_string", name = "название элемента, который нужно перетащить мышкой")
    @Parameter(type = "resolvable_string", name = "название элемента, на место которого нужно перетащить")
    @Example(example = "И переместить элемент 'Виджет 1' на место элемента 'Виджет 2'")
    public void dragAndDrop(String elementNameFrom, String elementNameTo) {

        final IElement elementFrom = pageManager.getCurrent().getElementByTitle(elementNameFrom);
        final IElement elementTo = pageManager.getCurrent().getElementByTitle(elementNameTo);
        iModuledActions.dragAndDrop(elementFrom, elementTo);
    }

    /**
     * Visible multiline text.
     *
     * @param textContent the text content
     */
    @When("проверить многострочный текст:$")
    @Sample("Осуществить проверку многострочного текста на странице")
    @Parameter(type = "$", name = "многострочный текст для проверки")
    @Example(
            example = "проверить многострочный текст:"
                    + "\"\"\""
                    + "первая строка многострочного текста"
                    + "вторая строка многострочного текста"
                    + "\"\"\"")
    public void visibleMultilineText(String textContent) {
        iModuledActions.visibleMultilineText(textContent);
    }

    /**
     * Wait for element by text to disappear.
     *
     * @param seconds the seconds
     * @param text    the text
     */
    @When("подождать {int} секунд, пока не исчезнет элемент с текстом {resolvable_string}")
    @Sample("подождать несколько секунд, пока не исчезнет элемент с экрана")
    @Parameter(type = "int", name = "время ожидания в секундах")
    @Parameter(type = "resolvable_string", name = "текст элемента")
    @Example(example = "подождать 2 секунды, пока не исчезнет элемент с текстом 'Кнопка'")
    public void waitForElementByTextToDisappear(int seconds, String text) {
        iModuledActions.waitForElementByTextToDisappear(seconds, text);
    }

    /**
     * Wait for element to disappear.
     *
     * @param seconds      the seconds
     * @param elementTitle the element title
     */
    @When("подождать {int} секунд, пока не исчезнет элемент {resolvable_string}")
    @Sample("подождать несколько секунд, пока не исчезнет элемент с экрана")
    @Parameter(type = "int", name = "время ожидания в секундах")
    @Parameter(type = "resolvable_string", name = "имя элемента")
    @Example(example = "И подождать 2 секунды, пока не исчезнет элемент 'Button'")
    public void waitForElementToDisappear(int seconds, String elementTitle) {
        IElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        iModuledActions.waitForElementToDisappear(seconds, element);
    }

    /**
     * Check visible text.
     *
     * @param text        the text
     * @param isDisplayed the is displayed
     */
    @When("проверить, что текст {string} {visibility}")
    @Sample("Осуществить проверку текста на странице")
    @Parameter(type = "resolvable_string", name = "текст для проверки")
    @Parameter(type = "visibility", name = "видимость элемента")
    @Example(example = "проверить, что отображается текст 'текст'")
    public void checkVisibleText(String text, Boolean isDisplayed) {
        iModuledActions.checkVisibleText(text, isDisplayed);
    }
}
