package ru.autotestframework.testlistener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

@Slf4j
public class ToolingAPI {

    private static GradleConnector connector = GradleConnector.newConnector();

    static {
        connector.forProjectDirectory(new File(System.getProperty("projectDir")));
    }

    public static void executeFeatures(String featureTag, String resultsDir, String runId) {
        List<String> arguments = new ArrayList<>();
        arguments.add("-Ptags=" + featureTag);
        //        arguments.add("-Dallure.results.dir=" + resultsDir);
        if (runId != null) {
            arguments.add("-DidRun=" + runId);
        }
        try {
            executeTask("executeFeatures", arguments);
            //            executeTask("orchestratorTest",arguments);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void executeTask(String task, List<String> arguments) {
        ProjectConnection connection = connector.connect();
        try {
            BuildLauncher buildLauncher = connection.newBuild();
            buildLauncher.forTasks("clean", task);
            buildLauncher.withArguments(arguments);
            buildLauncher.setStandardOutput(System.out);
            buildLauncher.run();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            connection.close();
        }
    }
}
