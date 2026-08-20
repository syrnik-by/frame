**Для добавления spotless требуется в свой build.gradle добавить**

1. Добавить "Плагин"  
``` 
plugins {
    id 'com.diffplug.spotless' version "${spotlessVersion}"
}
``` 
2. Добавить "Конфигурацию"
``` 
    spotless {
        java {
            target fileTree('.') {
                include '**/*.java'
                exclude '**/build/**', '**/build-*/**'
            }
            toggleOffOn()
            palantirJavaFormat()
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }

        json {
            target fileTree('.') {
                include '**/*.json'
                exclude '**/build/**', '**/build-*/**'
            }
            gson()
                    .indentWithSpaces(4)
                    .sortByKeys()
                    .escapeHtml()
        }

        sql {
            target fileTree('.') {
                include '**/*.sql'
                exclude '**/build/**', '**/build-*/**'
            }
            dbeaver()
        }
    }
``` 
Конфигурацию можно редактировать под нужды проекта, и подключить соотв. скрипты подробнее https://github.com/diffplug/spotless

В фреймворке используется сейчас spotlessVersion=6.3.0

После того как все будет добавлено корректно при reload gradle в списке задач будут отображаться задачи:

**spotlessApply** - при выполнении таски реформатит ваш код под шаблон 

**spotlessCheck** - выбрасывает в лог все несовпадения в классах по шаблону 

**spitlessDiagnose** - техническая таска проверки корректности подключения spotless