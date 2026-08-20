package ru.autotestframework.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBMetaDataComparator {
    @SneakyThrows
    public void dbComparator(Connection connection1, Connection connection2) {
        Map<String, TableMetaInfo> conn1Infos = proceedDataBase(connection1);
        Map<String, TableMetaInfo> conn2Infos = proceedDataBase(connection2);
        compareDataBases(conn1Infos, conn2Infos);
    }

    @SneakyThrows
    public void compareDataBases(Map<String, TableMetaInfo> conn1Infos1, Map<String, TableMetaInfo> conn2Infos) {
        List<String> erasureList = new ArrayList<>();
        conn1Infos1.forEach((tName, data) -> {
            TableMetaInfo data2 = conn2Infos.get(tName);
            erasureList.add(tName);
            if (data2 == null) {
                log.error("Не обнаружилось таблицы '{}' во второй БД", tName);
            } else {
                var comp = compareTable(data, data2, true);
            }
            log.warn(tName);
        });
    }

    @SneakyThrows
    public ComparatorReport compareTable(TableMetaInfo table1, TableMetaInfo table2, boolean includeColumnReport) {

        final String[] report = {"\tСравнение таблиц: " + table1.getTableName() + " <-> " + table2.getTableName()};
        final boolean[] isEquals = {true};

        List<String> erasureList = new ArrayList<>();
        var table1Infos = table1.getColumnInfo();
        var table2Infos = table2.getColumnInfo();

        table1Infos.forEach((columnName, column1) -> {
            TableMetaInfo.Column column2 = table2Infos.get(columnName);
            erasureList.add(columnName);
            if (column2 == null) {
                isEquals[0] = false;
                report[0] += "\n\t\t Не обнаружилось во второй БД столбца :" + columnName;
            } else {
                var columnRep = compareColumn(column1, column2, includeColumnReport);
                isEquals[0] = columnRep.isResult();
                if (includeColumnReport) {
                    report[0] += columnRep.getReport();
                }
            }
        });

        erasureList.forEach(x -> {
            table1Infos.remove(x);
            table2Infos.remove(x);
        });

        String remainColumnsFromT2 =
                table2Infos.entrySet().stream().map(x -> x.getKey()).collect(Collectors.joining("; "));
        if (remainColumnsFromT2.length() > 3) {
            report[0] += "\n\t\t Не обнаружилось в первой таблице столбцов :" + remainColumnsFromT2;
            isEquals[0] = false;
        }
        if (isEquals[0]) {
            report[0] += "\n\t\t Различий не обнаружено" + remainColumnsFromT2;
        }
        return new ComparatorReport(report[0], isEquals[0]);
    }

    // TODO inside comparator (field name)
    @SneakyThrows
    public ComparatorReport compareColumn(TableMetaInfo.Column column1, TableMetaInfo.Column column2, boolean include) {

        boolean isEquals = true;
        String report = "\t\t Column: " + column1.getColumnName();

        if (column1.getColumnSize() != column2.getColumnSize()) {
            report += "\n\t\t\t ColumnSize " + column1.getColumnSize() + "!=" + column2.getColumnSize();
            isEquals = false;
        }

        if (!column1.getColumnType().equals(column2.getColumnType())) {
            report += "\n\t\t\t  ColumnType " + column1.getColumnType() + "!=" + column2.getColumnType();
            isEquals = false;
        }

        if (column1.isNullable() != column2.isNullable()) {
            report += "\n\t\t\t isNullable " + column1.isNullable + "!=" + column2.isNullable();
            isEquals = false;
        }
        if (!include) {
            report = "";
        }
        return new ComparatorReport(report, isEquals);
    }

    public static Map<String, TableMetaInfo> proceedDataBase(Connection connection) throws SQLException {
        return proceedDataBase(connection, null);
    }

    public static Map<String, TableMetaInfo> proceedDataBase(Connection connection, String scheme) throws SQLException {

        Map<String, TableMetaInfo> tableMetaInfos = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        var tables = metaData.getTables(null, scheme, "%", new String[] {"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            String tableType = tables.getString("TABLE_TYPE");
            String tableScheme = tables.getString("TABLE_SCHEM");
            try {
                TableMetaInfo tableMetaInfo = new TableMetaInfo(tableName, tableType, tableScheme, connection, 0);
                tableMetaInfos.put(tableName, tableMetaInfo);
            } catch (Exception ex) {
                log.error("Обработка таблицы '{}' неуспешна", tableName, ex);
            }
        }
        return tableMetaInfos;
    }

    @SneakyThrows
    public static String print(ResultSet rs) {
        List allRows = new ArrayList();
        List<String> row = new ArrayList();
        while (rs.next()) {
            String[] currentRow = new String[rs.getMetaData().getColumnCount()];
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
                log.info("{} :{} == {}", i, rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            allRows.add(row);
        }
        return allRows.toString();
    }

    @Getter
    @AllArgsConstructor
    public static class ComparatorReport {
        String report;
        boolean result;
    }
}
