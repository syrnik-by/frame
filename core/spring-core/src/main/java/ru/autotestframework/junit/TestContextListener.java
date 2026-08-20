package ru.autotestframework.junit;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import ru.autotestframework.core.spring.ThreadScope;

/**
 * To register thread scope to initialise Beans in parallel tests within one application
 */
public class TestContextListener extends AbstractTestExecutionListener {
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        if (testContext.getApplicationContext() instanceof GenericApplicationContext) {
            GenericApplicationContext context = (GenericApplicationContext) testContext.getApplicationContext();
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

            Scope threadScope = new ThreadScope();
            beanFactory.registerScope("thread", threadScope);
        }
    }
}
