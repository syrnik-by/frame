package ru.autotestframework.configuration;

import static ru.autotestframework.Constants.ARRAY_STRING_DELIMETER_PROPERTY;
import static ru.autotestframework.Constants.COMPARISON_DECIMAL_PRECISION_SCALE;
import static ru.autotestframework.Constants.DEFAULT_GLUE;
import static ru.autotestframework.Constants.ENABLE_ACCESS_CHECK;
import static ru.autotestframework.Constants.FAKE_DB_DRIVER_USE;
import static ru.autotestframework.Constants.HTTPS_PROXY_HOST;
import static ru.autotestframework.Constants.HTTPS_PROXY_PORT;
import static ru.autotestframework.Constants.HTTPS_PROXY_USER;
import static ru.autotestframework.Constants.HTTP_NON_PROXY_HOSTS;
import static ru.autotestframework.Constants.HTTP_PROXY_HOST;
import static ru.autotestframework.Constants.HTTP_PROXY_PORT;
import static ru.autotestframework.Constants.HTTP_PROXY_USER;
import static ru.autotestframework.util.Validator.allNotBlank;

import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/**
 * Framework config.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = {DEFAULT_GLUE + ".*"})
@EnableConfigurationProperties
@EnableAspectJAutoProxy
public class FrameworkConfig {

    private final FrameworkProperties properties;
    private final Environment environment;
    private final ProxyProperties proxy;

    /**
     * Configure FrameWork on its Startup.
     */
    @EventListener(classes = {ContextRefreshedEvent.class})
    public void configureOnStartUp() {
        CopyGitHooks.main(new String[] {properties.getRemoveGitHooks()});
        logCurrentEnvironment();
        setUpProxy();
        configureSslValidation();
        configureUseFakeDbDriver();
        System.setProperty(COMPARISON_DECIMAL_PRECISION_SCALE, String.valueOf(properties.getDecimalPrecisionScale()));
        System.setProperty(ARRAY_STRING_DELIMETER_PROPERTY, properties.getArrayStringDelimiter());
        System.setProperty(ENABLE_ACCESS_CHECK, String.valueOf(properties.isAccessCheckEnabled()));
    }

    private void logCurrentEnvironment() {
        var activeProfiles = List.of(environment.getActiveProfiles());
        var defaultProfiles = List.of(environment.getDefaultProfiles());

        if (activeProfiles.isEmpty()) {
            log.info("No environment setting provided. Autotests will be run in '{}' environment", defaultProfiles);
        } else {
            log.info("Autotests will be run in '{}' environment", activeProfiles);
        }
    }

    private void setUpProxy() {
        if (!proxy.isEnabled()) {
            return;
        }

        var host = proxy.getHost();
        var port = proxy.getPort();
        var user = proxy.getUser();
        var nonProxyHosts = proxy.getNonProxyHosts();

        allNotBlank("Proxy settings are not provided", host, port);

        System.setProperty(HTTP_PROXY_HOST, host);
        System.setProperty(HTTPS_PROXY_HOST, host);
        System.setProperty(HTTP_PROXY_PORT, port);
        System.setProperty(HTTPS_PROXY_PORT, port);
        System.setProperty(HTTP_PROXY_USER, user);
        System.setProperty(HTTPS_PROXY_USER, user);

        if (StringUtils.isBlank(nonProxyHosts)) {
            log.warn("NonProxy hosts not provided");
        } else {
            System.setProperty(HTTP_NON_PROXY_HOSTS, nonProxyHosts);
        }
    }

    private void configureUseFakeDbDriver() {
        System.setProperty(FAKE_DB_DRIVER_USE, properties.getFakeDbDriverUse());
    }

    @SuppressWarnings({"java:S4830", "java:S4423", "java:S5527"})
    @SneakyThrows
    private void configureSslValidation() {
        if (properties.isSslEnabled()) {
            return;
        }

        log.info("SSL validation is disabled");
        var trustManagers = new TrustManager[] {
            new X509TrustManager() {
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    // ignore
                }

                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    // ignore
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };

        var sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }
}
