package ru.autotestframework.sql_steps;

import static java.util.stream.Collectors.toList;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.sql.DataSource;
import jdk.jfr.Description;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.sql_steps.components.*;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.util.Validator;

@Data
@Slf4j
@RequiredArgsConstructor
@Description("SQL")
public class SqlSteps {

    private final Context context;
    private final PlaceholderResolver placeholderResolver;
    private final FileLoaderImpl fileLoader;
    private final DBContextContainer dbContextContainer;

    @Autowired
    private ConnectionPoolContainer connectionPoolContainer;

    public void setSqlQuery(final String sqlQuery) {
        dbContextContainer.setSqlQuery(sqlQuery);
    }

    public void setDataSource(final DataSource dataSource) {
        dbContextContainer.setDataSource(dataSource);
    }

    public void setActualRecords(final List<Map<String, Object>> actualRecords) {
        dbContextContainer.setActualRecords(actualRecords);
    }

    public void setDbmsLogs(final List<String> dbmsLogs) {
        dbContextContainer.setDbmsLogs(dbmsLogs);
    }

    @Deprecated
    public String getSqlQuery() {
        return dbContextContainer.getSqlQuery();
    }

    @Deprecated
    public DataSource getDataSource() {
        return dbContextContainer.getDataSource();
    }

    @Deprecated
    public List<Map<String, Object>> getActualRecords() {
        return dbContextContainer.getActualRecords();
    }

    @Deprecated
    public List<String> getDbmsLogs() {
        return dbContextContainer.getDbmsLogs();
    }

    @When("установить подключение к БД:")
    @Sample("Установить подключение к БД по параметрам")
    @Parameter(type = ":", name = "список параметров")
    @Example(
            example = "И установить подключение к БД:"
                    + "| url    | user    | password    | driver                                       |"
                    + "| db.url | db.user | db.password | com.microsoft.sqlserver.jdbc.SQLServerDriver |")
    public void getConnectionToDb(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    @When("создать коннектор c {word} к БД:")
    @Sample("Установить подключение к БД по параметрам и добавить его в пул подключений")
    @Parameter(type = "word", name = "имя подключения в пуле")
    @Parameter(type = ":", name = "список параметров")
    @Example(
            example = "И создать коннектор c 'SQLServer' к БД"
                    + "| url    | user    | password    | driver                                       |"
                    + "| db.url | db.user | db.password | com.microsoft.sqlserver.jdbc.SQLServerDriver |")
    public void createConnection(final String connectionName, final DataSource dataSource) {
        connectionPoolContainer.createConnection(connectionName, dataSource);
    }

    @When("отправить в БД коннектором {word} методом {sql_method} SQL запрос из файла {path}")
    @Sample("Отправить SQL запрос в БД из файла")
    @Parameter(type = "word", name = "имя подключения в пуле")
    @Parameter(type = "sql_method", name = "метод отправки запроса")
    @Parameter(type = "path", name = "путь к файлу с запросом")
    @Example(
            example =
                    "И отправить в БД коннектором 'SQLServer' методом SELECT SQL запрос из файла 'data/sql/select.txt'")
    public void executeQueryFromFileByConnection(
            final String connectorName, final SqlMethod method, final String sqlFilePath) {
        dbContextContainer.setSqlQuery(fileLoader.readFileAsString(sqlFilePath));
        innerExecuteQueryByConnection(connectionPoolContainer.getConnectionByName(connectorName), method);
    }

    @When("отправить в БД коннектором {word} методом {sql_method} SQL запрос:")
    @Sample("Отправить SQL запрос в БД")
    @Parameter(type = "word", name = "имя подключения в пуле")
    @Parameter(type = "sql_method", name = "метод отправки запроса")
    @Example(
            example =
                    "И отправить в БД коннектором 'SQLServer' методом SELECT SQL запрос:" + "\"\"\"" + "sql" + "\"\"\"")
    public void executeQueryByConnection(final String connectorName, final SqlMethod method, final String query) {
        dbContextContainer.setSqlQuery(query);
        innerExecuteQueryByConnection(connectionPoolContainer.getConnectionByName(connectorName), method);
    }

    @When("отправить в БД методом {sql_method} SQL запрос из файла {path}")
    @Sample("Отправить SQL запрос в БД из файла")
    @Parameter(type = "sql_method", name = "метод отправки запроса")
    @Parameter(type = "path", name = "путь к файлу с запросом")
    @Example(example = "И отправить в БД методом SELECT SQL запрос из файла 'data/sql/select.txt'")
    public void executeQueryFromFile(final SqlMethod method, final String sqlFilePath) {
        dbContextContainer.setSqlQuery(fileLoader.readFileAsString(sqlFilePath));
        innerExecuteQuery(method);
    }

    @When("отправить в БД методом {sql_method} SQL запрос:")
    @Sample("Отправить SQL запрос в БД")
    @Parameter(type = "sql_method", name = "метод отправки запроса")
    @Example(example = "И отправить в БД методом SELECT SQL запрос:" + "\"\"\"" + "sql" + "\"\"\"")
    public void executeQuery(final SqlMethod method, final String query) {
        setSqlQuery(query);
        innerExecuteQuery(method);
    }

    private void innerExecuteQueryByConnection(final Connection connection, final SqlMethod method) {
        try {
            switch (method) {
                case SELECT:
                    setActualRecords(DbUtil.executeSelect(connection, getSqlQuery()));
                    convertMapsToCaseInsensitive(getActualRecords());
                    break;
                case EXECUTE:
                    setDbmsLogs(DbUtil.execute(connection, getSqlQuery()));
                    break;
                default:
                    throw new ExecutionException("Wrong method found: {}", method);
            }
        } catch (SQLException exception) {
            throw new ExecutionException("Unable to execute SQL query:\n {} \n", exception, getSqlQuery());
        }
    }

    private void innerExecuteQuery(final SqlMethod method) {
        try {
            switch (method) {
                case SELECT:
                    setActualRecords(DbUtil.executeSelect(getDataSource(), getSqlQuery()));
                    convertMapsToCaseInsensitive(getActualRecords());
                    break;
                case EXECUTE:
                    setDbmsLogs(DbUtil.execute(getDataSource(), getSqlQuery()));
                    break;
                default:
                    throw new ExecutionException("Wrong method found: {}", method);
            }
        } catch (SQLException exception) {
            throw new ExecutionException("Unable to execute SQL query:\n {} \n", exception, getSqlQuery());
        }
    }

    @Then("кол-во записей в ответе {matcher} {int}")
    @Sample("Отправить SQL запрос в БД")
    @Parameter(type = "matcher", name = "матчер проверки")
    @Parameter(type = "int", name = "необходимое кол-во")
    @Example(example = "И кол-во записей в ответе <= 5")
    @SuppressWarnings("unchecked")
    public void checkResponseSize(final MatcherName matcherName, final Integer expectedRecordsNumber) {
        var matcher = MatcherParser.getMatcher(matcherName, expectedRecordsNumber);
        Validator.assertThat(
                getActualRecords().size(),
                matcher,
                "Actual amount of records({}) doesn't match the expected",
                getActualRecords().size());
    }

    @Then("ответ содержит записи:/запись:")
    @Sample("Провалидировать результирующие записи из БД")
    @Parameter(type = ":", name = "Таблица ожидаемых результатов")
    @Example(
            example = "И ответ содержит записи:"
                    + "| ID      | FullName                 | INN      |"
                    + "| ${{ID}} | ${{Полное наименование}} | ${{ИНН}} |")
    public void checkResponseRecords(final List<Map<String, String>> expectedRecords) {
        List<Map<String, String>> resolovedRecords =
                expectedRecords.stream().map(placeholderResolver::resolve).collect(toList());
        Validator.assertThat(getActualRecords(), ContainsSqlRecords.containsSqlRecords(resolovedRecords));
    }

    @When("получить переменные из {int} записи SQL ответа:")
    @Sample("Получить переменные из определенной строки ответа")
    @Parameter(type = "int", name = "строка")
    @Parameter(type = ":", name = "наименование переменной куда положить результат + необходимый столбец")
    @Example(example = "И получить переменные из 1 записи SQL ответа:" + "| variableName | id |")
    public void setVariables(final int sqlRowNumber, final List<Pair> rows) {
        Validator.assertThat(
                getActualRecords().size() >= sqlRowNumber,
                "SQL Record with RowNumber {} is missing, actual Records: {}",
                sqlRowNumber,
                getActualRecords().toString());
        for (var pair : rows) {
            var variableName = pair.getFirst();
            var dbColumnName = pair.getSecond().replace("\"", "\\\"");
            var value = getActualRecords().get(sqlRowNumber - 1).get(dbColumnName);
            context.set(variableName, value);
        }
    }

    @When("получить переменные из логов от SQL ответа:")
    @Sample("Получить переменные из логов ответа")
    @Parameter(type = ":", name = "наименование переменной куда положить результат + номер индекса в логе")
    @Example(example = "И получить переменные из логов от SQL ответа:" + "| variableName | logNumber |")
    public void setVariablesFromLogs(final List<Pair> rows) {
        for (var pair : rows) {
            var variableName = pair.getFirst();
            var logNumberString = pair.getSecond();
            var logNumber = Integer.parseInt(logNumberString);
            var value = getDbmsLogs().get(logNumber - 1);
            context.set(variableName, value);
        }
    }

    private void convertMapsToCaseInsensitive(final List<Map<String, Object>> mapList) {
        mapList.replaceAll(recordMap -> {
            var caseInsensitiveMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            caseInsensitiveMap.putAll(recordMap);
            return caseInsensitiveMap;
        });
    }

    @Then("SQL ответ содержит в логах строки:")
    @Sample("Проверка наличия строк в логе")
    @Parameter(type = ":", name = "искомые строки")
    @Example(example = "И SQL ответ содержит в логах строки:" + "| string |")
    public void checkDbmsLogs(final List<String> expectedLogs) {
        var actualLogs = dbContextContainer.getDbmsLogs();
        expectedLogs.forEach(expectedLog -> {
            var found = actualLogs.stream().anyMatch(actualLog -> actualLog.contains(expectedLog));
            if (!found) {
                throw new AssertionError(StringUtil.format("Log not found: '{}'", expectedLog));
            }
        });
    }

    @When("ответ не содержит дубликаты")
    @Sample("Проверка, что нет повторяющихся записей")
    public void detectDuplicate() {
        List<Map<String, Object>> response = getActualRecords();
        List<Object> items = new ArrayList<>();
        for (Map<String, Object> map : response) {
            Collection val = map.values();
            if (items.containsAll(val)) {
                throw new AssertionError(StringUtil.format("Найдены дубликаты: '{}'", val));
            }
            items.addAll(map.values());
        }
    }

    @When("ответ не содержит пустых полей")
    @Sample("Проверка, что нет полей в записях ответа, значений которых null или пустая строка")
    public void checkEmptyTable() {
        List<Map<String, Object>> response = getActualRecords();
        Set<String> cols = new HashSet<String>();
        for (Map<String, Object> map : response) {
            for (Map.Entry entry : map.entrySet()) {
                var s = String.valueOf(entry.getValue());
                if (s.isEmpty()) {
                    cols.add(String.valueOf(entry.getKey()));
                }
            }
        }
        if (!cols.isEmpty()) throw new AssertionError(StringUtil.format("Пустые поля {}", cols));
    }
}
