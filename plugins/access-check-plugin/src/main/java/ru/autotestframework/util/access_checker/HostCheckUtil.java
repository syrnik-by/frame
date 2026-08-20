package ru.autotestframework.util.access_checker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.ConfigurationException;

@Slf4j
@UtilityClass
public final class HostCheckUtil {

    public static final int DEFAULT_PORT = 443;
    public static final String SCHEME_DELIMETER = "://";
    public static final String MOCKED_SCHEME = "my".concat(SCHEME_DELIMETER);
    public static final int TIMEOUT = 10000;

    private static Boolean checkIfAvailable(final String host, final Integer port) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            try (var sock = new Socket()) {
                int timeoutMs = TIMEOUT;
                sock.connect(socketAddress, timeoutMs);
                return true;
            }
        } catch (SocketTimeoutException e) {
            log.error("Timeout for {}:{}", host, port);
            return false;
        } catch (IOException | NumberFormatException | ConfigurationException e) {
            return false;
        }
    }

    /**
     * checks connection to host and port
     * @param address
     * @return
     */
    public static Boolean checkIfAvailable(final String address) {
        URI uri;
        String sUrl = address.contains(SCHEME_DELIMETER) ? address : MOCKED_SCHEME.concat(address);
        try {
            uri = new URI(sUrl);
        } catch (URISyntaxException e) {
            throw new ConfigurationException("Passed URI have wrong format : {}", e, address);
        }

        String host = Optional.ofNullable(uri.getHost()).orElseThrow(() -> {
            throw new ConfigurationException("URI : {} must have host parts", uri.toString());
        });
        int port = uri.getPort();
        if (uri.getPort() == -1) {
            log.error("Port not defined, used default: {} ", DEFAULT_PORT);
            port = DEFAULT_PORT;
        }
        return checkIfAvailable(host, port);
    }
}
