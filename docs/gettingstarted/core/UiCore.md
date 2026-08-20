## Общие настройки

```properties
framework.ui.timeout - timeout взаимодействия с элементами
framework.ui.allure.screenShootingOnCore.enabled=false -- функционал добавления скриншотов в Allure отчет для публичных методов UiCore (таблицы / элементы)
framework.ui.allure.screenShootingOnAnnotation.enabled=false -- функционал добавления скриншотов в Allure отчет для методов c аннотацией @AddScreenshotOnStep и классов c аннотацией @AddScreenshotOnClass
```

## Конфигурирование Page

```Java
// название Page для использования в тестах
@PageEntry(title = "Горизонтальное меню")
// данный класс должен находится в package из переменной framework.ui.page.package и обязательно наследование от AbstractPage
public class HeaderMenuPage extends AbstractPage {

    // название элемента для использования в тестах
    @Element("Кнопка поиска")
    // локатор для поиска элемента 
    @FindBy(xpath = "//div[@class = 'header-search']/button")
    // Переменная, которая хранит в себе найденный элемент
    public Button searchButton;

    // название элемента для использования в тестах с использованием компонента screen-elements
    @Element("Строка поиска")
    // путь к скриншоту для поиска элемента и сдвиг по координатам относительно центра найденного элемента 
    @FindByScreen(location = "screenshots/searchTextInput.png", x = 15, y = 15)
    // переменная, которая хранит в себе найденный элемент
    public TextInput searchTextInput;

    // название элемента для использования в тестах с использованием компонента autoit-elements
    @Element("Строка поиска")
    // путь к скриншоту для поиска элемента и сдвиг по координатам относительно центра найденного элемента 
    @FindByControl(winTitle = "наименования окна", control = "уникальный контрол элемента для поиска")
    // переменная, которая хранит в себе найденный элемент
    public TextInput searchTextInput;

    //задать кастомное ожидание загрузки страницы (прелоадеры / кнопки / заголовки и т.д.)
    @Override
    public void checkAcceptor() {
        $(searchTextInput).shouldBe(Condition.visible);
    }
}
```

## TypifiedElement`ы

Во фреймворке релизованы наиболее часто встречающиеся элементы интерфейса. Каждый элемент может содержать дефолтные
методы взаимодействия, характерные только для данного типа элемента. Методы, предоставляющие доступ к элементу на
странице по соглашению реализуются во вложенном интерфейсе I[Action]. Реализованы следующие стандартные элементы
интерфейса страниц:

*HTML Elements*

- Button
- ClassicCheckBox
- IFrame
- Image
- Link
- Table
- TextBlock
- TextInput

*Desktop Elements*

- Button
- Calendar
- CheckBox
- ComboBox
- ComboBoxItem
- CoordinateElement
- DataGridView
- DataGridViewCell
- DataGridViewHeader
- DataGridViewHeaderItem
- DataGridViewRow
- DateTimePicker
- files.ps1
- files.txt
- Grid
- GridCell
- GridHeader
- GridHeaderItem
- GridRow
- HorizontalScrollBar
- Label
- ListBox
- ListBoxItem
- Menu
- MenuItem
- ProgressBar
- RadioButton
- ScrollBarBase
- Slider
- Spinner
- Tab
- TabItem
- TextBox
- Thumb
- TitleBar
- ToggleButton
- Tree
- TreeItem
- VerticalScrollBar
- Window

*Java Elements*

- Button
- CheckBox
- Label
- List
- RadioButton
- Tab
- TabItem
- Table
- TextInput

*Screen Elements*

- Button
- TextBox

```diff
- Использование TypifiedElements является основным взаимодейтсвием фреймворка с элементами
```

## Создание кастомного элемента

Чтобы создать свой кастомный элемент, нужно унаследоваться от TypifiedWebElement (или любого другого класса подходящего
по функционалу) и имплементировать нужные поведенческие интерфейсы из следующего списка доступных:

- IAccessible - для элементов у которых нужно проверять состояние (напр. видимость или активность)
- IReadable, IMultipleValueReadable - для элементов из которых можно прочесть данные
- IScrollable - для скролбаров и слайдеров
- ISourceable - для элементов имеющих внутреннюю ссылку на источник или цель
- IValueTypeable, IMultipleValueTypeable - для многофазных элементов (например чекбокс или чекбокс с частичным
  включением)
- IVerifiable, IMultipleValueVerifiable, - для предоставляющих возможность проверки значения
- IWritable - для элементов предполагающих заполнение
- ICleanable - для элементов предполагающих очистку
- ISelectable, IMultipleValueSelectable - для создания типизированных элементов предполагающих возможность выбора
  значений в элементе/списке

**_Префикс MultipleValue_** - для взаимодействия с элементами, содержащими множественные значения
([ComboBox](../../../components/desktop-junit/src/main/java/ru/psb/autotestframework/desktop_elements/elements/ComboBox.java)
или MultiSelectBox)

```Java
public class CustomElement extends TypifiedElement implements IReadable, IWritable, IAccessible, ICleanable {
    public CustomElement(WebElement wrappedElement, String title) {
        super(wrappedElement, title);
    }

    @Override
    String readValue() {
        return "";
    }
    //...
}
```

## Работа с таблицами

Элемент-таблица инициализируется на странице следующим образом:

```Java

@PageEntry(title = "Горизонтальное меню")
public class HeaderMenuPage extends AbstractPage {
    @Element("Таблица")
    @FindBy(xpath = "//table-basic-example")
    @FindCellsBy(xpath = ".//td")
    @FindHeadersBy(xpath = ".//th")
    public WebTable table;
}
```

Если требуется создать свою кастомную таблицу с уникальными методами, то нужно создать свой класс и унаследоваться от
класса WebTable. 
Методы WebTable можно переопределять с помощью аннотации @Override

```Java
public class CustomTable extends WebTable {

    public CustomTable(WebElement element, String title) {
        super(element, title);
    }

    @Override
    void method(){
        //...
    }
}
```

Относительно других элементов добавлено две
новые [аннотации](../../../core/ui-core/src/main/java/ru/psb/autotestframework/ui_core/services/table_service):

- FindCellsBy - xpath до ячейки таблицы
- FindHeadersBy - xpath до заголовка таблицы

Основные шаги представлены в
классе [UiTableStepDefs](../../../core/cucumber-ui-core/src/main/java/ru/psb/autotestframework/cucumber/step_defs/UiTableStepDefs.java)

* Настройка для включения кеширования таблиц `framework.ui.aspects.tableCache.enabled:false`,при необходимости
  кеширования таблиц проставить true



## Подключение интеграции Allure <-> Selenide

//TODO Можно реализовать в UICore

Для интеграции Перед запуском всех тестов можно сконфигурировать интеграцию

1. Добавить зависимость

```Groovy
implementation 'io.qameta.allure:allure-selenide:2.17.3' //последняя доступная
```

2. Добавить следующую логику: 
   а) в BeforeAll и/или аналогичных (JUnit/Cucumber)
   б) имплементируя Spring-овый Interface InitializingBean

```Java
@Component
public class SelenideAllureInit implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)); // и т.д.
    }
};
```

## Работа с несколькими модулями одновременно

Ввиду проблематики выделения логики одновременной работы драйверов. Необходимо выполнить следующие пункты дял 
возможности использования нескольких драйверов одновременно

1. Подключить зависимости
2. Добавить properties.
3. framework.ui.driver.web.driverInit=false (для отключения запуска браузера при каждом тесте)
4. Добавить актуальный chrome/yandex driver-а в проект 
5. добавить файл с пропертями, с которыми будет запускаться web-приложение ([framework-web.properties](../components/front/WebElements.md)).
6. connectToRunningApp=true для десктопа важно проставить возможность переподключения к драйверу в пропертях
Note! Внимание - некоторые проперти являются общими для модулей (Ui.timeout и т.д.)
7. Добавить шаги на переключение контекста драйвера
