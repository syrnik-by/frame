package ru.autotestframework.sql.components;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DataSourceContainer {
    @Getter
    private Map<String, DataSource> dataSourcePool = new HashMap<>();
}
