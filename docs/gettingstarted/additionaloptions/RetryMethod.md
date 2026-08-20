# Перезапуск упавших методов

Во фреймворке имеется возможность аннотировать методы (или степы) аннотацией @WithRetries.
В аннотации указывается значение - количество перезапусков.

### Использование
Для использования добавить аспект WithRetriesAspect в ваш aop-ajc.xml (resources/META-INF/)
Пример:
```xml
<aspectj>
    <aspects>
        <aspect name="ru.autotestframework.util.retry.WithRetriesAspect"/>
    </aspects>
</aspectj>
```