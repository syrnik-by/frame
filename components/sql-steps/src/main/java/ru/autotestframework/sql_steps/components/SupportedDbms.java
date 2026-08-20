package ru.autotestframework.sql_steps.components;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.autotestframework.core.exception.ExecutionException;

/**
 * Supported class for work with sql Drivers
 */
@Getter
@RequiredArgsConstructor
public enum SupportedDbms {
    ORACLE("oracle.jdbc.OracleDriver"),
    POSTGRES("org.postgresql.Driver"),
    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    private final String driverName;

    /**
     * Get an JDBC DriverName on given Database.
     *
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

    /**
     * Get default driver
     *
     * @return supported dbms
     */
    public static SupportedDbms getDefaultDbms() {
        return ORACLE;
    }

    /**
     * Check supported drivers
     *
     * @param driverName name driver
     * @return check driver
     */
    public static boolean isDriverSupported(final String driverName) {
        return Arrays.stream(values()).anyMatch(dbms -> dbms.driverName.equalsIgnoreCase(driverName));
    }

    /**
     * Get all supported drivers
     *
     * @return enum all drivers
     */
    private static String getSupportedDbms() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining());
    }
}
