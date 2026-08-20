package ru.autotestframework.java_junit.driver_builder;

import net.sourceforge.marathon.javadriver.JavaDriver;
import net.sourceforge.marathon.javadriver.JavaProfile;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;

public class JavaDriverBuilder implements IDriverBuilder {

    private final Configuration configuration;

    public JavaDriverBuilder(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public WebDriver build() {
        return new JavaDriver(new JavaProfileBuilder(configuration).build());
    }

    private static final class JavaProfileBuilder {

        private static final String JNLP = "JNLP";
        private static final String JAR = "JAR";

        private final Configuration configuration;

        private JavaProfileBuilder(final Configuration configuration) {
            this.configuration = configuration;
        }

        private JavaProfile build() {
            if (configuration.getProperties() == null) {
                return new JavaProfile();
            }
            String launchMode =
                    configuration.getProperties().getProperty("launchMode").toUpperCase();
            switch (launchMode) {
                case JNLP:
                    return getJNLPJavaProfile();
                case JAR:
                    return getJarJavaProfile();
                default:
                    throw new ConfigurationException("incorrectly property launchMode - '{}'", launchMode);
            }
        }

        private JavaProfile getJNLPJavaProfile() {
            return new JavaProfile(JavaProfile.LaunchMode.JAVA_WEBSTART)
                    .setJNLPPath(configuration.getProperties().getProperty("jnlp"))
                    .setStartWindowTitle(configuration.getProperties().getProperty("startWindowTitle"))
                    .setJavaHome(configuration.getProperties().getProperty("javaHome"));
        }

        private JavaProfile getJarJavaProfile() {
            return new JavaProfile(JavaProfile.LaunchMode.EXECUTABLE_JAR)
                    .setExecutableJar(configuration.getProperties().getProperty("jar"));
        }
    }
}
