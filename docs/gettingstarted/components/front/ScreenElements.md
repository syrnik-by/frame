# Модуль Screen-elements

Модуль Screen-elements содержит в себе элементы и необходимый функционал для автоматизации тестирования любых труднотестируемых приложений при помощи
изображений (скриншотов). В данный момент используется SikuliX представляющий из себя драйвер взаимодействия с тестируемым приложением посредством
изображений (скриншотов).

Особенность компонента в том, что он может работать в связке с любым другим компонентом фреймворка для возможности взаимодействия с труднодоступными
частями приложения.

### Cостав модуля:

- configuration - работа с доступными проперти и конфигами
- elements - реализованные элементы
- stepdefs - реализация уникальных относительно других модулей шагов

### Заполнение проперти:

```properties
framework.ui.driver.screen.properties.path - путь до проперти-файла с настройками драйвера
```

### Проперти файл с настройками драйвера:

```properties
app - путь до тестируемого приложения
```

### Распознавание текста
Необходимо для работы TypifiedScreenTextElement (весь функционал)
И TypifiedScreenElement (get text)

Для получение текста с UI в модуле и для поиска элементов по тексту используются
[Средства OCR](https://ru.wikipedia.org/wiki/Оптическое_распознавание_символов)
Стандартным языком в Sikuli библиотеке выступает английский, длz переопределения и добавления значений необходимо 
выполнить следующие пункты

1. Добавить новый ресурс с
   данными ([RU](https://gitlab-01/automated-testing-technology/projects/psb-retail/psb_retail_autotest/-/tree/master/src/main/resources/tesseract/tessdata))

2. Задать в @BeforeAll (или любым другим способом на старте приложения) необходимые настрйоки например

```Java 
    Settings.OcrLanguage = "rus";
    Settings.OcrDataPath = this.getClass()
        .getClassLoader().getResource("tesseract")
        .getFile(); //путь до ресурса
    OCR.globalOptions().dataPath(this.getClass()
        .getClassLoader().getResource("tesseract")
        .getFile());
    OCR.globalOptions().language(Settings.OcrLanguage);
    OCR.globalOptions().psm(6);
```