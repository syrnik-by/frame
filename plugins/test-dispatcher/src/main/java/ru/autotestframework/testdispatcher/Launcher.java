package ru.autotestframework.testdispatcher;

import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.awaitility.Awaitility;

@Slf4j
public class Launcher {

    public static final String FEATURES_QUEUE = "FeaturesQueue";
    public static final String RESULTS_QUEUE = "ResultsQueue";

    public static void main(final String[] args) throws Exception {
        String brokerUrl = "tcp://" + InetAddress.getLocalHost().getHostName() + ":4444";
        ActiveMQServer server = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
                .setPersistenceEnabled(false)
                .setSecurityEnabled(false)
                .addAcceptorConfiguration("local", brokerUrl));
        server.start();
        System.setProperty("features", "features");

        Dispatcher dispatcher = new Dispatcher(brokerUrl, System.getProperty("features"));

        try {

            log.info("Количество подключений:" + server.getConnectionCount());
            dispatcher.launch();
            Awaitility.await().until(() -> server.getConnectionCount() == 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        } finally {
            server.stop();
        }
    }
}
