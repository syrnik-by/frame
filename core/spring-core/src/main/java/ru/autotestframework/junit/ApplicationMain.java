package ru.autotestframework.junit;

import static ru.autotestframework.Constants.DEFAULT_GLUE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Entering point for starting Spring Application (Context, etc.)
 */
@Configuration
@SpringBootApplication(scanBasePackages = DEFAULT_GLUE)
public class ApplicationMain {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
