package ru.autotestframework.sql.components;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.autotestframework.core.exception.ExecutionException;

@Getter
@RequiredArgsConstructor
public enum SupportedDbms {
    ORACLE("oracle.jdbc.OracleDriver"),
    POSTGRES("org.postgresql.Driver"),
    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    private final String driverName;

    /**
     * Get an JDBC DriverName on given Database.
     * @param dbmsName databaseName
     * @return enum driver value
     */
    public static SupportedDbms findByName(final String dbmsName) {
        return Arrays.stream(values())
                .filter(n -> n.name().equalsIgnoreCase(dbmsName))
                .findFirst()
                .orElseThrow(() -> new ExecutionException(
                        "БД `{}` не поддерживается. Список поддерживаемых `{}`", dbmsName, getSupportedDbms()));
    }

    public static SupportedDbms getDefaultDbms() {
        return ORACLE;
    }

    public static boolean isDriverSupported(final String driverName) {
        return Arrays.stream(values()).anyMatch(dbms -> dbms.driverName.equalsIgnoreCase(driverName));
    }

    private static String getSupportedDbms() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining());
    }
}
