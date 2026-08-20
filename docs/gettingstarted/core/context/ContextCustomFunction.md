# Создание своих функций контекста

1. Создайте класс реализующий интерфейс `ContextFunctionsSupplier.java` и пометьте его аннотацией `@Component`
2. Создайте необходимые функции в классе, реализуя интерфейс `StringLookup.java`
3. Положите все функции в map'у и верните их в `ContextFunctionsSupplier`

Пример:

```java
@Component
public class CustomContextFunctionsSupplier implements ContextFunctionsSupplier {

    @Override
    public HashMap<String, StringLookup> get() {
        var functions = new HashMap<String, StringLookup>();

        var customFunction1 = new StringLookup() {
            @Override
            public String lookup(String arg) {
                //some code here
                return null;
            }
        };

        var customFunction2 = new StringLookup() {
            @Override
            public String lookup(String arg) {
                //some code here
                return null;
            }
        };

        functions.put("customFunction1", customFunction1);
        functions.put("customFunction2", customFunction2);

        return functions;
    }
}
```