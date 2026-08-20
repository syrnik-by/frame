package ru.autotestframework.cucumber.utils;

import static ru.autotestframework.Constants.ARRAY_STRING_DELIMETER;
import static ru.autotestframework.Constants.CELLS_VALUES;

import com.google.common.base.Splitter;
import io.cucumber.datatable.DataTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.type.CucumberTypesDefinition;
import ru.autotestframework.cucumber.type.Pair;

/**
 * Table util.
 */
@Slf4j
@UtilityClass
public final class TableUtil {

    /**
     * Gets cells as list from context.
     *
     * @param operand the operand
     * @return the cells as list from context
     */
    public static List<Pair> getCellsAsListFromContext(final String operand) {
        if (operand.contains(CELLS_VALUES)) {
            return Splitter.on(ARRAY_STRING_DELIMETER).splitToList(operand.replace(CELLS_VALUES, "")).stream()
                    .map(cell -> {
                        List<String> splitString = Splitter.on(":").splitToList(cell);
                        return Pair.of(splitString.get(0), splitString.get(1));
                    })
                    .collect(Collectors.toList());
        } else {
            throw new AutotestException("Cannot convert context content to list");
        }
    }

    /**
     * Gets data table from pairs list.
     *
     * @param headerCellPairs the header cell pairs
     * @return the data table from pairs list
     */
    public static List<Map<String, String>> getDataTableFromPairsList(List<Pair> headerCellPairs) {
        List<List<String>> table = new ArrayList<>();
        headerCellPairs.stream()
                .map(Pair::getFirst)
                .distinct()
                .forEach(header -> table.add(new ArrayList<>(List.of(header))));

        Map<String, List<Pair>> collect = headerCellPairs.stream().collect(Collectors.groupingBy(Pair::getFirst));
        table.forEach(list -> {
            list.addAll(collect.get(list.get(0)).stream().map(Pair::getSecond).collect(Collectors.toList()));
        });
        return CucumberTypesDefinition.TABLE_CONVERTER.toMaps(
                DataTable.create(table).transpose(), String.class, String.class);
    }
}
