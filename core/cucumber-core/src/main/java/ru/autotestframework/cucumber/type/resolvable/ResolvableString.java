package ru.autotestframework.cucumber.type.resolvable;

import ru.autotestframework.core.PlaceholderResolver;

/**
 * Resolvable string.
 */
public class ResolvableString implements IResolvable<ResolvableString> {

    private final String value;
    private final PlaceholderResolver placeholderResolver;

    /**
     * Instantiates a new Resolvable string.
     *
     * @param unresolvableString  the unresolvable string
     * @param placeholderResolver the placeholder resolver
     */
    public ResolvableString(final String unresolvableString, final PlaceholderResolver placeholderResolver) {
        this.value = unresolvableString;
        this.placeholderResolver = placeholderResolver;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public ResolvableString resolve(final ResolvableString object) {
        return new ResolvableString(placeholderResolver.resolve(object.toString()), placeholderResolver);
    }
}
