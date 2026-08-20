package ru.autotestframework.web_elements.step_defs;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.autotestframework.Constants.*;
import static ru.autotestframework.cucumber.parser.MatcherParser.getMatcher;
import static ru.autotestframework.cucumber.type.CucumberTypesDefinition.TABLE_CONVERTER;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.en.When;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v96.fetch.Fetch;
import org.openqa.selenium.devtools.v96.fetch.model.HeaderEntry;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Description;
import ru.autotestframework.Constants;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.type.resolvable.ResolvableDataTable;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.page_manager.URL;
import ru.autotestframework.util.Validator;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

@RequiredArgsConstructor
@Slf4j
@Description("Html UI Steps")
public class WebStepsUi {
    private static final String URL_FIELD_NAME = "url";
    private static final String LOCAL_STORAGE = "localStorage";
    private static final String URL_MESSAGE =
            "Declaration of URL field with annotation is incorrect or missed for page: {}, class: {},\n"
                    + "   expected annotation: @URL(url = \"{link}\"),\n"
                    + "   expected fieldName: {}";
    private final PageManager pageManager;
    private final WebDriversProperties webDriversProperties;
    private final Context context;
    private final PlaceholderResolver placeholderResolver;
    private final FileLoaderImpl fileLoader;

    private static void openWithCredentials(final String absoluteUrl, final ResolvableDataTable dataTable) {
        var credDataTable = dataTable.getValue();
        Map<Object, Object> credentials = TABLE_CONVERTER
                .toMaps(credDataTable, String.class, String.class)
                .get(0);
        var password = credentials.get("password").toString();
        var user = credentials.get("user").toString();
        String domain = credentials.get("domain") == null
                ? ""
                : credentials.get("domain").toString();
        if (user == null) {
            throw new AutotestException("Parameters 'user' or/and 'password' wasn't provided");
        }
        open(absoluteUrl, domain, user, password);
    }

    @When("открыть ссылку {resolvable_string}")
    @Sample("Перейти по URL адресу")
    @Parameter(type = "resolvable_string", name = "URL адрес страницы для прямого перехода")
    @Example(example = "И открыть ссылку 'https://yandex.ru/'")
    public void openUrl(final String url) {
        open(url);
    }

    @When("нажать на элемент {resolvable_string} используя javascript")
    @Sample("Выполнить клик по элементу используя javascript")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 'Кнопка меню' используя javascript")
    public void clickWithJs(final String elementTitle) {
        TypifiedWebElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        element.getSelenideElement().click(ClickOptions.usingJavaScript());
    }

    @When("нажать на элемент {int} из списка {resolvable_string} используя javascript")
    @Sample("Выполнить клик по элементу из списка элементов используя javascript")
    @Parameter(type = "int", name = "номер элемента в списке")
    @Parameter(type = "resolvable_string", name = "название элемента")
    @Example(example = "И нажать на элемент 1 из списка 'Кнопки меню' используя javascript")
    public void clickWithJs(final int index, final String elementTitle) {
        TypifiedWebElement element = (TypifiedWebElement)
                pageManager.getCurrent().getElementsListByTitle(elementTitle).get(index - 1);
        element.getSelenideElement().click(ClickOptions.usingJavaScript());
    }

    @When("добавить записи в {storages}:")
    @Sample("Добавить записи в localStorage")
    @Parameter(type = "storages", name = "тип Хранилища (localStorage/sessionStorage)")
    @Parameter(type = ":", name = "таблица из названий записей и значений")
    @Example(example = "И добавить записи в localStorage:" + "| key1 | value1 |" + "| key2 | value2 |")
    public void setKeysToStorage(final String storage, final ResolvableMap data) {
        if (storage.equals(LOCAL_STORAGE)) {
            data.forEach((key, value) -> localStorage().setItem(key, value));
            return;
        }
        data.forEach((key, value) -> sessionStorage().setItem(key, value));
    }

    @When("проверить записи в {storages}:")
    @Sample("Проверить записи в localStorage")
    @Parameter(type = "storages", name = "тип Хранилища (localStorage/sessionStorage)")
    @Parameter(type = ":", name = "таблица из названий записей и значений")
    @Example(example = "И проверить записи в localStorage:" + "| key1 | value1 |" + "| key2 | value2 |")
    public void checkKeysToStorage(final String storage, final ResolvableMap data) {
        log.info("checkKeys storage - {}", storage);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String item = executeJavaScript(String.format("return %s.getItem('%s')", storage, entry.getKey()));
            assertEquals(entry.getValue(), item, "Parameter - '" + entry.getKey() + "'");
        }
    }

    @When("удалить записи в {storages}:")
    @Sample("Удалить записи в localStorage")
    @Parameter(type = "storages", name = "тип Хранилища (localStorage/sessionStorage)")
    @Parameter(type = ":", name = "таблица из названий записей")
    @Example(example = "И удалить записи в localStorage:" + "| key1 |" + "| key2 |")
    public void deleteKeysToStorage(final String storage, final ResolvableList data) {
        log.info("delete storage - {}", storage);
        if (storage.equals(LOCAL_STORAGE)) {
            data.forEach(key -> localStorage().removeItem(key));
            return;
        }
        data.forEach(key -> sessionStorage().removeItem(key));
    }

    @When("удалить все записи в {storages}")
    @Sample("Удалить все записи в localStorage")
    @Parameter(type = "storages", name = "тип Хранилища (localStorage/sessionStorage)")
    @Example(example = "И удалить все записи в localStorage")
    public void deleteAllKeysToStorage(final String storage) {
        log.info("clear storage - {}", storage);
        if (storage.equals(LOCAL_STORAGE)) {
            localStorage().clear();
            return;
        }
        sessionStorage().clear();
    }

    @When("добавить cookies :")
    @Sample("добавляет cookies в сессию браузера")
    @Parameter(type = ":", name = "таблица из cookies")
    @Example(example = "добавить cookies :" + "|Имя cookie 1|Значение cookie 1|" + "|Имя cookie 2|Значение cookie 2|")
    public void addCookies(final ResolvableMap cookies) {
        var webDriver = WebDriverRunner.getWebDriver();
        cookies.forEach((name, value) -> webDriver.manage().addCookie(new Cookie(name, value)));
    }

    @When("проверить cookies :")
    @Sample("проверить cookies в сессии браузера")
    @Parameter(type = ":", name = "таблица из cookies")
    @Example(example = "проверить cookies :" + "|Имя cookie 1|Значение cookie 1|" + "|Имя cookie 2|Значение cookie 2|")
    public void checkCookies(final ResolvableMap cookies) {
        var webDriver = WebDriverRunner.getWebDriver();
        cookies.forEach((name, value) -> Optional.ofNullable(webDriver.manage().getCookieNamed(name))
                .ifPresentOrElse(cookie -> checkCookie(cookie, value), () -> {
                    throw new ExecutionException("Cookie {} not exist", name);
                }));
    }

    @When("проверить заголовок cookies :")
    @Sample("проверить заголовок cookies в сессии браузера")
    @Parameter(type = ":", name = "таблица из cookies")
    @Example(example = "проверить заголовок cookies :" + "|Имя cookie 1|" + "|Имя cookie 2|")
    public void checkCookiesHeader(final ResolvableList cookieNames) {
        var webDriver = WebDriverRunner.getWebDriver();
        cookieNames.forEach((name) -> Optional.ofNullable(webDriver.manage().getCookieNamed(name))
                .ifPresentOrElse(cookie -> checkCookieName(cookie, name), () -> {
                    throw new ExecutionException("Not found cookie with name {}", name);
                }));
    }

    @When("удалить cookies :")
    @Sample("удалить cookies в сессии браузера")
    @Parameter(type = ":", name = "таблица из имени cookies")
    @Example(example = "удалить cookies :" + "|Имя cookie 1|" + "|Имя cookie 2|")
    public void deleteCookies(final List<String> cookiesNames) {
        var webDriver = WebDriverRunner.getWebDriver();
        cookiesNames.forEach(cookiesName -> webDriver.manage().deleteCookieNamed(cookiesName));
    }

    @When("удалить все cookies")
    @Sample("удалить все cookies в сессии браузера")
    public void deleteAllCookies() {
        WebDriverRunner.getWebDriver().manage().deleteAllCookies();
    }

    @When("открыть ссылку {resolvable_string} под учетными данными:")
    @Sample("Осуществить переход по ссылке используя Basic Authentication")
    @Parameter(type = "resolvable_string", name = "URL")
    @Parameter(type = ":", name = "таблица c учетными данными")
    @Example(
            example = "И открыть ссылку 'https://auth/letters' под учетными данными:"
                    + "| user                 | password                 | domain     |"
                    + "| ${{env:DOMAIN_USER}} | ${{env:DOMAIN_PASSWORD}} | DOMAIN |")
    public void openUrlViaBasicAuth(final String url, final ResolvableDataTable credentials) {
        openWithCredentials(url, credentials);
    }

    @When("открыть страницу {resolvable_string} под учетными данными:")
    @Sample("Осуществить переход на страницу используя Basic Authentication")
    @Parameter(type = "resolvable_string", name = "наименование страницы")
    @Parameter(type = ":", name = "таблица c учетными данными")
    @Example(
            example = "И открыть страницу 'Главная' под учетными данными:"
                    + "| user                 | password                 | domain     |"
                    + "| ${{env:DOMAIN_USER}} | ${{env:DOMAIN_PASSWORD}} | HEADOFFICE |")
    public void openPageViaBasicAuth(final String pageTitle, final ResolvableDataTable credentials) {
        String absoluteUrl = getPageUrl(pageTitle);
        openWithCredentials(absoluteUrl, credentials);
        pageManager.getPageByTitle(pageTitle);
    }

    @When("открыть страницу {resolvable_string}")
    @Sample("Осуществить переход на страницу")
    @Parameter(type = "resolvable_string", name = "наименование страницы")
    @Example(example = "И открыть страницу 'Главная'")
    public void openPageNew(final String pageTitle) {
        String absoluteUrl = getPageUrl(pageTitle);
        open(absoluteUrl);
        pageManager.getPageByTitle(pageTitle);
    }

    @When("нажать на {string}, чтобы скачать файл, записать путь в {string}")
    @Sample("скачать файл по нажатию на элемент и сохранить путь к нему в переменную")
    @Example(example = "нажать на 'Скачать', чтобы скачать файл, записать путь в 'pathToFile'")
    public void downloadFileOnAction(final String elementTitle, final String variableName) {
        final TypifiedWebElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        File file;
        try {
            // TODO remove filtering after update to selenide 6.12 (if possible due version date)
            // see https://ru.selenide.org/2023/02/24/selenide-6.12.0/
            // file = $(element).download(DEFAULT_DOWNLOAD_WAIT,  withNameMatching(Constants.FILTER_TEMP_FILE_REGEX));
            file = returnFileAfterAction(() -> element.click());
        } catch (Exception e) {
            throw new ElementInteractionException(
                    "No downloaded files on interaction with Element '{}':", e, elementTitle);
        }
        context.set(variableName, Constants.ABSOLUTE_FILE_PATH_PREFIX.concat(file.getAbsolutePath()));
    }

    @SneakyThrows
    public File returnFileAfterAction(Runnable action) {
        File downloadFolder = Path.of(TEMP_UI_FOLDER).toFile();
        if (!downloadFolder.isDirectory()) {
            throw new AssertionError("Ошибка сохранения файла: Некорректная дирректория сохранения.");
        }
        // Запоминаем количество файлов в папке до клика
        var filesBeforeClick = new ArrayList<>(Arrays.asList(downloadFolder.listFiles()));
        var filesBeforeClickCount = filesBeforeClick.size();
        // Кликаем на кнопку при которой происходит скачивание
        action.run();
        var fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.matches(FILTER_TEMP_FILE_REGEX);
            }
        };
        var count = 0;
        // Дожидаемся, что файлов в каталоге стало больше
        while (filesBeforeClickCount == downloadFolder.listFiles().length) {
            Selenide.sleep(1000);
            // TODO: добавить условие выхода из цикла
            count++;
            if (count > DEFAULT_DOWNLOAD_WAIT / 1000) {
                throw new AssertionError("Файлов в каталоге не стало больше");
            }
        }
        log.info(
                "Список файлов: {}",
                "\n"
                        + Arrays.stream(downloadFolder.listFiles())
                                .map(x -> x.getAbsolutePath())
                                .collect(Collectors.joining("\n")));
        count = 0;
        // Дожидаемся, что файлов в каталоге пропали временные файлы загрузки
        while (downloadFolder.list(fileNameFilter).length != 0) {
            Selenide.sleep(1000);
            // TODO: добавить условие выхода из цикла
            count++;
            if (count > DEFAULT_DOWNLOAD_WAIT / 1000) {
                throw new AssertionError("Временные файлы все еще в каталоге");
            }
        }
        log.info(
                "Список файлов: {}",
                "\n"
                        + Arrays.stream(downloadFolder.listFiles())
                                .map(x -> x.getAbsolutePath())
                                .collect(Collectors.joining("\n")));
        var filesAfterClick = new ArrayList<>(Arrays.asList(downloadFolder.listFiles()));
        filesAfterClick.removeAll(filesBeforeClick);
        var downloadFile = filesAfterClick.get(0);
        // Проверка возможно излишне, но пусть будет
        Assertions.assertThat(downloadFile.exists())
                .withFailMessage("Файл не существует по пути: " + downloadFile.getAbsolutePath())
                .isTrue();
        return downloadFile;
    }

    @When("установить разрешение браузера {int} x {int}")
    @Sample("Установить разрешение браузера")
    @Parameter(type = "int", name = "размер окна по горизонтали")
    @Parameter(type = "int", name = "размер окна по вертикали")
    @Example(example = "установить разрешение браузера 1024 x 768")
    public void setBrowserSize(final int x, final int y) {
        var window = WebDriverRunner.getWebDriver().manage().window();
        window.setPosition(new Point(0, 0));
        window.setSize(new Dimension(x, y));
    }

    @When("проверить, что разрешение браузера {int} x {int}")
    @Sample("Проверить что у браузера установлено соответствующее разрешение")
    @Parameter(type = "int", name = "размер окна по горизонтали")
    @Parameter(type = "int", name = "размер окна по вертикали")
    @Example(example = "проверить, что разрешение браузера 1024 x 768")
    public void checkBrowserSize(final int x, final int y) {
        Dimension actualSize = WebDriverRunner.getWebDriver().manage().window().getSize();
        var expectedSize = new Dimension(x, y);
        Validator.assertThat(
                actualSize.equals(expectedSize),
                "Actual browser actualSize '{}' doesn't match expected browser actualSize '{}'",
                actualSize,
                expectedSize);
    }

    @When("установить масштаб страницы браузера - {int}%")
    @Sample("установить масштаб страницы браузера")
    @Parameter(type = "int", name = "масштаб экрана браузера")
    @Example(example = "установить масштаб страницы браузера - 80%")
    public void setZoom(final int zoom) {
        executeJS("document.body.style.zoom='" + zoom + "%'");
    }

    @When("развернуть окно браузера на весь экран")
    @Sample("Развернуть окно браузера на весь экран")
    @Example(example = "развернуть окно браузера на весь экран")
    public void setBrowserFullSize() {
        WebDriverRunner.getWebDriver().manage().window().fullscreen();
    }

    @When("выполнить js-скрипт:$")
    @Sample("выполнить js-скрипт на странице")
    @Parameter(type = "$", name = "js-скрипт")
    @Example(example = "выполнить js-скрипт:" + "\"\"\"" + "console.log('Hello World!')" + "\"\"\"")
    public void executeJS(final String jsScript) {
        executeJavaScript(jsScript);
    }

    @When("проверить CSS-свойства элемента {resolvable_string}:")
    @Sample("Осуществить проверку CSS-свойства элемента")
    @Parameter(type = "resolvable_string", name = "имя элемента")
    @Parameter(type = ":", name = "таблица c сss данными")
    @Example(
            example = "проверить CSS-свойства элемента {resolvable_string}:"
                    + "| property1 | propertyValue1 |"
                    + "|property2|propertyValue2|")
    public void cssProperty(final String elementTitle, final ResolvableMap cssProperties) {
        final TypifiedWebElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
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

    private void checkCookie(final Cookie actual, final String expectedValue) {
        log.info("check cookie {}", actual.getName());
        Validator.assertThat(
                actual.getValue().equals(expectedValue),
                "Cookie {} not expected value:\n" + " expected : {} \n" + " actual : {}",
                actual.getName(),
                expectedValue,
                actual.getValue());
    }

    private void checkCookieName(final Cookie actual, final String expectedName) {
        log.info("check cookie name {}", actual.getName());
        Validator.assertThat(
                actual.getName().equals(expectedName),
                "Cookie name not expected:\n" + " expected : {} \n" + " actual : {}",
                expectedName,
                actual.getName());
    }

    private String getPageUrl(final String pageTitle) {
        String absoluteUrl;
        String pageUrl = null;
        try {
            var field = pageManager.getRegisteredPageClasses().get(pageTitle).getDeclaredField(URL_FIELD_NAME);
            pageUrl = placeholderResolver
                    .resolve(field.getAnnotation(URL.class).url())
                    .replace('\\', '/');
            String baseUrl = webDriversProperties.getStartingUrl();
            log.error(pageUrl);
            final var uri = new URI(pageUrl);
            absoluteUrl = uri.isAbsolute() ? pageUrl : baseUrl.concat("/").concat(pageUrl);
        } catch (URISyntaxException e) {
            log.warn(
                    "Incorrect format of URL declared on page: {}, class: {}, stacktrace {}",
                    pageTitle,
                    pageManager.getRegisteredPageClasses().get(pageTitle).getSimpleName(),
                    e.getMessage());
            absoluteUrl = pageUrl;
        } catch (NoSuchFieldException | NullPointerException npe) {
            throw new ConfigurationException(
                    URL_MESSAGE,
                    npe,
                    pageTitle,
                    pageManager.getRegisteredPageClasses().get(pageTitle).getSimpleName(),
                    URL_FIELD_NAME);
        }
        return absoluteUrl;
    }

    @When("проверить, что URL страницы {matcher} {resolvable_string}")
    @Sample("Проверить URL адрес страницы")
    @Parameter(type = "matcher", name = "матчер проверки")
    @Parameter(type = "resolvable_string", name = "ожидаемый URL страницы")
    @Example(example = "проверить, что URL страницы == 'https://yandex.ru/'")
    public void checkUrl(final MatcherName matcherName, final String expectedUrl) {
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertThat(currentUrl, getMatcher(matcherName, expectedUrl));
    }

    @When("проверить, что заголовок страницы {matcher} {resolvable_string}")
    @Sample("Проверить заголовок страницы")
    @Parameter(type = "matcher", name = "матчер проверки")
    @Parameter(type = "resolvable_string", name = "текст для проверки")
    @Example(example = "проверить, что заголовок страницы == 'Банковские карты'")
    public void checkTitle(final MatcherName matcherName, final String expectedTitle) {
        String currentTitle = WebDriverRunner.getWebDriver().getTitle();
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
        var webDriver = WebDriverRunner.getWebDriver();
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
        final TypifiedWebElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
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

    @When("вернуться в браузере на страницу {pageDirection}")
    @Sample("вернуться в браузере на страницу назад или вперёд")
    @Parameter(type = "pageDirection", name = "направление движения")
    @Example(example = "вернуться в браузере на страницу назад")
    public void pageNavigation(final String direction) {
        try {
            if (direction.equals("назад")) {
                back();
                return;
            }
            forward();
        } catch (Exception e) {
            throw new ExecutionException("No direction available with name '{}'", e, direction);
        }
    }

    @When("открыть в браузере новую вкладку")
    @Sample("открыть в браузере новую вкладку")
    @Example(example = "открыть в браузере новую вкладку")
    public void openNewPage() {
        var webDriver = WebDriverRunner.getWebDriver();
        String currentHandle = webDriver.getWindowHandle();
        try {
            // https://github.com/seleniumhq/selenium/issues/12025
            Selenide.switchTo().newWindow(WindowType.TAB);
        } catch (Exception ex) {
            executeJavaScript("window.open();");
        }
        Set<String> handles = webDriver.getWindowHandles();
        for (String actual : handles) {
            if (!actual.equalsIgnoreCase(currentHandle)) {
                webDriver.switchTo().window(actual);
            }
        }
    }

    @When("переключиться на вкладку {int}")
    @Sample("переключиться на другую вкладку в браузере")
    @Parameter(type = "int", name = "номер вкладки")
    @Example(example = "переключиться на вкладку 1")
    public void switchToTab(int tabNumber) {
        try {
            switchTo().window(tabNumber - 1);
        } catch (Exception e) {
            throw new ExecutionException("No window found with number '{}'", e, tabNumber);
        }
    }

    @When("закрыть текущую вкладку")
    @Sample("закрыть текущую вкладку")
    @Example(example = "закрыть текущую вкладку")
    public void closeCurrentTab() {
        closeWindow();
    }

    @Deprecated
    @When("изменить запрос {resolvable_string} на {resolvable_string}")
    @Sample("заменить запрос на заданный")
    @Example(example = "подставить в запрос новый url")
    public void mockRequest(final String requestUrl, final String newRequestUrl) {
        ChromeDriver driver = (ChromeDriver) WebDriverRunner.getWebDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
        devTools.addListener(Fetch.requestPaused(), requestPaused -> {
            if (requestUrl.equals(requestPaused.getRequest().getUrl())) {
                devTools.send(Fetch.continueRequest(
                        requestPaused.getRequestId(),
                        Optional.of(newRequestUrl),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
            } else {
                devTools.send(Fetch.continueRequest(
                        requestPaused.getRequestId(),
                        Optional.of(requestUrl),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
            }
        });
        devTools.close();
    }

    @Deprecated
    @When("подставить ответ на запрос {resolvable_string} с телом {resolvable_string}")
    @Sample("заменить тело ответа на запрос на заданное")
    @Example(example = "подставить в ответ на запрос свое body")
    public void mockResponse(final String requestUrl, final String jsonPath) {
        String responseBody = fileLoader.readFileAsString(jsonPath);
        AtomicBoolean complete = new AtomicBoolean(false);
        ChromeDriver driver = (ChromeDriver) WebDriverRunner.getWebDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
        devTools.addListener(Fetch.requestPaused(), requestPaused -> {
            if (requestUrl.equals(requestPaused.getRequest().getUrl())) {
                String sentBody = Base64.getEncoder().encodeToString(responseBody.getBytes());
                List<HeaderEntry> headerEntries = new ArrayList<>();
                headerEntries.add(new HeaderEntry("Access-Control-Allow-Origin", "*"));
                devTools.send(Fetch.fulfillRequest(
                        requestPaused.getRequestId(),
                        200,
                        Optional.of(headerEntries),
                        Optional.empty(),
                        Optional.of(sentBody),
                        Optional.of("No value present")));
                complete.set(true);
            } else {
                devTools.send(Fetch.continueRequest(
                        requestPaused.getRequestId(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
            }
        });
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(d -> complete.get());
        devTools.close();
    }
}
