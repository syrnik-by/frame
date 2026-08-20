# Создание своих типов переменных контекста

1. Создать класс с новым типом. В нем реализовать интерфейс `IResolvable`, воспользовавшись функционалом класса
   `PlaceHolderResolverImpl`. Пример:

```Java
public class NewResolvableType implements IResolvable {

   private final PlaceholderResolver placeholderResolver;

   @Override
   public NewResolvableType resolve(NewResolvableType object) {
      return new NewResolvableType(placeholderResolver.resolve(object), placeholderResolver);
   }
}
```

2. Добавить метод с описанием преобразования в нужный тип для Cucumber. Пример:

```Java
@DataTableType
public NewResolvableType parseResolvableMap(DataTable unresolvableDataTable) {
        return new NewResolvableType(unresolvableDataTable,placeholderResolver);
        }
```
