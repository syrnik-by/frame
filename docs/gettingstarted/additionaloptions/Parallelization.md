## Параллелизация тестов на cucumber проектах

Для настройки возможности запуска автотестов в несколько потоков необходимо:

Прописать в файле build.gradle в таску, которая у Вас отвечает за запуск автотестов опредленные строки

Например:
```groovy
task executeFeatures(type: Test) {

//#Это нужно добавить чтобы узнать максимально возможное количество форков на конкретной машине**

    maxParallelForks = Runtime.getRuntime().availableProcessors()
    println("Setting maxParallelForks to $maxParallelForks")

//#Это нужно добавить чтобы активировать параллельный запуск**
    
    systemProperties["cucumber.execution.parallel.enabled"]= true
    systemProperties["cucumber.execution.parallel.mode.default"]= "concurrent"
    systemProperties["cucumber.execution.parallel.config.strategy"]= "fixed"
    systemProperties["cucumber.execution.parallel.config.fixed.parallelism"]= "4"

//#Конец добавляемого контента
    
    def infoMessageTemplate = '''\n=== Running %s tests with profile: '%s' and tags: '%s' =====\n'''
    def envProfile = findProperty('stand').toString().toLowerCase()
    def skippedTags = 'not (@Skip or @Demo)'
    def tags = project.hasProperty('tags') ? findProperty('tags') + ' and ' + skippedTags : skippedTags
    def isTmsEnabled = Boolean.parseBoolean(System.getenv("USE_TMS"))
    doFirst {
        println String.format(infoMessageTemplate, project, envProfile, tags)
        environment "SPRING_PROFILES_ACTIVE", envProfile
        environment "CUCUMBER_FILTER_TAGS", tags
        if (isTmsEnabled) {
           println '=== Enabled sending results in TMS ==='
           environment "CUCUMBER_PLUGIN", TEST_IT_PLUGIN
        }
    }
}

```