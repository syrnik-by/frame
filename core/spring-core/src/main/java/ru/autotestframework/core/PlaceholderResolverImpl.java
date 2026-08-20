package ru.autotestframework.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.PlaceholderResolverConfig;

/**
 * Placeholder resolver.
 */
@Slf4j
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class PlaceholderResolverImpl implements PlaceholderResolver {

    @Getter
    private final StringSubstitutor resolver;

    @Getter
    private static final Pattern pattern = Pattern.compile("\\{\\{.+?}}");

    /**
     * Resolve string.
     *
     * @param input      the input
     * @param maskSecret the mask secret
     * @return the string
     */
    public String resolve(final String input, boolean maskSecret) {
        if (maskSecret) {
            return resolve(input.replace("${{secret:", "${{dummy:"));
        } else {
            return resolve(input);
        }
    }

    @Override
    public String resolve(final String input) {
        if (PlaceholderResolverConfig.NULL_PLACEHOLDER.equals(input)) {
            return null;
        }

        if (input == null) {
            return null;
        }

        try {
            return resolver.replace(input);
        } catch (Exception e) {
            String[] parts = input.split("\\$");
            if (parts[0].equals("") && parts.length == 2) {
                return nullReplace(input);
            }

            var sb = new StringBuilder();
            for (String part : parts) {
                sb.append(nullReplaceAndConcat(part));
            }
            return sb.toString();
        }
    }

    @Override
    public Map<String, String> resolve(final Map<String, String> inputMap) {
        return inputMap.entrySet().stream()
                .collect(
                        HashMap::new,
                        (map, entry) -> map.put(resolve(entry.getKey()), resolve(entry.getValue())),
                        HashMap::putAll);
    }

    private String nullReplace(final String input) {
        try {
            return resolver.replace(input);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String nullReplaceAndConcat(final String input) {
        var matcher = pattern.matcher(input);
        if (matcher.find()) {
            try {
                return resolver.replace("$" + input);
            } catch (IllegalArgumentException e) {
                log.warn("Variable {} is null", input.substring(input.indexOf("{") + 2, input.indexOf("}")));
                return matcher.replaceFirst("null");
            }
        } else {
            return input;
        }
    }
}
