package ru.autotestframework.sql.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceHolder {

    private static final Map<String, HikariDataSource> DATA_SOURCES = new ConcurrentHashMap<>();

    private DataSourceHolder() {}

    public static synchronized HikariDataSource getDataSource(ConnectionSupplier connection) {
        if (!DATA_SOURCES.containsKey(connection.getUrl())) {
            DATA_SOURCES.put(connection.getUrl(), getHikariDatasource(connection));
        }
        return DATA_SOURCES.get(connection.getUrl());
    }

    public static void shutdown() {
        DATA_SOURCES.values().forEach(HikariDataSource::close);
    }

    private static HikariDataSource getHikariDatasource(ConnectionSupplier connection) {
        return new HikariDataSource(getConfig(connection));
    }

    private static HikariConfig getConfig(ConnectionSupplier connection) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connection.getUrl());
        config.setUsername(connection.getLogin());
        config.setPassword(connection.getPassword());
        config.setMaximumPoolSize(10);
        return config;
    }
}
