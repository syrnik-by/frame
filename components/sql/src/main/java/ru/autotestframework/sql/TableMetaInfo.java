package ru.autotestframework.sql;

import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class TableMetaInfo {
    String tableName;
    String tableType;
    String tableScheme;
    Connection connection;
    long rows;

    @Data
    @AllArgsConstructor
    public static class Column {
        String columnName;
        String columnType;
        int columnSize;
        boolean isNullable;
    }

    @SneakyThrows
    public HashMap<String, Column> getColumnInfo() {
        var cols = getColumns();
        return Maps.newHashMap(Maps.uniqueIndex(cols, Column::getColumnName));
    }

    @SneakyThrows
    public Set<Column> getColumns() {
        ResultSet columns = connection.getMetaData().getColumns(null, tableScheme, tableName, "%");
        Set<Column> result = new HashSet<>();
        try {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                boolean isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                Column column = new Column(columnName, columnType, columnSize, isNullable);
                result.add(column);
            }
        } catch (Exception ex) {
            log.warn("Таблица {} не обработась полностью: {}", tableName, ex.getMessage());
        }
        return result;
    }
}
