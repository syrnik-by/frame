package ru.autotestframework.access_checker_plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.springframework.core.env.StandardEnvironment;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.util.access_checker.AccessChecker;

public class AccessCheckTask extends DefaultTask {

    /**
     * activates access check plugin function
     */
    @TaskAction
    public void taskMainMethod() {
        var accessChecker = new AccessChecker(new FrameworkProperties(), new StandardEnvironment());
        accessChecker.setTaskRegime(true);
        accessChecker.check();
    }
}
