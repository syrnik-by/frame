package ru.autotestframework.sql_steps.components.fake_sql_driver;

import static org.mockito.Mockito.mock;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Wrapper;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.sql_steps.components.DbUtil;

/**
 * A fake Connection capable of simulating various scenarios.
 */
@Slf4j
@Data
public class FakeConnection implements Connection, Wrapper {

    private final String path;

    public FakeConnection(final String url) {
        path = url;
    }

    /**
     * Cucumber table for setting driver parameters and connecting to the database
     *
     * @param sql sql request
     * @return returns an empty Optional instance. No value is present for this Optional.
     */
    public <T> Optional<T> executeQuery(final String sql) {
        final JsonElement fileData;

        try (var is = DbUtil.class.getClassLoader().getResourceAsStream(path);
                var isr = new InputStreamReader(Objects.requireNonNull(is))) {
            fileData = JsonParser.parseReader(isr);
        } catch (IOException e) {
            throw new ExecutionException("Unable to read the file " + path, e);
        }
        if (!fileData.isJsonArray()) {
            throw new ExecutionException(String.format("File with SQL resolver '%s' must be an array", path));
        }
        for (final JsonElement e : fileData.getAsJsonArray()) {
            final Optional<T> fakeResponse = createFakeResponse(e, sql);
            if (fakeResponse.isPresent()) {
                return fakeResponse;
            }
        }
        return Optional.empty();
    }

    /**
     * Cucumber table for setting driver parameters and connecting to the database
     *
     * @param sql sql request
     * @param element sql request
     * @return returns an empty Optional instance. No value is present for this Optional.
     */
    private <T> Optional<T> createFakeResponse(final JsonElement element, final String sql) {
        if (!element.getAsJsonObject().has("request")) {
            throw new ExecutionException(
                    String.format("File with SQL resolver '%s' " + "must contains field 'request' with matcher", path));
        }
        final var matcher = Pattern.compile(
                        element.getAsJsonObject().get("request").getAsString())
                .matcher(sql);
        if (matcher.find()) {
            if (!element.getAsJsonObject().has("response")) {
                throw new ExecutionException(String.format(
                        "File with SQL resolver '%s'" + " must contains field 'response' with SQL response", path));
            }
            final var typeOfT = new TypeToken<T>() {}.getType();
            return Optional.of(new Gson().fromJson(element.getAsJsonObject().getAsJsonArray("response"), typeOfT));
        }
        return Optional.empty();
    }

    @Override
    public Statement createStatement() {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) {
        return mock(PreparedStatement.class);
    }

    @Override
    public CallableStatement prepareCall(final String sql) {
        throw new NotImplementedException();
    }

    @Override
    public String nativeSQL(final String sql) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return false;
    }

    @Override
    public boolean getAutoCommit() {
        throw new NotImplementedException();
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) {
        throw new NotImplementedException();
    }

    @Override
    public void commit() {
        throw new NotImplementedException();
    }

    @Override
    public void rollback() {
        throw new NotImplementedException();
    }

    @Override
    public void close() {
        log.info("fakeConnection.close() called from try with resources");
    }

    @Override
    public boolean isClosed() {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return mock(DatabaseMetaData.class);
    }

    @Override
    public boolean isReadOnly() {
        throw new NotImplementedException();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        throw new NotImplementedException();
    }

    @Override
    public String getCatalog() {
        throw new NotImplementedException();
    }

    @Override
    public void setCatalog(final String catalog) {
        throw new NotImplementedException();
    }

    @Override
    public int getTransactionIsolation() {
        throw new NotImplementedException();
    }

    @Override
    public void setTransactionIsolation(final int level) {
        throw new NotImplementedException();
    }

    @Override
    public SQLWarning getWarnings() {
        throw new NotImplementedException();
    }

    @Override
    public void clearWarnings() {
        throw new NotImplementedException();
    }

    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(
            final String sql, final int resultSetType, final int resultSetConcurrency) {
        throw new NotImplementedException();
    }

    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) {
        throw new NotImplementedException();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() {
        throw new NotImplementedException();
    }

    @Override
    public void setTypeMap(final Map<String, Class<?>> map) {
        throw new NotImplementedException();
    }

    @Override
    public int getHoldability() {
        return 0;
    }

    @Override
    public void setHoldability(final int holdability) {
        throw new NotImplementedException();
    }

    @Override
    public Savepoint setSavepoint() {
        throw new NotImplementedException();
    }

    @Override
    public Savepoint setSavepoint(final String name) {
        throw new NotImplementedException();
    }

    @Override
    public void rollback(final Savepoint savepoint) {
        throw new NotImplementedException();
    }

    @Override
    public void releaseSavepoint(final Savepoint savepoint) {
        throw new NotImplementedException();
    }

    @Override
    public Statement createStatement(
            final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(
            final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        throw new NotImplementedException();
    }

    @Override
    public CallableStatement prepareCall(
            final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) {
        throw new NotImplementedException();
    }

    @Override
    public Clob createClob() {
        throw new NotImplementedException();
    }

    @Override
    public Blob createBlob() {
        throw new NotImplementedException();
    }

    @Override
    public NClob createNClob() {
        throw new NotImplementedException();
    }

    @Override
    public SQLXML createSQLXML() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isValid(final int timeout) {
        return false;
    }

    @Override
    public void setClientInfo(final String name, final String value) {
        throw new NotImplementedException();
    }

    @Override
    public String getClientInfo(final String name) {
        throw new NotImplementedException();
    }

    @Override
    public Properties getClientInfo() {
        throw new NotImplementedException();
    }

    @Override
    public void setClientInfo(final Properties properties) {
        throw new NotImplementedException();
    }

    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) {
        throw new NotImplementedException();
    }

    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) {
        throw new NotImplementedException();
    }

    @Override
    public String getSchema() {
        throw new NotImplementedException();
    }

    @Override
    public void setSchema(final String schema) {
        throw new NotImplementedException();
    }

    @Override
    public void abort(final Executor executor) {
        throw new NotImplementedException();
    }

    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) {
        throw new NotImplementedException();
    }

    @Override
    public int getNetworkTimeout() {
        return 0;
    }
}
