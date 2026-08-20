package ru.autotestframework.cucumber.type.resolvable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.autotestframework.core.PlaceholderResolver;

/**
 * Resolvable list.
 */
public class ResolvableList extends ArrayList<String> implements IResolvable<ResolvableList> {

    private final PlaceholderResolver placeholderResolver;

    /**
     * Instantiates a new Resolvable list.
     *
     * @param value               the value
     * @param placeholderResolver the placeholder resolver
     */
    public ResolvableList(final List<String> value, final PlaceholderResolver placeholderResolver) {
        super(value);
        this.placeholderResolver = placeholderResolver;
    }

    @Override
    public ResolvableList resolve(final ResolvableList object) {
        return new ResolvableList(
                object.stream().map(placeholderResolver::resolve).collect(Collectors.toList()), placeholderResolver);
    }
}
