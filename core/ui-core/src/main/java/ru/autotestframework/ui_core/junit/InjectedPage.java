package ru.autotestframework.ui_core.junit;

import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;
import static org.assertj.core.api.Assertions.assertThat;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.ElementFactory;
import ru.autotestframework.ui_core.services.ScreenshotRegistry;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;
import ru.autotestframework.util.StringUtil;

/**
 * Injected page.
 *
 * @param <T> the type parameter
 */
public abstract class InjectedPage<T> extends AbstractPage {
    @Getter
    @Setter
    private DriverContainer driverContainer;

    /**
     * Instantiates a new Injected page.
     */
    public InjectedPage() {
        if (hasWebDriverStarted()) {
            init();
        }
    }

    /**
     * Init t.
     *
     * @return the t
     */
    public T init() {
        T page = (T) ElementFactory.initElements(this);
        ScreenshotRegistry.registerPage(this);
        return page;
    }

    /**
     * Go to b.
     *
     * @param <B>       the type parameter
     * @param pageClass the page class
     * @return the b
     */
    @SneakyThrows
    public <B extends InjectedPage> B goTo(Class<B> pageClass) {
        var page = (B) pageClass.getDeclaredConstructor().newInstance().init();
        initStep(page.getTitle());
        return page;
    }

    /**
     * Init step.
     *
     * @param pageName the page name
     */
    @Step("Инициализировать страницу {pageName}")
    public void initStep(String pageName) {}

    /**
     * Fill field t.
     *
     * @param elementName the element name
     * @param value       the value
     * @return the t
     */
    @Step("Заполнить поле {elementName} значением {value}")
    public T fillField(String elementName, String value) {
        getElementByTitle(elementName).clear();
        ((IWritable) getElementByTitle(elementName)).write(value);
        return (T) this;
    }

    /**
     * Append field t.
     *
     * @param element the element
     * @param value   the value
     * @return the t
     */
    public T appendField(IElement element, String value) {
        return appendField(element.getTitle(), value);
    }

    /**
     * Click t.
     *
     * @param element the element
     * @return the t
     */
    public T click(IElement element) {
        return click(element.getTitle());
    }

    /**
     * Fill field t.
     *
     * @param element the element
     * @param value   the value
     * @return the t
     */
    public T fillField(IElement element, String value) {
        return fillField(element.getTitle(), value);
    }

    /**
     * Append field t.
     *
     * @param elementName the element name
     * @param value       the value
     * @return the t
     */
    @Step("Дополнить поле {elementName} значением {value}")
    public T appendField(String elementName, String value) {
        ((IWritable) getElementByTitle(elementName)).append(value);
        return (T) this;
    }

    /**
     * Click t.
     *
     * @param elementName the element name
     * @return the t
     */
    @Step("Нажать на элемент {elementName}")
    public T click(String elementName) {
        getElementByTitle(elementName).click();
        return (T) this;
    }

    /**
     * Hover t.
     *
     * @param elementName the element name
     * @return the t
     */
    @Step("Установить фокус на элемент {elementName}")
    public T hover(String elementName) {
        getElementByTitle(elementName).hover();
        return (T) this;
    }

    /**
     * Clear t.
     *
     * @param element the element
     * @return the t
     */
    @Step("Очистить поле {elementName}")
    public T clear(IElement element) {
        return clear(element.getTitle());
    }

    /**
     * Clear t.
     *
     * @param elementName the element name
     * @return the t
     */
    @Step("Очистить поле {elementName}")
    public T clear(String elementName) {
        getElementByTitle(elementName).clear();
        return (T) this;
    }

    /**
     * Check condition t.
     *
     * @param elementName the element name
     * @param condition   the condition
     * @return the t
     */
    @Step("Проверить состояние элемента {elementName}: {condition}")
    public T checkCondition(String elementName, Condition condition) {
        getElementByTitle(elementName).shouldBe(condition, true);
        return (T) this;
    }

    /**
     * Check attribute t.
     *
     * @param elementName   the element name
     * @param attributeName the attribute name
     * @return the t
     */
    @Step("Проверить у элемента {elementName} наличие атрибута {attributeName}")
    public T checkAttribute(String elementName, String attributeName) {
        getElementByTitle(elementName).shouldBe(Condition.attribute(attributeName), true);
        return (T) this;
    }

    /**
     * Check attribute t.
     *
     * @param elementName   the element name
     * @param attributeName the attribute name
     * @param state         the state
     * @return the t
     */
    @Step("Проверить у элемента {elementName} атрибута {attributeName} значение: {state}")
    public T checkAttribute(String elementName, String attributeName, String state) {
        getElementByTitle(elementName).shouldBe(Condition.attribute(attributeName, state), true);
        return (T) this;
    }

    /**
     * Right click t.
     *
     * @param elementName the element name
     * @return the t
     */
    @Step("Нажать на элемент {elementName} правой кнопкой мыши")
    public T rightClick(String elementName) {
        getElementByTitle(elementName).rightClick();
        return (T) this;
    }

    /**
     * Double click t.
     *
     * @param elementName the element name
     * @return the t
     */
    @Step("Нажать на элемент {elementName} дважды")
    public T doubleClick(String elementName) {
        getElementByTitle(elementName).doubleClick();
        return (T) this;
    }

    /**
     * Select in element t.
     *
     * @param elementName the element name
     * @param value       the value
     * @return the t
     */
    @Step("Выбрать в элементе {elementName} значение {value}")
    public T selectInElement(String elementName, String value) {
        ((ISelectable) getElementByTitle(elementName)).select(value);
        return (T) this;
    }

    /**
     * Check value t.
     *
     * @param elementName the element name
     * @param value       the value
     * @return the t
     */
    @Step("Проверить заполнение поля {elementName} значением {value}")
    public T checkValue(String elementName, String value) {
        var presentedValue = ((IReadable) getElementByTitle(elementName)).readValue();
        assertThat(presentedValue)
                .withFailMessage(
                        StringUtil.format("Field '{}' actual value is '{}' but expected '{}'"),
                        elementName,
                        presentedValue,
                        value)
                .isEqualTo(value);
        return (T) this;
    }

    /**
     * Read value string.
     *
     * @param elementName the element name
     * @return the string
     */
    @Step("Прочитать значения поля {elementName}")
    public String readValue(String elementName) {
        final IReadable element = getElement(elementName, IReadable.class);
        return element.readValue();
    }
}
