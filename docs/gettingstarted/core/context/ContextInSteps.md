# Взаимодействие с контекстом в шагах

```Java
public class CommonSteps {
    private final Context context;

    public void saveGetContext(String key, String value) {
        String oldString = context.get(key);
        context.set(key, value);
    }
    @When("сохранить в {resolvable_string} значение {resolvable_string}")
    public void saveGetContext2(String key, String value) {
        String oldString = context.get(key);
        context.set(key, value);
    }

    @When("сохранить в {resolvable_string} значение {resolvable_string}")
    public void saveGetContext2(String key, String value) {
        String oldString = context.get(key);
        context.set(key, value);
    }
    
}
``` 

## Обработка контекста

Для процессинга строк, списков строк, словарей (Map), таблиц, содержащих контекстные переменные на уровне feature-файла
в `StepDef`, необходимо использовать готовые классы, выполняющие работу с контекстными переменными на уровне
feature-файла:

| Тип         | Класс                 |
|-------------|-----------------------|
| `String`    | `ResolvableString`    |
| `List`      | `ResolvableList`      |
| `Map`       | `ResolvableMap`       | 
| `DataTable` | `ResolvableDataTable` |

[Полный список](../../../../core/cucumber-core/src/main/java/ru/psb/autotestframework/cucumber/type)

```Java
public class CommonSteps {
   //...
   public void findAndFillWithText(ResolvableMap variables) {
       //...
   }
}
```    

Для создания собственных типов переменных используйте [инструкцию](ContextCustomVariable.md)
Для обработки объектов из контекста в шагах можно воспользоваться следующей логикой
---

```Java   
    @When("обработать {object}") 
    public void doSmth(Path obj)  //работает автокаст к соотв указанному типу при получении объекта из контекста
    {
        // do smth
    }
```

Или же 
```Java   
    @When("обработать Word {string}")//resolvable_string 
    public void doSmth(String varName)  //работает автокаст к соотв типу при получении объекта из контекста
    {
        Word word = (Word) context.get(varName);
        //...
    }
```
}


## Сохранение контекста

В случаях, когда необходимо передавать переменные контекста и временные файлы между сценариями в одном feature-файле,
предусмотрен тэг `@SaveContext`, который устанавливается над функцией:

```gherkin
#language:ru
@SaveContext
Функция: Почтовый клиент. Письма
```