package ru.autotestframework.sql_steps.components;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * This class for storage data source pool
 */
@Component
public class DataSourceContainer {
    @Getter
    private Map<String, DataSource> dataSourcePool = new HashMap<>();
}
