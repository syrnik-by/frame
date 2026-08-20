package ru.autotestframework.access_checker_plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AccessCheckPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "accessExt";

    /**
     * creates access check plugin task
     * @param project
     */
    @Override
    public void apply(final Project project) {
        project.getTasks().create("accessCheck", AccessCheckTask.class).setGroup(EXTENSION_NAME);
    }
}
