package ru.autotestframework.sql_steps.components.fake_sql_driver;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A fake DataSource capable of simulating various scenarios.
 */
public class FakeDataSource implements DataSource {
    private final String path;

    public FakeDataSource(final String url) {
        path = url;
    }

    @Override
    public Connection getConnection() {
        return new FakeConnection(path);
    }

    @Override
    public Connection getConnection(final String username, final String password) {
        return new FakeConnection(path);
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(final PrintWriter out) {
        throw new NotImplementedException();
    }

    @Override
    public void setLoginTimeout(final int seconds) {
        throw new NotImplementedException();
    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return false;
    }
}
