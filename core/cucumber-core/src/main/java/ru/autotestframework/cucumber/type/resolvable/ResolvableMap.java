package ru.autotestframework.cucumber.type.resolvable;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.PlaceholderResolver;

/**
 * Resolvable map.
 */
@Slf4j
@RequiredArgsConstructor
public class ResolvableMap extends HashMap<String, String> implements IResolvable<ResolvableMap> {

    private final PlaceholderResolver placeholderResolver;

    /**
     * Instantiates a new Resolvable map.
     *
     * @param resolvableMap       the resolvable map
     * @param placeholderResolver the placeholder resolver
     */
    public ResolvableMap(final Map<String, String> resolvableMap, final PlaceholderResolver placeholderResolver) {
        super(resolvableMap);
        this.placeholderResolver = placeholderResolver;
    }

    @Override
    public ResolvableMap resolve(final ResolvableMap object) {
        return new ResolvableMap(placeholderResolver.resolve(object), placeholderResolver);
    }
}
