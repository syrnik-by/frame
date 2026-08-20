package ru.autotestframework.desktop_elements.desktop_driver;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.remote.service.DriverService;

public class DesktopDriverService extends DriverService {

    /**
     * System property that defines the location of the log that will be written by service.
     */
    public static final String DESKTOP_DRIVER_LOG_PATH_PROPERTY = "desktop.driver.logpath";

    protected DesktopDriverService(
            final File executable,
            final int port,
            final Duration timeout,
            final List<String> args,
            final Map<String, String> environment)
            throws IOException {
        super(executable, port, timeout, args, environment);
    }

    /**
     * Creates a default instance of the DesktopDriverService using a default path to the Desktop Driver.
     *
     * @return A {@link DesktopDriverService} using DesktopDriver and random port
     */
    public static DesktopDriverService createDesktopService() {
        return new Builder().usingAnyFreePort().buildDesktopService();
    }

    public static class Builder extends DriverService.Builder<DesktopDriverService, Builder> {
        private static final String DESKTOP_DRIVER_SERVICE_FILENAME = "FlaNium.Driver.exe";

        private static final String DESKTOP_DRIVER_EXE_PROPERTY = "";

        private File exe = null;
        private boolean verbose = false;
        private boolean silent = false;
        private int port = 9999;
        private Duration timeout;

        @Override
        public int score(final Capabilities capabilities) {
            return 0;
        }

        /**
         * Sets which driver executable the builder will use.
         *
         * @param file The executable to use.
         * @return A self reference.
         */
        @Override
        public Builder usingDriverExecutable(final File file) {
            checkNotNull(file);
            checkExecutable(file);
            this.exe = file;
            return this;
        }

        /**
         * sets port
         * @param port The port to use; must be non-negative.
         * @return
         */
        @Override
        public Builder usingPort(int port) {
            this.port = Require.nonNegative("Port Number", port);
            return this;
        }

        /**
         * Configures the driver server verbosity.
         *
         * @param verbose true for verbose output, false otherwise.
         * @return A self reference.
         */
        public Builder withVerbose(final boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * Configures the driver server for silent output.
         *
         * @param silent true for silent output, false otherwise.
         * @return A self reference.
         */
        public Builder withSilent(final boolean silent) {
            this.silent = silent;
            return this;
        }

        /**
         * Configures the driver' timeout.
         *
         * @param timeout configure driver default timeout for tests
         * @return A self reference.
         */
        @Override
        public Builder withTimeout(final Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Creates a new {@link DesktopDriverService} to manage the Desktop Driver server.
         * Before creating a new service, the builder will find a port for the server to listen to.
         *
         * @return The new {@link DesktopDriverService} object.
         */
        public DesktopDriverService buildDesktopService() {
            int port = getPort();
            if (port == 0) {
                port = this.port;
            }

            if (timeout == null) {
                timeout = getDefaultTimeout();
            }

            if (exe == null) {
                exe = findDesktopDriverExecutable();
            }

            try {
                return new DesktopDriverService(exe, port, timeout, createArgs(), Map.of());
            } catch (IOException e) {
                throw new WebDriverException(e);
            }
        }

        @Override
        protected File findDefaultExecutable() {
            return findDesktopDriverExecutable();
        }

        @Override
        protected ImmutableList<String> createArgs() {
            if (getLogFile() == null) {
                String logFilePath = System.getProperty(DESKTOP_DRIVER_LOG_PATH_PROPERTY);
                if (logFilePath != null) {
                    withLogFile(new File(logFilePath));
                }
            }

            ImmutableList.Builder<String> argsBuidler = new ImmutableList.Builder<>();

            if (silent) {
                argsBuidler.add("--silent");
            }
            if (verbose) {
                argsBuidler.add("--verbose");
            }
            if (getLogFile() != null) {
                argsBuidler.add(String.format("--log-path=%s", getLogFile().getAbsolutePath()));
            }

            argsBuidler.add(String.format("--port=%d", port));

            return argsBuidler.build();
        }

        @Override
        protected DesktopDriverService createDriverService(
                final File exe,
                final int port,
                final Duration timeout,
                final List<String> args,
                final Map<String, String> environment) {
            try {
                return new DesktopDriverService(exe, port, timeout, args, environment);
            } catch (IOException e) {
                throw new WebDriverException(e);
            }
        }

        private File findDesktopDriverExecutable() {
            return findExecutable(DESKTOP_DRIVER_SERVICE_FILENAME, DESKTOP_DRIVER_EXE_PROPERTY, "", "");
        }
    }
}
