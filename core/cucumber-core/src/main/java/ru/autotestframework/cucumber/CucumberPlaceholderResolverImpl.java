package ru.autotestframework.cucumber;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.DataTableTypeRegistryTableConverter;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.PlaceholderResolverImpl;

/**
 * Cucumber placeholder resolver.
 */
@Slf4j
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Primary
@ConditionalOnProperty(value = "framework.junit", havingValue = "false", matchIfMissing = true)
public class CucumberPlaceholderResolverImpl extends PlaceholderResolverImpl implements PlaceholderResolver {
    /**
     * Instantiates a new Cucumber placeholder resolver.
     *
     * @param resolver the resolver
     */
    public CucumberPlaceholderResolverImpl(StringSubstitutor resolver) {
        super(resolver);
    }

    @Override
    public DataTable resolve(final DataTable dataTable) {
        List<List<String>> mutableList = (new DataTableTypeRegistryTableConverter(
                        new DataTableTypeRegistry(Locale.ENGLISH)))
                .toLists(dataTable, String.class);
        mutableList.forEach(list -> list.replaceAll(this::resolve));
        return DataTable.create(mutableList);
    }
}
