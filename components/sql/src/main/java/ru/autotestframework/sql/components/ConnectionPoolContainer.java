package ru.autotestframework.sql.components;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.AutotestException;

@Slf4j
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@Data
public class ConnectionPoolContainer implements DisposableBean {
    private Map<String, Connection> connectionPool = new HashMap<>();

    // возвращает соединение в пул DataSource-а по окончанию теста
    @Override
    public void destroy() throws SQLException {
        for (Map.Entry<String, Connection> entry : connectionPool.entrySet()) {
            entry.getValue().close();
        }
    }

    public Connection getConnectionByName(final String connectionName) {
        return Optional.ofNullable(connectionPool.get(connectionName))
                .orElseThrow(() -> new AutotestException(
                        "Within Scenario was no Connector with name '{}' created", connectionName));
    }

    /**
     * Creates connection with given Name in a given pool (DataSource) for current Autotest in Context.
     * @param connectionName connection alias
     * @param dataSource
     */
    public void createConnection(final String connectionName, final DataSource dataSource) {
        Optional<Connection> optionalConnection = Optional.ofNullable(connectionPool.get(connectionName));
        if (!optionalConnection.isPresent()) {
            try {
                connectionPool.put(connectionName, dataSource.getConnection());
            } catch (SQLException e) {
                throw new AutotestException("Failed to create Connector with Name {}", e, connectionName);
            }
        }
    }
}
