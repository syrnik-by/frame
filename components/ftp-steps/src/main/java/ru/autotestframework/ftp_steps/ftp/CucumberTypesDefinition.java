package ru.autotestframework.ftp_steps.ftp;

import static ru.autotestframework.util.Validator.notBlank;

import io.cucumber.java.DataTableType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.exception.ExecutionException;

/**
 * Cucumber types definition.
 */
@Slf4j
@RequiredArgsConstructor
public class CucumberTypesDefinition {
    /**
     * The constant DEF_PORT.
     */
    public static final int DEF_PORT = 21;

    private final PlaceholderResolver placeholderResolver;

    /**
     * Create FtpClientProperties on a given params through feature file (see @DataTableType).
     *
     * @param unresolvedProperties map with connection parameters
     * @return {@link FtpClientProperties}
     */
    @DataTableType
    public FtpClientProperties clientProperties(final Map<String, String> unresolvedProperties) {
        var properties = placeholderResolver.resolve(unresolvedProperties);

        var url = properties.get("url");
        var port = properties.get("port");
        var user = properties.get("user");
        var password = properties.get("password");
        var encoding = properties.get("encoding");

        notBlank(url, "Адрес сервера не установлен");
        int intPort;
        try {
            intPort = (port == null) ? DEF_PORT : Integer.parseInt(port);
        } catch (NumberFormatException exception) {
            throw new ExecutionException("Port '{}' contains invalid characters", port);
        }

        if (user != null || password != null) {
            notBlank(user, "Имя пользователя не установлено");
            notBlank(password, "Пароль не установлен");
        } else {
            user = "anonymous";
        }

        encoding = (encoding == null) ? "WINDOWS-1251" : encoding;

        return new FtpClientProperties(url, intPort, user, password, encoding);
    }
}
