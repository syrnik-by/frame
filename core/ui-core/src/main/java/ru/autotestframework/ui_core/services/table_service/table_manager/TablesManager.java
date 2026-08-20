package ru.autotestframework.ui_core.services.table_service.table_manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.page_manager.Page;
import ru.autotestframework.ui_core.services.table_service.ITable;

/**
 * Tables manager.
 */
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TablesManager {

    private Boolean isTableCacheEnabled;
    private Map<String, ITable> allPageTables = new HashMap<>();
    private Page page;
    // TODO del after delete deprecate cache clean
    private final UiProperties configuration;

    /**
     * Instantiates a new Tables manager.
     *
     * @param uiProperties the ui properties
     */
    public TablesManager(UiProperties uiProperties) {
        this.configuration = uiProperties;
        isTableCacheEnabled = uiProperties.isTableCacheEnabled();
    }

    /**
     * Gets table.
     *
     * @param tableName the table name
     * @return the table
     */
    public ITable getTable(final String tableName) {
        return getInitTable(checkTable(tableName));
    }

    /**
     * Sets current page tables.
     *
     * @param page the page
     */
    public void setCurrentPageTables(Page page) {
        this.page = page;
        allPageTables.clear();
        allPageTables = page.getElementsByType(ITable.class);
    }

    /**
     * Clean table cache.
     *
     * @param tableName the table name
     */
    public void cleanTableCache(String tableName) {
        allPageTables.get(tableName).clearCache();
    }

    /**
     * Clean table cache.
     */
    public void cleanTableCache() {
        allPageTables.forEach((key, value) -> value.clearCache());
    }

    private ITable getInitTable(ITable table) {
        if (table.isInit() || !isTableCacheEnabled) {
            table.init();
            allPageTables.put(table.getTitle(), table);
        }
        return table;
    }

    private ITable checkTable(String tableName) {
        var currentTable = allPageTables.get(tableName);
        if (currentTable == null) {
            throw new AutotestException(
                    "Table with name '{}' not exist in page '{}' \n Available tables '{}'",
                    tableName,
                    page.getTitle(),
                    Arrays.toString(allPageTables.keySet().toArray()));
        }
        return currentTable;
    }
}
