package ru.autotestframework.sql.db;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DslContextHolder {

    private static final Map<String, DSLContext> CONTEXTS = new ConcurrentHashMap<>();

    private DslContextHolder() {}

    public static synchronized DSLContext getDslContext(ConnectionSupplier connection) {
        if (!CONTEXTS.containsKey(connection.getUrl())) {
            HikariDataSource dataSource = DataSourceHolder.getDataSource(connection);
            CONTEXTS.put(connection.getUrl(), DSL.using(dataSource, SQLDialect.POSTGRES));
        }
        return CONTEXTS.get(connection.getUrl());
    }

    public static void shutdown() {
        CONTEXTS.clear();
    }
}
