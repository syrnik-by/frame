package ru.autotestframework;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * Constants.
 */
@UtilityClass
public class Constants {

    /**
     * The constant HTTP_PROXY_HOST.
     */
    public static final String HTTP_PROXY_HOST = "http_proxyHost";
    /**
     * The constant HTTPS_PROXY_HOST.
     */
    public static final String HTTPS_PROXY_HOST = "https_proxyHost";
    /**
     * The constant HTTP_PROXY_PORT.
     */
    public static final String HTTP_PROXY_PORT = "http_proxyPort";
    /**
     * The constant HTTPS_PROXY_PORT.
     */
    public static final String HTTPS_PROXY_PORT = "https_proxyPort";
    /**
     * The constant HTTP_PROXY_USER.
     */
    public static final String HTTP_PROXY_USER = "http_proxyUser";
    /**
     * The constant HTTPS_PROXY_USER.
     */
    public static final String HTTPS_PROXY_USER = "https_proxyUser";
    /**
     * The constant HTTP_NON_PROXY_HOSTS.
     */
    public static final String HTTP_NON_PROXY_HOSTS = "http_nonProxyHosts";

    /**
     * The constant FILTER_TEMP_FILE_REGEX.
     */
    public static final String FILTER_TEMP_FILE_REGEX = "^(?!(\\.ru.yandex)).*(?<!((\\.tmp)|(load)|(.{2}\\.~)))$";
    /**
     * The constant STRING_REGEX.
     */
    public static final String STRING_REGEX = "\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"";
    /**
     * The constant STRING_REGEX_2.
     */
    public static final String STRING_REGEX_2 = "'([^'\\\\]*(\\\\.[^'\\\\]*)*)'";
    /**
     * The constant DATE_DD_MM_YYYY.
     */
    public static final String DATE_DD_MM_YYYY = "\\d{2}.\\d{2}.\\d{4}";
    /**
     * The constant TIMESTAMP_REGEX.
     */
    public static final String TIMESTAMP_REGEX =
            "^((\\d{2}|\\d{4})-(\\d{2})-(\\d{2}|\\d{4})" + " (\\d{2})\\:(\\d{2})\\:(\\d{2}).(\\d{1,9}))$";
    /**
     * The constant STRING_ARRAY_REGEX.
     */
    public static final String STRING_ARRAY_REGEX = "\\[(\"([^\"]*)\"(, |))+\\]"; // Строка не может содержать ковычки
    /**
     * The constant INT_REGEX.
     */
    public static final String INT_REGEX = "(\\d+)";
    /**
     * The constant BOOLEAN_REGEX.
     */
    public static final String BOOLEAN_REGEX = "^(true|false)$";
    /**
     * The constant INT_ARRAY_REGEX.
     */
    public static final String INT_ARRAY_REGEX = "\\[((\\d+)(, |))+\\]";
    /**
     * The constant FLOAT_REGEX.
     */
    public static final String FLOAT_REGEX = "^(\\d+\\.\\d+)$";
    /**
     * The constant FLOAT_ARRAY_REGEX.
     */
    public static final String FLOAT_ARRAY_REGEX = "\\[((\\d+\\.\\d+)(, |))+\\]";
    /**
     * The constant HTTP_METHOD_REGEX.
     */
    public static final String HTTP_METHOD_REGEX = "(GET|PUT|POST|DELETE|HEAD|TRACE|OPTIONS|PATCH)";
    /**
     * The constant NOT_USED.
     */
    public static final String NOT_USED = "notUsed";

    /**
     * The constant DEFAULT_GLUE.
     */
    public static final String DEFAULT_GLUE = "ru.autotestframework";
    /**
     * The constant DEFAULT_TAGS.
     */
    public static final String DEFAULT_TAGS = "not (@Skip or @Demo)";
    /**
     * The constant DEFAULT_FEATURES.
     */
    public static final String DEFAULT_FEATURES = "classpath:features";
    /**
     * The constant ALLURE_PLUGIN.
     */
    public static final String ALLURE_PLUGIN = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm";
    /**
     * The constant TEST_IT_PLUGIN.
     */
    public static final String TEST_IT_PLUGIN = "ru.psb.testit.listener.BaseCucumber7Listener";
    /**
     * The constant KEYS_DELIMETER.
     */
    public static final String KEYS_DELIMETER = "\\+";
    /**
     * The constant FAKE_DB_DRIVER_USE.
     */
    public static final String FAKE_DB_DRIVER_USE = "framework.fake.db.driver.use:";
    /**
     * The constant ENABLE_BROWSER_REUSE.
     */
    public static final String ENABLE_BROWSER_REUSE = "ENABLE_BROWSER_REUSE";
    /**
     * The constant ARRAY_STRING_DELIMETER_PROPERTY.
     */
    public static final String ARRAY_STRING_DELIMETER_PROPERTY = "framework.array.string.delimiter:";
    /**
     * The constant ARRAY_STRING_DELIMETER.
     */
    public static final String ARRAY_STRING_DELIMETER = System.getProperty(ARRAY_STRING_DELIMETER_PROPERTY);
    /**
     * The constant TEST_FAIL_STATUS_NAME.
     */
    public static final String TEST_FAIL_STATUS_NAME = "framework.test.fail.status.name:";
    /**
     * The constant COMPARISON_DECIMAL_PRECISION_SCALE.
     */
    public static final String COMPARISON_DECIMAL_PRECISION_SCALE = "framework.matcher.decimal.scale:";
    /**
     * The constant SUPPORTED_TEXT_FILES.
     */
    public static final List<String> SUPPORTED_TEXT_FILES =
            Lists.newArrayList("txt", "json", "csv", "sql", "xml", "xsd", "html", "yaml");

    /**
     * The constant ABSOLUTE_FILE_PATH_PREFIX.
     */
    public static final String ABSOLUTE_FILE_PATH_PREFIX = "file:";
    /**
     * The constant TEMP_FOLDER.
     */
    public static final String TEMP_FOLDER = "temp";
    /**
     * The constant TEMP_UI_FOLDER.
     */
    public static final String TEMP_UI_FOLDER = TEMP_FOLDER.concat("/ui");
    /**
     * The constant TEMP_FTP_FOLDER.
     */
    public static final String TEMP_FTP_FOLDER = TEMP_FOLDER.concat("/ftp");
    /**
     * The constant TEMP_HTTP_FOLDER.
     */
    public static final String TEMP_HTTP_FOLDER = TEMP_FOLDER.concat("/http");
    /**
     * The constant DISABLE_HOOKS_COPY.
     */
    public static final String DISABLE_HOOKS_COPY = "DISABLE_HOOKS_COPY";
    /**
     * The constant ENABLE_ACCESS_CHECK.
     */
    public static final String ENABLE_ACCESS_CHECK = "ENABLE_ACCESS_CHECK";
    /**
     * The constant CELLS_VALUES.
     */
    public static final String CELLS_VALUES = "{cells}";

    public static int DEFAULT_DOWNLOAD_WAIT = 30000;
    /**
     * The constant STRIPPED.
     */
    public static final String STRIPPED = "[|;&$<>'!#]*";
    /**
     * The constant SANITIZE_COMMAND_INJECTION.
     */
    public static final Pattern SANITIZE_COMMAND_INJECTION = Pattern.compile("^[a-zA-Zа-яА-Я0-9/._-]+$");
}
