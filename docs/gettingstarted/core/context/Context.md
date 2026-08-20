# Контекст

Для хранения значений контекста Теста используется
класс [Context](../../../../core/spring-core/src/main/java/ru/psb/autotestframework/core/context/ContextImpl.java)

---
Позволяет хранить Object-ы (автоматически обрабатывает String)

## Окружение

В рамках feature-файла доступны контекстные переменные из [конфигурации проекта](../../additionaloptions/Environment.md)

Пример использования:

```gherkin
Дано адрес сервера '${{serviceUrl.api}}'
``` 

---

## [Плейсхолдеры](Placeholder.md)

Контекстные переменные в тексте feature-файлов обозначаются как `${{contextVariable}}`
. [Правила наименования](../../project_requirements/NamingConventionCodeStyle.md)

```gherkin
#language:ru
Дано установить переменные:
| oldString  | old |

И установить переменные:
| newString  | ${{oldString}}NewString |

Тогда переменные имеют значение:
| newString  | == | oldNewString |
``` 

Можно использовать вложенные контекстные функции. Пример для экранирования:
```gherkin
#language:ru
Дано установить переменные:
| name | ОАО "Рога и копыта"   |
| var1 | ${{escape:${{name}}}} |
Тогда переменные имеют значения:
| var1 | == | ОАО \"Рога и копыта\" |
``` 

## Маскирование контекстных переменных в Allure отчете

Для обеспечения размаскирования/маскирования контекстных переменных при сохранении маскирования паролей необходимо
1. Подключить плагин
```groovy
dependencies{
   testImplementation "ru.autotestframework.plugins:allure-extension:${VERSION}"
}
```
2. установить параметры в framework.properties в необходимые значения
```properties
framework.step.metainfo.enabled=true
framework.unmasking.enabled=true
```
3. Для паролей и других секретов, которые должны остаться маскированными необходимо использовать контекстную функцию ${{secret:value}}
   И тестим таблицу:
   | param 1 | ${{secret:${{param1}}}} |

```gherkin
language:ru
И тестим таблицу:
| param 1 | ${{secret:${{param1}}}} |
```

Note. Секретные переменные лучше не использовать в feature-файле (для логики авторизации создавать отдельный Шаг), 
но если необходимо - лучше через dataTable(не раскрывает параметры в именовании скринов) при unmasking = true;