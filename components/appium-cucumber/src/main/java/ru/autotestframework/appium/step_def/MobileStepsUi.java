package ru.autotestframework.appium.step_def;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.autotestframework.cucumber.parser.MatcherParser.getMatcher;
import static ru.autotestframework.util.StringUtil.format;

import com.codeborne.selenide.*;
import com.codeborne.selenide.ex.UIAssertionError;
import io.cucumber.java.en.When;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.context.annotation.Description;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.cucumber.PlaceholderResolver;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.util.Validator;

@RequiredArgsConstructor
@Slf4j
@Description("Mobile Steps")
public class MobileStepsUi {
    private final DriverContainer driverContainer;
    private final PageManager pageManager;
    private final Context context;
    private final PlaceholderResolver placeholderResolver;

    @When("нажать на элемент с текстом {resolvable_string}")
    @Sample("Выполнить клик по элементу с текстом")
    @Parameter(type = "resolvable_string", name = "текст элемента")
    @Example(example = "И нажать на элемент с текстом 'файлы cookie'")
    public void clickByText(final String text) {
        driverContainer
                .getActiveDriver()
                .getDriver()
                .findElement(By.xpath("//*[contains(text(), '" + text + "')]"))
                .click();
    }

    @When("нажать на элемент {resolvable_string} используя javascript")
    @Sample("Выполнить клик по элементу используя javascript")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 'Кнопка меню' используя javascript")
    public void clickWithJs(final String elementTitle) {
        TypifiedMobileElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        element.getSelenideElement().click(ClickOptions.usingJavaScript());
    }

    @When("переместить элемент {resolvable_string} на место элемента {resolvable_string}")
    @Sample("Выполнить клик по элементу")
    @Parameter(type = "resolvable_string", name = "название элемента, который нужно перетащить мышкой")
    @Parameter(type = "resolvable_string", name = "название элемента, на место которого нужно перетащить")
    @Example(example = "И переместить элемент 'Виджет 1' на место элемента 'Виджет 2'")
    public void dragAndDrop(final String elementNameFrom, final String elementNameTo) {
        final IElement elementFrom = pageManager.getCurrent().getElementByTitle(elementNameFrom);
        final IElement elementTo = pageManager.getCurrent().getElementByTitle(elementNameTo);

        if (isHtml5Draggable(elementFrom)) {
            log.warn("Element '{}' have 'draggable' HTML5 attribute : trying with JS", elementFrom.getTitle());
            $(elementFrom).dragAndDropTo($(elementTo));
        } else {
            log.warn("Element '{}' not have 'draggable' HTML5 attribute, trying with Actions", elementFrom.getTitle());
            var actions = new Actions(WebDriverRunner.getWebDriver());
            actions.dragAndDrop(elementFrom, elementTo).build().perform();
        }
    }

    @When("проверить, что текст {string} {visibility}")
    @Sample("Осуществить проверку текста на странице")
    @Parameter(type = "resolvable_string", name = "текст для проверки")
    @Parameter(type = "visibility", name = "видимость элемента")
    @Example(example = "проверить, что текст 'текст' не отображается")
    @Example(example = "проверить, что текст 'текст' отображается")
    public void checkVisibleText(final String text, final Boolean isDisplayed) {
        var by = withText(text);
        Condition visibleCondition = isDisplayed ? Condition.visible : Condition.visible.negate();
        Validator.tryOrAssertion(
                () -> $(by).shouldBe(visibleCondition),
                "Element with text '{}' doesn't match the expected displayed status (expected '{}')",
                text,
                isDisplayed);
    }

    @When("проверить многострочный текст:$")
    @Sample("Осуществить проверку многострочного текста на странице")
    @Parameter(type = "$", name = "многострочный текст для проверки")
    @Example(
            example = "проверить многострочный текст:"
                    + "\"\"\""
                    + "первая строка многострочного текста"
                    + "вторая строка многострочного текста"
                    + "\"\"\"")
    public void visibleMultilineText(final String textContent) {
        checkVisibleText(textContent, true);
    }

    @When("проверить CSS-свойства элемента {resolvable_string}:")
    @Sample("Осуществить проверку CSS-свойства элемента")
    @Parameter(type = "resolvable_string", name = "имя элемента")
    @Parameter(type = ":", name = "таблица c сss данными")
    @Example(
            example = "проверить CSS-свойства элемента {resolvable_string}:"
                    + "| property1 | propertyValue1 |"
                    + "| property2 | propertyValue2 |")
    public void cssProperty(final String elementTitle, final ResolvableMap cssProperties) {
        final TypifiedMobileElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        cssProperties.forEach((cssProperty, expectedValue) -> {
            String cssPropertyValue = element.getCssValue(cssProperty);
            Validator.assertThat(
                    cssPropertyValue.equals(expectedValue),
                    "Element '{}' have css property '{}' in actual value '{}' doesn't match" + " expected value '{}'",
                    elementTitle,
                    cssProperty,
                    cssPropertyValue,
                    expectedValue);
        });
    }

    private boolean isHtml5Draggable(final WebElement element) {
        return element.getAttribute("draggable").equalsIgnoreCase("true");
    }

    @When("проверить, что URL страницы {matcher} {resolvable_string}")
    @Sample("Проверить URL адрес страницы")
    @Parameter(type = "matcher", name = "матчер проверки")
    @Parameter(type = "resolvable_string", name = "ожидаемый URL страницы")
    @Example(example = "проверить, что URL страницы == 'https://yandex.ru/'")
    public void checkUrl(final MatcherName matcherName, final String expectedUrl) {
        String currentUrl = driverContainer.getActiveDriver().getDriver().getCurrentUrl();
        assertThat(currentUrl, getMatcher(matcherName, expectedUrl));
    }

    @When("проверить, что заголовок страницы {matcher} {resolvable_string}")
    @Sample("Проверить заголовок страницы")
    @Parameter(type = "matcher", name = "матчер проверки")
    @Parameter(type = "resolvable_string", name = "текст для проверки")
    @Example(example = "проверить, что заголовок страницы == 'Банковские карты'")
    public void checkTitle(final MatcherName matcherName, final String expectedTitle) {
        String currentTitle = driverContainer.getActiveDriver().getDriver().getTitle();
        assertThat(currentTitle, getMatcher(matcherName, expectedTitle));
    }

    @When("выбрать в открывшемся поп-апе {popup_action}")
    @Sample("выбрать в открывшемся поп-апе Подтвердить")
    @Parameter(type = "popup_action", name = "Действие")
    @Example(example = "выбрать в открывшемся поп-апе Подтвердить")
    public void closePopUp(final String command) {
        var webDriver = WebDriverRunner.getWebDriver();
        if (command.equalsIgnoreCase("подтвердить")) {
            webDriver.switchTo().alert().accept();
        } else {
            webDriver.switchTo().alert().dismiss();
        }
    }

    @When("проверить, что текст поп-апа {matcher} {resolvable_string}")
    @Sample("проверить, что текст поп-апа соответствует условию")
    @Parameter(type = "matcher", name = "применимый оператор сравнения")
    @Parameter(type = "resolvable_string", name = "сравниваемое значение")
    @Example(example = "проверить, что текст поп-апа contains 'Введите текст'")
    public void getPopUpText(final MatcherName matcherName, final String expectedValue) {
        var webDriver = WebDriverRunner.getWebDriver();
        String alertText = webDriver.switchTo().alert().getText();
        Validator.assertThat(
                alertText,
                getMatcher(matcherName, expectedValue),
                "Actual text of alert ({}) doesn't match the expected {}",
                alertText,
                expectedValue);
    }

    @When("ввести в окне поп-апа {resolvable_string}")
    @Sample("заполнить поле поп-апа")
    @Parameter(type = "resolvable_string", name = "текст")
    @Example(example = "ввести в окне поп-апа 'vasya@mail.ru'")
    public void setPopupText(final String value) {
        var webDriver = driverContainer.get();
        webDriver.switchTo().alert().sendKeys(value);
    }

    @When("прокрутить ScrollBar вниз до конца страницы")
    @Sample("прокрутить ScrollBar вниз до конца страницы")
    @Example(example = "прокрутить ScrollBar вниз до конца")
    public void scrollDown() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    @When("прокрутить ScrollBar вверх до начала страницы")
    @Sample("прокрутить ScrollBar вверх до начала страницы")
    @Example(example = "прокрутить ScrollBar вверх до начала страницы")
    public void scrollUp() {
        executeJavaScript("window.scrollTo(0, 0)");
    }

    @When("прокрутить ScrollBar {scrollDirection} на {int} пикселей")
    @Sample("прокрутить ScrollBar вниз или вверх на определённое количество пикселей")
    @Parameter(type = "scrollDirection", name = "направление прокрутки")
    @Parameter(type = "int", name = "количество пикселей")
    @Example(example = "прокрутить ScrollBar вниз на 100 пикселей")
    public void scroll(final String direction, final int pixels) {
        if (direction.equals("вниз")) {
            executeJavaScript("window.scrollBy(0, " + pixels + ")");
        } else if (direction.equals("вверх")) {
            executeJavaScript("window.scrollBy(0, -" + pixels + ")");
        }
    }

    @When("прокрутить ScrollBar до появления элемента с текстом {resolvable_string}")
    @Sample("прокрутить ScrollBar до появления элемента с текстом")
    @Parameter(type = "resolvable_string", name = "текст элемента")
    @Example(example = "И прокрутить ScrollBar до появления элемента с текстом 'Кнопка'")
    public void scrollWhileElemByTextNotFoundOnPage(final String text) {
        final SelenideElement element = Selenide.$(withText(text));
        do {
            if (element.isDisplayed()) {
                break;
            }
            executeJavaScript("return window.scrollBy(0, 250);");
        } while (!atBottom());
        assertThat("Элемент с текстом " + text + " не найден", element.isDisplayed());
    }

    @When("прокрутить ScrollBar до появления элемента {resolvable_string}")
    @Sample("прокрутить ScrollBar до появления элемента")
    @Parameter(type = "resolvable_string", name = "имя элемента")
    @Example(example = "И прокрутить ScrollBar до появления элемента 'Кнопка'")
    public void scrollWhileElemNotFoundOnPage(final String elementTitle) {
        final TypifiedMobileElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        do {
            if (element.isDisplayed()) {
                break;
            }
            executeJavaScript("return window.scrollBy(0, 250);");
        } while (!atBottom());
        assertThat("Элемент " + elementTitle + " не найден", element.isDisplayed());
    }

    @When("обновить текущую страницу")
    @Sample("обновить текущую страницу")
    @Example(example = "обновить текущую страницу")
    public void refreshCurrentPage() {
        refresh();
    }

    @When("подождать {int} секунд, пока не исчезнет элемент с текстом {resolvable_string}")
    @Sample("подождать несколько секунд, пока не исчезнет элемент с экрана")
    @Parameter(type = "int", name = "время ожидания в секундах")
    @Parameter(type = "resolvable_string", name = "текст элемента")
    @Example(example = "подождать 2 секунды, пока не исчезнет элемент с текстом 'Кнопка'")
    public void waitForElementByTextToDisappear(final int seconds, final String text) {
        final SelenideElement element = Selenide.$(withText(text));
        try {
            element.should(Condition.disappear, Duration.ofSeconds(seconds));
        } catch (UIAssertionError e) {
            throw new AssertionError(format("Element with text '{}' not disappear in {} seconds", text, seconds));
        }
    }

    @When("подождать {int} секунд, пока не исчезнет элемент {resolvable_string}")
    @Sample("подождать несколько секунд, пока не исчезнет элемент с экрана")
    @Parameter(type = "int", name = "время ожидания в секундах")
    @Parameter(type = "resolvable_string", name = "имя элемента")
    @Example(example = "подождать 2 секунды, пока не исчезнет элемент 'Button'")
    public void waitForElementToDisappear(final int seconds, final String elementTitle) {
        final SelenideElement element =
                Selenide.$((WebElement) pageManager.getCurrent().getElementByTitle(elementTitle));
        try {
            element.should(Condition.disappear, Duration.ofSeconds(seconds));
        } catch (UIAssertionError e) {
            throw new AssertionError(format("Element '{}' not disappear in {} seconds", elementTitle, seconds));
        }
    }
}
