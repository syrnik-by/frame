package ru.autotestframework.sql_steps.Tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.sql_steps.SqlSteps;
import ru.autotestframework.sql_steps.components.ConnectionPoolContainer;
import ru.autotestframework.sql_steps.components.CucumberTypesDefinition;
import ru.autotestframework.sql_steps.components.DBContextContainer;
import ru.autotestframework.sql_steps.components.DataSourceContainer;
import ru.autotestframework.sql_steps.components.SqlMethod;
import ru.autotestframework.sql_steps.components.fake_sql_driver.FakeConnection;
import ru.autotestframework.sql_steps.components.fake_sql_driver.FakeDataSource;

@Tag("@SqlSteps")
class SqlStepsTests {
    private SqlSteps sqlSteps;

    Pair pair1 = Pair.of("r1", "v1");

    Pair pair2 = Pair.of("r2", "v2");

    private String rnd1 = Faker.instance().rockBand().name();
    private String rnd2 = Faker.instance().rockBand().name();

    @BeforeEach
    void beforeEach() {
        ConnectionPoolContainer connectionPoolContainer = new ConnectionPoolContainer();
        DBContextContainer dbContextContainer = new DBContextContainer();
        FrameworkProperties frameworkProperties = new FrameworkProperties();
        PlaceholderResolverImpl placeholderResolver = new PlaceholderResolverImpl(new StringSubstitutor());

        int countContext = 10;
        ContextImpl context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
        while (countContext != 0) {
            context.set(String.valueOf(countContext), RandomStringUtils.randomAlphanumeric(10));
            countContext--;
        }
        sqlSteps = new SqlSteps(
                context,
                placeholderResolver,
                new FileLoaderImpl(placeholderResolver, frameworkProperties),
                dbContextContainer);
        sqlSteps.setConnectionPoolContainer(connectionPoolContainer);
    }

    @Test
    void checkConnectorsDiffNames() {
        DataSource dataSource1 = new FakeDataSource("url1");
        DataSource dataSource2 = new FakeDataSource("url2");

        Map<String, Connection> connectionPoolBefore =
                sqlSteps.getConnectionPoolContainer().getConnectionPool();
        Map<Connection, Long> uniqueConnectionsBefore = connectionPoolBefore.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Assertions.assertEquals(0, uniqueConnectionsBefore.size());
        Assertions.assertEquals(0, connectionPoolBefore.size());
        sqlSteps.createConnection(rnd1, dataSource1);
        sqlSteps.createConnection(rnd2, dataSource2);
        Map<String, Connection> connectionPoolResult =
                sqlSteps.getConnectionPoolContainer().getConnectionPool();
        Map<Connection, Long> uniqueConnections = connectionPoolResult.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Assertions.assertEquals(2, uniqueConnections.size());
    }

    @Test
    void checkConnectorWithSameNames() {
        DataSource dataSource = new FakeDataSource("");
        Map<String, Connection> connectionPoolBefore =
                sqlSteps.getConnectionPoolContainer().getConnectionPool();
        Map<Connection, Long> uniqueConnectionsBefore = connectionPoolBefore.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Assertions.assertEquals(0, uniqueConnectionsBefore.size());
        Assertions.assertEquals(0, connectionPoolBefore.size());
        sqlSteps.createConnection(rnd1, dataSource);
        sqlSteps.createConnection(rnd1, dataSource);
        Map<String, Connection> connectionPoolResult =
                sqlSteps.getConnectionPoolContainer().getConnectionPool();
        Map<Connection, Long> uniqueConnections = connectionPoolResult.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Assertions.assertEquals(1, uniqueConnections.size());
    }

    @Test
    void checkCucumberTypesProceedingSameDataSource() {
        DataSourceContainer dataSourceContainer = new DataSourceContainer();
        CucumberTypesDefinition cucumberTypesDefinition =
                new CucumberTypesDefinition(new PlaceholderResolverImpl(new StringSubstitutor()), dataSourceContainer);
        Map<String, String> cucumberProperties = new HashMap<>() {
            {
                put("url", "data/fake_select_sql.json");
                put("user", "user ");
                put("password", "password");
                put("dbms", "ORACLE");
            }
        };
        cucumberTypesDefinition.dataSource(cucumberProperties);
        cucumberTypesDefinition.dataSource(cucumberProperties);
        Map<String, DataSource> connectionPoolResult = dataSourceContainer.getDataSourcePool();
        Assertions.assertEquals(1, connectionPoolResult.size());
    }

    @Test
    void checkCucumberTypesProceedingDiffDataSource() {
        DataSourceContainer dataSourceContainer = new DataSourceContainer();
        CucumberTypesDefinition cucumberTypesDefinition =
                new CucumberTypesDefinition(new PlaceholderResolverImpl(new StringSubstitutor()), dataSourceContainer);
        cucumberTypesDefinition.dataSource(new HashMap<>() {
            {
                put("url", "data/fake_select_sql.json");
                put("user", "user ");
                put("password", "password");
                put("dbms", "ORACLE");
            }
        });
        cucumberTypesDefinition.dataSource(new HashMap<>() {
            {
                put("url", "data/fake_update_sql1.json");
                put("user", "user1 ");
                put("password", "password1");
                put("dbms", "ORACLE");
            }
        });
        Assertions.assertEquals(2, dataSourceContainer.getDataSourcePool().size());
    }

    @Test
    void negativeNoConnectorCreated() {
        String query = "select 1 from dual";
        Assertions.assertThrows(
                AutotestException.class, () -> sqlSteps.executeQueryByConnection("dummy", SqlMethod.EXECUTE, query));
    }

    @Test
    void setSqlQuery() {
        sqlSteps.setSqlQuery(rnd1);
        Assertions.assertEquals(sqlSteps.getDbContextContainer().getSqlQuery(), rnd1);
    }

    @Test
    void setDataSource() {
        DataSource dataSource = new FakeDataSource(rnd1);
        Connection actualConnection;
        sqlSteps.setDataSource(dataSource);
        Assertions.assertDoesNotThrow(
                () -> sqlSteps.getDbContextContainer().getDataSource().getConnection());
        try {
            actualConnection = sqlSteps.getDbContextContainer().getDataSource().getConnection();
            Assertions.assertEquals(rnd1, ((FakeConnection) actualConnection).getPath());
        } catch (SQLException ignore) {
        }
    }

    @Test
    void setActualRecords() {
        final List<Map<String, Object>> actualRecords = new ArrayList<>() {
            {
                add(new HashMap<>() {
                    {
                        put("1", "value1");
                    }
                });
                add(new HashMap<>() {
                    {
                        put("2", "value2");
                    }
                });
            }
        };
        sqlSteps.setActualRecords(actualRecords);
        Assertions.assertEquals(
                sqlSteps.getDbContextContainer().getActualRecords().get(1), actualRecords.get(1));
        Assertions.assertEquals(
                sqlSteps.getDbContextContainer().getActualRecords().get(0), actualRecords.get(0));
    }

    @Test
    void setDbmsLogs() {
        final List<String> dbmsLogs = new ArrayList<>() {
            {
                add(rnd1);
            }
        };
        sqlSteps.setDbmsLogs(dbmsLogs);
        Assertions.assertEquals(sqlSteps.getDbContextContainer().getDbmsLogs().get(0), rnd1);
    }

    @Test
    void getConnectionToDb() {
        DataSource dataSource = new FakeDataSource(rnd1);
        Connection actualConnection;
        sqlSteps.getConnectionToDb(dataSource);
        Assertions.assertDoesNotThrow(
                () -> sqlSteps.getDbContextContainer().getDataSource().getConnection());
        try {
            actualConnection = sqlSteps.getDbContextContainer().getDataSource().getConnection();
            Assertions.assertEquals(rnd1, ((FakeConnection) actualConnection).getPath());
        } catch (SQLException ignore) {
        }
    }

    @Test
    void executeQueryByConnection() {
        sqlSteps.createConnection(rnd1, new FakeDataSource("data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQueryByConnection(rnd1, SqlMethod.SELECT, rnd2));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQueryByConnection(rnd1, SqlMethod.EXECUTE, rnd2));
    }

    @Test
    void executeQueryByConnectionFile() {
        sqlSteps.createConnection(rnd1, new FakeDataSource("data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(
                () -> sqlSteps.executeQueryByConnection(rnd1, SqlMethod.SELECT, "data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(
                () -> sqlSteps.executeQueryByConnection(rnd1, SqlMethod.EXECUTE, "data/fake_select_sql.json"));
    }

    @Test
    void executeQuery() {
        sqlSteps.setDataSource(new FakeDataSource("data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQuery(SqlMethod.SELECT, "data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQuery(SqlMethod.EXECUTE, "data/fake_select_sql.json"));
    }

    @Test
    void executeQueryResolve() {

        sqlSteps.setDataSource(new FakeDataSource("data/fake_select_sql.json"));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQuery(SqlMethod.SELECT, rnd2));
        Assertions.assertDoesNotThrow(() -> sqlSteps.executeQuery(SqlMethod.EXECUTE, rnd1));
    }

    @Test
    void checkResponseRecords() {
        final List<Map<String, String>> expectedRows = new ArrayList<>() {
            {
                add(new HashMap<>() {
                    {
                        put("PARAM", String.valueOf(true));
                    }
                });
                add(new HashMap<>() {
                    {
                        put("PARAM1", String.valueOf(100.0));
                    }
                });
                add(new HashMap<>() {
                    {
                        put("PARAM2", "test");
                    }
                });
            }
        };
        String query = "from table sql1";
        sqlSteps.createConnection(rnd1, new FakeDataSource("data/fake_select_sql.json"));
        sqlSteps.executeQueryByConnection(rnd1, SqlMethod.SELECT, query);
        Assertions.assertDoesNotThrow(() -> sqlSteps.checkResponseRecords(expectedRows));
        Assertions.assertDoesNotThrow(() -> sqlSteps.checkResponseSize(MatcherName.EQUAL_TO, 1));
    }

    @Test
    void setVar() {
        List<Pair> rows = List.of(pair1, pair2);

        int contextBeforeSet = sqlSteps.getContext().getAll().size();
        sqlSteps.setActualRecords(List.of(Map.of("key1", "val2")));
        sqlSteps.setVariables(1, rows);
        Assertions.assertEquals(rows.size(), sqlSteps.getContext().getAll().size() - contextBeforeSet);
    }

    @Test
    void setVariablesFromLogs() {
        int contextBeforeSet = sqlSteps.getContext().getAll().size();
        sqlSteps.setDbmsLogs(List.of(
                Faker.instance().science().scientist(),
                Faker.instance().weather().description()));
        sqlSteps.setVariablesFromLogs(new ArrayList<>() {
            {
                add(Pair.of("test", "1"));
            }
        });

        Assertions.assertEquals(
                contextBeforeSet + 1, sqlSteps.getContext().getAll().size());
    }

    @Test
    void checkDbmsLogs() {
        sqlSteps.setDbmsLogs(List.of(Faker.instance().science().scientist()));
        List<String> dbmsLogs = sqlSteps.getDbContextContainer().getDbmsLogs();
        Assertions.assertDoesNotThrow(() -> sqlSteps.checkDbmsLogs(dbmsLogs));
    }
}
