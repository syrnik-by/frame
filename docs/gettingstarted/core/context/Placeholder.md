# Плейсхолдеры

В фрейморке поддерживаются плейсхолдеры вида `${{placeholder}}`  
[Правила наименования](../../project_requirements/NamingConventionCodeStyle.md)

### Где можно использовать плейсхолдеры?

- В любых строковых частях шага:

```gherkin
#language:ru
Дано адрес сервера '${{service.url}}'
И ответ содержит XML из файла 'data/demo/${{fileName}}.xml'
И сформирован SQL запрос:
"""
SELECT * FROM some_db_query where msg_id = '${{messageId}}'
"""
```

- В любых текстовых файлах(txt,json,xml и т.д). Например, файл request.xml:

```xml
<soap:Body>
<Divide xmlns="https://tempuri.org/">
<intA>${{intA}}</intA>
<intB>5</intB>
</Divide>
</soap:Body>
```

---

### Виды плейсхолдеров:

- `${{prefix.varName}}` - замена на переменную контекста

- `${{functionName:arg}}` - замена на значение, полученое [функцией контекста](ContextFunction.md)

---

### Что такое переменные контекста?

Переменные контекста - объектные переменные, которые можно использовать для хранения настроек и контекстуальных значений теста (файлы, объекты, строки).   
Установить переменные контекста можно с помощью:

- настроек фрейморка установив параметр `framework.variables.varName`:

  - в файле `framework.properties` (переменная `varName` будет доступна всегда)
  - в файле `framework-test.properties` (переменная `varName` будет доступна только для окружения test)
  - с помощью переменных среды (переменная `varName` будет доступна всегда)

- во время исполнения сценария специальными шагами (переменная `varName` будет доступна только для сценария в котором
  она была установлена)

```gherkin
#language:ru
Когда установлены переменные:
| varName  | 15              |
| varName2 | ${{regex:/d/d}} |
  
Тогда переменные получены из ответа на запрос:
| varName  | Envelope.Body.DivideResponse.DivideResult.text() |
  
Тогда переменные получены из sql ответа:
| varName  | classified |
```

```Java
private final Context context;

public void saveContext(String key, String value) {
  context.set(key, value);
}
```

---

### Использование дефолтных значений в плейсхолдерах

При использовании плейсхолдера, можно сразу задать дефолтное значение:   
Шаблон - `${{prefix.varName:-defaultValue}}`   
Пример - `${{request.read.timeout:-1000}}`

Если переменная контекста `request.read.timeout` не будет задана, то будет использоваться значение `1000`

---

### Плейсхолдеры могут быть вложенными

- `${{service.${{anyOtherVar}}}}`
- `${{base64Decoder:${{service.encodedPasswod}}}}`

Пример использования:
```gherkin
#language:ru
Дано установить переменные:
| text    | Hello, world!                                   |
| encoder | ${{base64Encoder:${{text}}}}                    |
| decoder | ${{base64Decoder:${{base64Encoder:${{text}}}}}} |
Тогда переменные имеют значения:
| encoder | == | SGVsbG8sIHdvcmxkIQ== |
| decoder | == | Hello, world!        |
``` 
---

### Автоматический резолв переменных

Автоматический резолв переменных осуществляется в резолв-типах переменных.   
Если есть необходимость в [написании собственных типов переменных](ContextCustomVariable.md), то необходимо
реализовать в них интерфейс `IResolvable` и метод `resolve()`.   
