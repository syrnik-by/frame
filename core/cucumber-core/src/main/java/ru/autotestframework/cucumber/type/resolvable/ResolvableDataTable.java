package ru.autotestframework.cucumber.type.resolvable;

import io.cucumber.datatable.DataTable;
import ru.autotestframework.cucumber.PlaceholderResolver;

/**
 * Resolvable data table.
 */
public class ResolvableDataTable implements IResolvable<ResolvableDataTable> {

    private final DataTable value;
    private final PlaceholderResolver placeholderResolver;

    /**
     * Instantiates a new Resolvable data table.
     *
     * @param unresolvedDataTable the unresolved data table
     * @param placeholderResolver the placeholder resolver
     */
    public ResolvableDataTable(final DataTable unresolvedDataTable, final PlaceholderResolver placeholderResolver) {
        this.value = unresolvedDataTable;
        this.placeholderResolver = placeholderResolver;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public DataTable getValue() {
        return value;
    }

    @Override
    public ResolvableDataTable resolve(final ResolvableDataTable object) {
        return new ResolvableDataTable(placeholderResolver.resolve(object.getValue()), placeholderResolver);
    }
}
