# Модуль Document-Steps

Модуль Document-Steps содержит в себе шаги для работы с Json, Excel, Word и Pdf .

### Cостав модуля:

- ExcelSteps - шаги для работы с Excel
- PdfSteps - шаги для работы с Pdf
- json_service - пакет реализации сравнения json'ов
- WordSteps - шаги для работы с Word
- document_service - пакет реализации генерации документов по шаблону и сервисов обработки документов

### Подключение модуля

- Добавляем зависимость:

```
implementation "ru.autotestframework.components:document-steps:${core_version}"
```

### Пример Cucumber-шагов

```gherkin
#language:ru
Сценарий: Сохранение текста из Word документа
И записать текст документа Word из пути 'src/test/resources/doc.docx' в переменную 'Текст документа'

Сценарий: Сохранение значения ячейки документа в контекст
И считать значение Excel 'src/test/resources/table.xlsx' листа 'Таблица' в строке 1 и колонке 1 и записать в переменную 'ExcelVar'

Сценарий: Сохранение текста из Pdf документа
И считать текст PDF 'src/test/resources/text.pdf' со страницы 2 в переменную 'pdfText'

```

### Пример использования JsonComparator
```java
    public JsonNode compareTwoJsons(String json1, String json2) {
        JsonNode compare = JsonComparator.compare(json1,json2);
        compare.size();
        compare.toString();
    }
```

Все шаги представлены в
классах:
[ExcelSteps](../../../../components/document-steps/src/main/java/ru/autotestframework/document_steps/ExcelSteps.java)
[PdcSteps](../../../../components/document-steps/src/main/java/ru/autotestframework/document_steps/PdfSteps.java)
[WordSteps](../../../../components/document-steps/src/main/java/ru/autotestframework/document_steps/WordSteps.java)