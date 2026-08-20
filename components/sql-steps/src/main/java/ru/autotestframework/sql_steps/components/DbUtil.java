package ru.autotestframework.sql_steps.components;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Allure;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import ru.autotestframework.sql_steps.components.fake_sql_driver.FakeConnection;

/**
 * This util class for execute method sql request
 */
@Slf4j
@UtilityClass
public class DbUtil {

    private static final String SQL_REQUEST = "SQL request:\n{}";

    /**
     * Execute given query and return results.
     *
     * @param dataSource {@link DataSource}
     * @param sqlQuery   sql select as String
     * @return List of Mapped as Object SqlResults
     * @throws SQLException on any error
     */
    public static List<Map<String, Object>> executeSelect(final DataSource dataSource, final String sqlQuery)
            throws SQLException {
        try (var connection = dataSource.getConnection()) {
            return executeSelect(connection, sqlQuery);
        }
    }

    /**
     * Execute given query and return results.
     *
     * @param connection {@link Connection}
     * @param sqlQuery   sql select as String
     * @return List of Mapped as Object SqlResults
     * @throws SQLException on any error
     */
    public static List<Map<String, Object>> executeSelect(final Connection connection, final String sqlQuery)
            throws SQLException {
        log.info(SQL_REQUEST, sqlQuery);
        Allure.addAttachment("SQL request", sqlQuery);
        final List<Map<String, Object>> response = Lists.newArrayList();
        if (connection instanceof FakeConnection) {
            Optional<List<Map<String, Object>>> fakeResponse = ((FakeConnection) connection).executeQuery(sqlQuery);
            response.addAll(fakeResponse.orElse(new ArrayList<>()));
        } else {
            DSL.using(connection)
                    .fetchMany(sqlQuery)
                    .forEach(result -> result.forEach(record -> response.add(record.intoMap())));
        }
        final String responseLog =
                new GsonBuilder().setPrettyPrinting().create().toJson(response);
        log.info("SQL response:\n{}", responseLog);
        Allure.addAttachment("SQL response", responseLog);
        return response;
    }

    /**
     * Execute given script and return logs.
     *
     * @param dataSource {@link DataSource}
     * @param sqlQuery   executable sql script
     * @return List of logs from DB
     * @throws SQLException on any error.
     */
    public static List<String> execute(final DataSource dataSource, final String sqlQuery) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            return execute(connection, sqlQuery);
        }
    }

    /**
     * Execute given script and return logs.
     *
     * @param connection A connection session
     * @param sqlQuery   executable sql script
     * @return List of logs from DB
     * @throws SQLException
     */
    public static List<String> execute(final Connection connection, final String sqlQuery) throws SQLException {
        log.info(SQL_REQUEST, sqlQuery);
        Allure.addAttachment("SQL request", sqlQuery);
        final List<String> logs = Lists.newArrayList();
        if (connection instanceof FakeConnection) {
            final Optional<List<String>> fakeResponse = ((FakeConnection) connection).executeQuery(sqlQuery);
            fakeResponse.ifPresent(logs::addAll);
        } else {
            var driverClassName = DriverManager.getDriver(
                            connection.getMetaData().getURL())
                    .getClass()
                    .getName();
            if (driverClassName.equals(SupportedDbms.ORACLE.getDriverName())) {
                logs.addAll(executeAndGetLogs(connection, sqlQuery));
            } else {
                DSL.using(connection)
                        .resultQuery(sqlQuery)
                        .forEach(r -> logs.add(r.intoList().toString()));
            }
        }
        if (!logs.isEmpty()) {
            log.info("SQL response:\n{}", String.join("\n", logs));
            Allure.addAttachment("SQL response", String.join("\n", logs));
        }
        return logs;
    }

    private static List<String> executeAndGetLogs(final Connection connection, final String sqlQuery)
            throws SQLException {
        List<String> logs = Lists.newArrayList();
        var dbmsOutput = new DbmsOutput(connection);
        try (var stmt = connection.createStatement()) {
            dbmsOutput.enable(1000000);
            stmt.execute(sqlQuery);
        }
        logs.addAll(dbmsOutput.getLogs());
        dbmsOutput.disable();
        dbmsOutput.close();

        return logs;
    }
}
