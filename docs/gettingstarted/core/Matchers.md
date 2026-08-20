# Валидация. Операторы сравнения. Примеры использования

Во фреймворке используются валидаторы из hamcrest matchers или созданные на его основе
см. [Hamcrest](https://hamcrest.org/JavaHamcrest/tutorial)

## Список операторов

Все операторы и типы можно посмотреть в пакете [cucumber.parser](../../../core/cucumber-core/src/main/java/ru/psb/autotestframework/cucumber/parser),
а также в пакете [cucumber.parser](../../../core/spring-core/src/main/java/ru/psb/autotestframework/core/matcher)

### Cтроковые операторы

- `contains`
- `!contains`
- `notBlank` - не пустой
- `isBlank` - пустой (не null и не пробельные символы)
- `matchesRegex` - соответсвтие регулярному выражению
- `notNull`
- `isNull`
- `==` _*(вовзращает false для аргументов 1.0 и 1). см. bigDecimalEquals_
- `!=`

### Другие операторы

#### Числовые

- `>`
- `>=`
- `<`
- `<=`
- `bigDecimalEquals`* - сравнивает одни и те же матемаические значения с разной точностью
- `bigDecimalCloseTo`* - сравнивает числа в пределах установленной точности знаков после запятой (проперти
  framework.matcher.decimal.scale)
  позволяет сравнивать числа с определенной погрешностью

*_Для применения в рамках модуля
см. [RestAssured](/docs/gettingstarted/components/back/HttpSteps.md)

#### Операторы не используемые в feature-файлах

- IsEqualJson
- IsEqualXml
- IsEqualFile

_Используются для сравнения соответствующих форматов данных_

## Применение

### В Feature-файлах

- Общие шаги

```gherkin
#language:ru
И переменные имеют значение: #Примернимы только строковые мачтеры
| source-guid | notNull | |
```

- sql-steps

```gherkin
#language:ru
И кол-во записей в ответе {matcher} {int}
И кол-во записей в ответе == 3
```

- http-steps

```gherkin
#language:ru
И ответ содержит body:
| amount | {matcher} | {amount}

И ответ содержит body:
| amount | bigDecimalEquals | ${{amount}}
```

### Java

- Валидация ответа Rest-Assured

```
response.then().assertThat().body(isEqualJson(expectedJson, JSONCompareMode.LENIENT));
```

- Пример сравнения файлов

```
var actual = new File(actualFilePath);
var expected = new File(expectedFilePath);
assertThat(actual, isEqualFile(expected), "Файлы отличаются");
```

### Свои матчеры
Для добавления своих матчеров, чтобы их можно было использовать в core необходимо выполнить следующее:

1. Создать класс имплементирующий интерфейс IMatcher
```Java
public class CustomMatcher implements IMatcher {
    @Override
    public Matcher createMatcher(Object value) {
        return greaterThan(Integer.valueOf((String) value));
    }
}
```

2. Зарегистрировать matcher-ы (Необходимо добавить имя матчера в формате соответствующем регулярному выражению #[a-zA-Z]{4,}#)
```Java
public class Hooks {

    @BeforeAll
    public static void customMatcher() {
        MatcherParser.getMatchers().putAll(Map.of("#custom#", new CustomMatcher(), "#notEqual#", new CustomMatcher2()));
    }
}
```