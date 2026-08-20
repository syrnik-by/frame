package ru.autotestframework.cucumber;

import io.cucumber.datatable.DataTable;

/**
 * Представляет объект, позволяющий заменять плейсхолдеры вида '{@code ${{variable}}}' в строковых переменных.
 */
public interface PlaceholderResolver extends ru.autotestframework.core.PlaceholderResolver {

    /**
     * Заменяет плейсхолдеры вида '{@code ${{variable}}}' на актуальные значения переменных.
     * <br>
     *
     * @param dataTable исходная таблица
     * @return таблица с замененными плейсхолдерами
     */
    DataTable resolve(DataTable dataTable);
}
