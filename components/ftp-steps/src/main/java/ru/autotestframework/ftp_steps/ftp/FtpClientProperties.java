package ru.autotestframework.ftp_steps.ftp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Ftp client properties.
 */
@Getter
@RequiredArgsConstructor
public class FtpClientProperties {

    private final String url;
    private final int port;
    private final String user;
    private final String password;
    private final String encoding;
}
