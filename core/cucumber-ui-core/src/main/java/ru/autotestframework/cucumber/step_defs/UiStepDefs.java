package ru.autotestframework.cucumber.step_defs;

import io.cucumber.java.en.When;
import java.util.List;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * Ui step defs.
 */
@Slf4j
@RequiredArgsConstructor
@Description("UI Steps")
public class UiStepDefs implements IStepsUi {

    @Autowired
    private IStepsUi steps;

    @Autowired
    private PageManager pageManager;

    @When("закрыть приложение")
    @Sample("останавливает процесс драйвера")
    @Example(example = "И закрыть приложение")
    @Override
    public void closeApp() {
        steps.closeApp();
        Assertions.assertTrue(true, "");
    }

    /**
     * Waiting for content to load.
     */
    @When("ожидает прогрузки текущей страницы")
    @Sample("Ожидает пока на странице прогрузится контент")
    public void waitingForContentToLoad() {
        pageManager.getCurrent().checkAcceptor();
    }

    @When("перейти на страницу {resolvable_string}")
    @Sample("Выполнить инициализацию pageObject")
    @Parameter(type = "resolvable_string", name = "название страницы из аннотации @PageEntry")
    @Example(example = "И перейти на страницу 'Главная'")
    @Override
    public void setCurrentPage(final String pageTitle) {
        steps.setCurrentPage(pageTitle);
    }

    @When("нажать на элемент {resolvable_string}")
    @Sample("Выполнить клик по элементу")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 'Кнопка меню'")
    @Override
    public void click(final String elementTitle) {
        steps.click(elementTitle);
    }

    @When("нажать на элемент {int} из списка {resolvable_string}")
    @Sample("Выполнить клик по элементу из списка элементов")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 1 из списка 'Кнопки меню'")
    @Override
    public void click(final int index, final String elementTitle) {
        steps.click(index, elementTitle);
    }

    @When("дважды нажать на элемент {resolvable_string}")
    @Sample("Выполнить двойной клик по элементу")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И дважды нажать на элемент 'Кнопка меню'")
    @Override
    public void doubleClick(final String elementTitle) {
        steps.doubleClick(elementTitle);
    }

    @When("дважды нажать на элемент {int} из списка {resolvable_string}")
    @Sample("Выполнить двойной клик по элементу из списка элементов")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И дважды нажимает на элемент 1 из списка 'Кнопки меню'")
    @Override
    public void doubleClick(final int index, final String elementTitle) {
        steps.doubleClick(index, elementTitle);
    }

    @When("нажать на элемент {resolvable_string} правой кнопкой мыши")
    @Sample("Выполнить клик правой кнопкой мыши по элементу")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 'Кнопка меню' правой кнопкой мыши")
    @Override
    public void rightClick(final String elementTitle) {
        steps.rightClick(elementTitle);
    }

    @When("нажать на элемент {int} из списка {resolvable_string} правой кнопкой мыши")
    @Sample("Выполнить клик правой кнопкой мыши по элементу из списка элементов")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать правой кнопкой мыши на элемент 1 из списка 'Кнопки меню'")
    @Override
    public void rightClick(final int index, final String elementTitle) {
        steps.rightClick(index, elementTitle);
    }

    @When("установить фокус на элемент {resolvable_string}")
    @Sample("устанавливает фокус на элемент, а если элемент за границами видимости, то скролит к нему")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И установить фокус на элемент 'Отправить'")
    @Override
    public void hover(final String elementTitle) {
        steps.hover(elementTitle);
    }

    @When("установить фокус на элемент {int} из списка {resolvable_string}")
    @Sample("устанавливает фокус на элемент из списка, а если элемент за границами видимости, то скролит к нему")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И установить фокус на элемент 1 из списка 'Кнопки меню'")
    @Override
    public void hover(final int index, final String elementTitle) {
        steps.hover(index, elementTitle);
    }

    @When("очистить поле {resolvable_string}")
    @Sample("Очистить поле")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И очистить поле 'Логин'")
    @Override
    public void clearField(final String elementTitle) {
        steps.clearField(elementTitle);
    }

    @When("очистить поле {int} из списка {resolvable_string}")
    @Sample("Очистить поле из списка полей")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И очистить поле 1 из списка 'Логины'")
    @Override
    public void clearField(final int index, final String elementTitle) {
        steps.clearField(index, elementTitle);
    }

    @When("очистить поля:")
    @Sample("Очистить поля")
    @Parameter(type = ":", name = "список полей для очищения")
    @Example(example = "И очистить поля:\n" + "| Фамилия |\n" + "| Имя |\n" + "| Отчество |")
    @Override
    public void clearFields(final List<String> elementTitlesList) {
        steps.clearFields(elementTitlesList);
    }

    @When("заполнить поле {resolvable_string} значением {resolvable_string}")
    @Sample("Заполнить единственное поле значением")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И заполнить поле 'Теги' значением 'тег1, тег2'")
    @Override
    public void fillField(final String elementTitle, final String value) throws ElementInteractionException {
        steps.fillField(elementTitle, value);
    }

    @When("заполнить поле {int} из списка {resolvable_string} значением {resolvable_string}")
    @Sample("Заполнить единственное поле из списка полей значением")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И заполнить поле 1 из списка 'Теги' значением 'тег1, тег2'")
    @Override
    public void fillField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        steps.fillField(index, elementTitle, value);
    }

    @When("заполнить поля значениями:")
    @Sample("Заполнить список полей значениями")
    @Parameter(type = ":", name = "таблица из названий полей и значений")
    @Example(
            example = "И заполнить поля значениями:"
                    + "| Фамилия | Иванов     |"
                    + "| Имя     | Петр       |"
                    + "| Теги    | тег1, тег2 |")
    @Override
    public void fillFields(final ResolvableMap data) throws ElementInteractionException {
        steps.fillFields(data);
    }

    @When("выбрать в элементе {resolvable_string} значение/значения {resolvable_string}")
    @Sample("Выбрать в элементе поля со значением/значениями")
    @Parameter(type = "resolvable_string", name = "название элемента/списка")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И выбрать в элементе 'Теги' значения 'тег1, тег2'")
    @Override
    public void selectValue(final String elementTitle, final String value) throws ElementInteractionException {
        steps.selectValue(elementTitle, value);
    }

    @When("выбрать в элементе {int} из списка {resolvable_string} значение/значения {resolvable_string}")
    @Sample("Выбрать в элементе из списка элементов поля со значением/значениями")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента/списка")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И выбрать в элементе 1 из списка 'Теги' значения 'тег1, тег2'")
    @Override
    public void selectValue(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        steps.selectValue(index, elementTitle, value);
    }

    @When("дополнить поле {resolvable_string} значением {resolvable_string}")
    @Sample("Дополнить единственное поле значением")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И дополнить поле 'Теги' значением 'тег1, тег2'")
    @Override
    public void appendField(final String elementTitle, final String value) throws ElementInteractionException {
        steps.appendField(elementTitle, value);
    }

    @When("дополнить поле {int} из списка {resolvable_string} значением {resolvable_string}")
    @Sample("Дополнить единственное поле из списка полей значением")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И дополнить поле 1 из списка 'Теги' значением 'тег1, тег2'")
    @Override
    public void appendField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        steps.appendField(index, elementTitle, value);
    }

    @When("дополнить поля значениями:")
    @Sample("Дополнить список полей значениями")
    @Parameter(type = ":", name = "таблица из названий полей и значений")
    @Example(
            example = "И дополнить поля значениями:"
                    + "| Фамилия | Иванов     |"
                    + "| Имя     | Петр       |"
                    + "| Теги    | тег1, тег2 |")
    @Override
    public void appendFields(final ResolvableMap data) throws ElementInteractionException {
        steps.appendFields(data);
    }

    @When("проверить заполнение поля {resolvable_string} значением {resolvable_string}")
    @Sample("Проверяет значение в единственном поле. Для множественных значений проверяется полное соответствие по "
            + "количеству ожидаемых и фактических значений")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И проверить заполнение поля 'Теги' значением 'тег1, тег2'")
    @Override
    public void strongVerifyField(final String elementTitle, final String value) throws ElementInteractionException {
        steps.strongVerifyField(elementTitle, value);
    }

    @When("проверить заполнение поля {int} из списка {resolvable_string} значением {resolvable_string}")
    @Sample(
            "Проверяет значение в единственном поле из списка полей. Для множественных значений проверяется полное соответствие по "
                    + "количеству ожидаемых и фактических значений")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И проверить заполнение поля 1 из списка 'Теги' значением 'тег1, тег2'")
    @Override
    public void strongVerifyField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        steps.strongVerifyField(index, elementTitle, value);
    }

    @When("проверить вхождение в поле {resolvable_string} значений {resolvable_string}")
    @Sample("Для множественных значений единственного поля проверяется вхождение части ожидаемых значений в список "
            + "актуальных")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И проверить вхождение в поле 'Теги' значений 'тег1, тег2'")
    @Override
    public void verifyField(final String elementTitle, final String value) throws ElementInteractionException {
        steps.verifyField(elementTitle, value);
    }

    @When("проверить вхождение в поле {int} из списка {resolvable_string} значений {resolvable_string}")
    @Sample("Для множественных значений единственного поля проверяется вхождение части ожидаемых значений в список "
            + "актуальных")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = "resolvable_string", name = "значение или список значений через запятую")
    @Example(example = "И проверить вхождение в поле 1 из списка 'Теги' значений 'тег1, тег2'")
    @Override
    public void verifyField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        steps.verifyField(index, elementTitle, value);
    }

    @When("проверить заполнение полей:")
    @Sample("Проверяет заполнение в множестве полей. Для множественных значений проверяется полное соответствие по "
            + "количеству ожидаемых и фактических значений")
    @Parameter(type = ":", name = "таблица из названий полей и значений")
    @Example(
            example = " И проверить заполнение полей:"
                    + " | Фамилия | Иванов     |"
                    + " | Имя     | Петр       |"
                    + " | Теги    | тег1, тег2 |")
    @Override
    public void strongVerifyFields(final ResolvableMap data) throws ElementInteractionException {
        steps.strongVerifyFields(data);
    }

    @When("проверить вхождение значений в поля:")
    @Sample("Проверяет заполнение в множестве полей. Для множественных значений поля проверяется "
            + "вхождение части ожидаемых значений в список актуальных")
    @Parameter(type = ":", name = "таблица из названий полей и значений")
    @Example(
            example = "И проверить вхождение значений в поля:"
                    + "| Фамилия | Иванов     |"
                    + "| Имя     | Петр       |"
                    + "| Теги    | тег1, тег2 |")
    @Override
    public void verifyFields(final ResolvableMap data) throws ElementInteractionException {
        steps.verifyFields(data);
    }

    @When("проверить вхождение значений в список {resolvable_string}:")
    @Sample("Проверяет заполнение в списке полей")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = ":", name = "таблица из искомых значений")
    @Example(
            example = "И проверить вхождение значений в список 'Теги':"
                    + "| Иванов     |"
                    + "| Петр       |"
                    + "| тег1, тег2 |")
    @Override
    public void verifyFields(final String elementTitle, final ResolvableList values)
            throws ElementInteractionException {
        steps.verifyFields(elementTitle, values);
    }

    @When("проверить, что список {resolvable_string} содержит все элементы таблицы:")
    @Sample("Проверяет заполнение в списке полей")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Parameter(type = ":", name = "таблица из искомых значений")
    @Example(
            example = "И проверить, что список 'Тэги' содержит все элементы таблицы:"
                    + "| Иванов     |"
                    + "| Петр       |"
                    + "| тег1, тег2 |")
    @Override
    public void strongVerifyFields(final String elementTitle, final ResolvableList values)
            throws ElementInteractionException {
        steps.strongVerifyFields(elementTitle, values);
    }

    @When("перейти в окно {resolvable_string}")
    @Sample("Выполнить переключение на окно")
    @Parameter(type = "resolvable_string", name = "название окна")
    @Example(example = "И переходит на окно 'Другое окно'")
    @Override
    public void setWindow(final String windowName) {
        steps.setWindow(windowName);
    }

    @When("элементы {visibility} :")
    @Sample("проверяет отображение элементов на странице")
    @Parameter(type = "visibility", name = "видимость элемента")
    @Parameter(type = ":", name = "таблица из названий элементов")
    @Example(example = "элементы не отображаются :" + "| Элемент |" + "| Кнопка |" + "| Блок |")
    @Override
    public void verifyDisplayedElements(final Boolean isDisplayed, final List<String> elementTitlesList) {
        steps.verifyDisplayedElements(isDisplayed, elementTitlesList);
    }

    @When("{visibility} элемент {resolvable_string}")
    @Sample("проверяет отображение элемента на странице")
    @Parameter(type = "visibility", name = "видимость элемента")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "элемент 'Кнопка' не отображается")
    @Override
    public void verifyDisplayedElement(final Boolean isDisplayed, final String elementTitle) {
        steps.verifyDisplayedElement(isDisplayed, elementTitle);
    }

    @When("{visibility} элемент {int} из списка {resolvable_string}")
    @Sample("проверяет отображение элемента из списка на странице")
    @Parameter(type = "visibility", name = "видимость элемента")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "не отображается элемент 1 из списка 'Кнопки'")
    @Override
    public void verifyDisplayedElement(final Boolean isDisplayed, final int index, final String elementTitle) {
        steps.verifyDisplayedElement(isDisplayed, index, elementTitle);
    }

    @When("проверить, что элемент {resolvable_string} {activity}")
    @Sample("проверяет что элемент на странице активен")
    @Parameter(type = "activity", name = "активность элемента")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "проверить, что элемент 'Кнопка' не активен")
    @Override
    public void verifyActiveElement(final String elementTitle, final Boolean isActive) {
        steps.verifyActiveElement(elementTitle, isActive);
    }

    @When("проверить, что элемент {resolvable_string} {editable} для редактирования")
    @Sample("проверяет что элемент на странице редактируем")
    @Parameter(type = "editable", name = "редактируемость элемента")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "проверить, что элемент 'Кнопка' недоступен для редактирования")
    @Override
    public void verifyEditableElement(final String elementTitle, final Boolean isEditable) {
        steps.verifyEditableElement(elementTitle, isEditable);
    }

    @When("проверить, что элементы {editable} для редактирования:")
    @Sample("проверяет что элементы на странице доступны для редактирования")
    @Parameter(type = "editable", name = "редактируемость элемента")
    @Parameter(type = ":", name = "таблица из названий элементов")
    @Example(
            example = "проверить, что элементы доступны для редактирования:"
                    + "| Элемент |"
                    + "| Кнопка  |"
                    + "| Блок    |")
    @Override
    public void verifyEditableElements(final Boolean isEditable, final List<String> elementTitlesList) {
        steps.verifyEditableElements(isEditable, elementTitlesList);
    }

    @When("проверить, что элемент {int} из списка {resolvable_string} {activity}")
    @Sample("проверяет что элемент из списка на странице активен")
    @Parameter(type = "activity", name = "активность элемента")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "проверить, что элемент 1 из списка 'Кнопки' не активен")
    @Override
    public void verifyActiveElement(final int index, final String elementTitle, final Boolean isActive) {
        steps.verifyActiveElement(index, elementTitle, isActive);
    }

    @When("проверить, что элементы {activity} :")
    @Sample("проверяет что элементы на странице активны")
    @Parameter(type = "activity", name = "активность элементов")
    @Parameter(type = ":", name = "таблица из названий элементов")
    @Example(example = "проверить, что элементы не активны :" + "| Элемент |" + "| Кнопка |" + "| Блок |")
    @Override
    public void verifyActiveElements(final Boolean isActive, final List<String> elementTitlesList) {
        steps.verifyActiveElements(isActive, elementTitlesList);
    }

    @When("количество элементов в списке {resolvable_string} соответствует {int}")
    @Sample("Сравнить количество элементов в списке с заданным числом")
    @Parameter(type = "resolvable_string", name = "список элементов")
    @Parameter(type = "int", name = "количество элементов")
    @Example(example = "И количество элементов в списке 'Пункты меню' соответствует 15")
    public void equalsNumbersOfElements(final String elementTitle, final Integer numbers) {
        steps.equalsNumbersOfElements(elementTitle, numbers);
    }

    @When("сохранить в переменные значения полей:")
    @Sample("Сохраняет значения полей в контекстные переменные")
    @Parameter(type = ":", name = "таблица из названий полей и названий контекстных переменных")
    @Example(
            example = "И сохранить в переменные значения полей:"
                    + "| amount   | Сумма   |"
                    + "| currency | Валюта  |"
                    + "| date     | Дата    |")
    @Override
    public void readValues(final ResolvableMap data) {
        steps.readValues(data);
    }

    @When("сохранить в переменную {resolvable_string} значение поля {resolvable_string}")
    @Sample("Сохраняет значение поля в контекстную переменную")
    @Parameter(type = "resolvable_string", name = "наименование переменной")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И сохранить в переменную 'tagFieldValue' значение поля 'Теги'")
    public void readValue(final String variableName, final String elementTitle) {
        steps.readValue(variableName, elementTitle);
    }

    @When("ввести с клавиатуры {string}")
    @Sample("Осуществить ввод с клавиатуры")
    @Parameter(type = "string", name = "комбинация клавиш")
    @Example(example = "ввести с клавиатуры 'CONTROL+V'")
    @Override
    public void pressOnKeyBoard(final String keysCombination) {
        steps.pressOnKeyBoard(keysCombination);
    }

    @When("записать в файл {resolvable_string} скриншот текущего окна")
    @Sample("Делает скриншот текущего окна приложения и сохраняет в файл")
    @Example(example = "И сделать скриншот окна и сохранить в файл 'src/test/resources/screen.png'")
    public void takeScreenshotAndSave(final String path) {
        steps.takeScreenshotAndSave(path);
    }

    @When("записать в файл {resolvable_string} сравнение текущего окна со скриншотом {resolvable_string}")
    @Sample("Сравнивает скриншот текущего окна со скриншотом из файла")
    @Example(
            example =
                    "И записать в файл 'src/test/resources/result.png' сравнение текущего окна со скриншотом 'src/test/resources/screen.png'")
    public void compareCurrentWindowWithScreenshot(final String resultPath, final String path) {
        steps.compareCurrentWindowWithScreenshot(resultPath, path);
    }

    @When("записать в файл {resolvable_string} сравнение скриншотов {resolvable_string} {resolvable_string}")
    @Sample("Сравнивает два скриншота из файлов")
    @Example(
            example =
                    "И записать в файл 'src/test/resources/result.png' сравнение скриншотов 'src/test/resources/screen1.png' 'src/test/resources/screen2.png'")
    public void compareScreenshots(final String resultPath, final String path1, final String path2) {
        steps.compareScreenshots(resultPath, path1, path2);
    }

    @When("проверить у элемента {resolvable_string} наличие атрибута {resolvable_string}")
    @Sample("Проверяет наличие атрибута у элемента")
    @Parameter(type = "resolvable_string", name = "наименование элемента")
    @Parameter(type = "resolvable_string", name = "наименование атрибута")
    @Example(example = "И проверить у элемента 'Кнопка' наличие атрибута 'hidden'")
    @Override
    public void hasAttribute(final String elementTitle, final String attribute) {
        steps.hasAttribute(elementTitle, attribute);
    }

    @When("проверить у элемента {resolvable_string} значения атрибутов:")
    @Sample("Проверяет наличие атрибута у элемента")
    @Parameter(type = "resolvable_string", name = "наименование элемента")
    @Parameter(type = ":", name = "таблица с наименованием атрибутов, матчером и искомым значением")
    @Example(
            example = "И проверить у элемента 'Кнопка' значения атрибутов:"
                    + "| type | !=   | text   |"
                    + "| name | ==   | button |")
    @Override
    public void checkAttributes(final String elementTitle, final List<Triple> rows) {
        steps.checkAttributes(elementTitle, rows);
    }
}
