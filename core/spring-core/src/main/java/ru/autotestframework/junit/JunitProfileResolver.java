package ru.autotestframework.junit;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.test.context.ActiveProfilesResolver;

/**
 * Junit profile resolver.
 */
public class JunitProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> testClass) {
        String active = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        return new String[] {active};
    }
}
