package ru.autotestframework.orm_steps.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.orm_steps.step_defs.ORMSteps;
import ru.autotestframework.orm_steps.utils.ClassFinder;

/**
 * Orm steps config.
 */
@Configuration
@RequiredArgsConstructor
public class ORMStepsConfig {
    private final ORMStepsProperties ormStepsProperties;
    private final Context context;

    /**
     * Gets orm steps.
     *
     * @return the orm steps
     */
    @Bean
    @Primary
    public ORMSteps getORMSteps() {
        org.hibernate.cfg.Configuration cfg = getCfg();
        ClassFinder.find(ormStepsProperties.getPackagePath()).forEach(cfg::addAnnotatedClass);
        return new ORMSteps(cfg.buildSessionFactory(), ormStepsProperties.getPackagePath(), context);
    }

    /**
     * Get hibernate configuration.
     *
     * @return org.hibernate.cfg.Configuration
     */
    private org.hibernate.cfg.Configuration getCfg() {
        var properties = new Properties();
        try (var fileReader = new FileReader(ormStepsProperties.getPropertiesPath())) {
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new org.hibernate.cfg.Configuration().addProperties(properties);
    }
}
