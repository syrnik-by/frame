package ru.autotestframework.sql.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class ConnectionSupplier {

    private String url;
    private String login;
    private String password;

    public ConnectionSupplier(String connectionString) {}
}
