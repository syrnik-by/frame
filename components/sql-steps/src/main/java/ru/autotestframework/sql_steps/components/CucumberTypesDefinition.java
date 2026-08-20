package ru.autotestframework.sql_steps.components;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.autotestframework.Constants;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.sql_steps.components.fake_sql_driver.FakeDataSource;
import ru.autotestframework.util.Validator;

/**
 * This class describes the cucumber types used in cucumber sql steps
 */
@Data
@Slf4j
@RequiredArgsConstructor
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CucumberTypesDefinition {

    private final PlaceholderResolver placeholderResolver;
    private final DataSourceContainer dataSourceContainer;

    /**
     * Cucumber type sql method
     *
     * @param value sql method
     * @return SqlMethod.REGEX
     */
    @ParameterType(name = "sql_method", value = SqlMethod.REGEX)
    public SqlMethod sql_method(final String value) {
        return SqlMethod.valueOf(value);
    }

    /**
     * Cucumber table for setting driver parameters and connecting to the database
     *
     * @param unresolvedProperties param (url,user,password,dbms)
     * @return connect
     */
    @DataTableType
    public DataSource dataSource(final Map<String, String> unresolvedProperties) {
        var properties = placeholderResolver.resolve(unresolvedProperties);
        var url = properties.get("url");
        var user = properties.get("user");
        char[] pass = properties.get("password").toCharArray();
        if (url == null || user == null) {
            throw new AutotestException("Required parameters weren't specified: url, user, password");
        }
        var driverProperty = properties.get("driver");
        var dbmsProperty = properties.get("dbms");
        String driver;
        // TODO удалить "driver", когда все перейдут на параметр dbms
        if (driverProperty != null && !driverProperty.isBlank()) {
            log.error("Using of 'driver' property is not recommended, use `dbms`");
            Validator.checkThat(
                    SupportedDbms.isDriverSupported(driverProperty),
                    "JDBC driver '{}' is not supported",
                    driverProperty);
            driver = driverProperty;
        } else if (dbmsProperty != null && !dbmsProperty.isBlank()) {
            driver = SupportedDbms.findByName(dbmsProperty).getDriverName();
        } else {
            driver = SupportedDbms.getDefaultDbms().getDriverName();
        }
        if ("true".equals(System.getProperty(Constants.FAKE_DB_DRIVER_USE))) {
            return new FakeDataSource(url);
        }
        var dataSource = Optional.ofNullable(
                        dataSourceContainer.getDataSourcePool().get(url))
                .orElse(DataSourceBuilder.create()
                        .driverClassName(driver)
                        .url(url)
                        .username(user)
                        .password(String.valueOf(pass))
                        .build());
        Arrays.fill(pass, ' ');
        dataSourceContainer.getDataSourcePool().put(url, dataSource);
        return dataSource;
    }
}
