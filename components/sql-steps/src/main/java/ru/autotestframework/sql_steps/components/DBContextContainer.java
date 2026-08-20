package ru.autotestframework.sql_steps.components;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class for storage data context
 */
@Slf4j
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@Data
public class DBContextContainer {

    private DataSource dataSource;

    private List<Map<String, Object>> actualRecords;

    private List<String> dbmsLogs;

    private String sqlQuery;
}
