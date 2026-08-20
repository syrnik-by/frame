package ru.autotestframework.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Proxy properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.proxy")
public class ProxyProperties {

    /**
     * The constant ENABLE_PROPERTY_NAME.
     */
    public static final String ENABLE_PROPERTY_NAME = "framework.proxy.enabled";
    /**
     * The constant HOST_PROPERTY_NAME.
     */
    public static final String HOST_PROPERTY_NAME = "framework.proxy.host";
    /**
     * The constant PORT_PROPERTY_NAME.
     */
    public static final String PORT_PROPERTY_NAME = "framework.proxy.port";
    /**
     * The constant USER_PROPERTY_NAME.
     */
    public static final String USER_PROPERTY_NAME = "framework.proxy.user";
    /**
     * The constant NON_PROXY_HOSTS_PROPERTY_NAME.
     */
    public static final String NON_PROXY_HOSTS_PROPERTY_NAME = "framework.proxy.non-proxy-hosts";

    private boolean enabled = false;

    private String host;

    private String port;

    private String user;

    private String nonProxyHosts;
}
