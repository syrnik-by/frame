package ru.autotestframework.testlistener;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import ru.autotestframework.testdispatcher.rmi.RMIListener;

@Slf4j
public class Listener extends UnicastRemoteObject implements RMIListener {

    private final String LISTENER_URL;
    private FeatureExecutor featureExecutor;

    public Listener(String listenerUrl) throws RemoteException {
        super();
        LISTENER_URL = listenerUrl;
    }

    @SneakyThrows
    public static void main(String[] args) throws RemoteException, MalformedURLException {

        var url1 = InetAddress.getLocalHost().getHostName();
        Listener listener = new Listener(url1);

        var registry = LocateRegistry.createRegistry(1101);
        registry.rebind(url1, listener);

        log.info("Listener started " + url1 + " : " + System.currentTimeMillis());
        try (PrintWriter out = new PrintWriter(Thread.currentThread().getName() + "log.txt")) {
            log.info(Thread.currentThread().getName() + " at " + url1);
        }
    }

    @Override
    public void initProcessing(String brokerUrl, String projectDir) {
        System.setProperty("projectDir", projectDir);
        log.info("Initialization started");
        try {
            Awaitility.await().until(() -> featureExecutor == null || !featureExecutor.isAlive());
        } catch (ConditionTimeoutException e) {
            log.error("Initialization error");
        }

        featureExecutor = FeatureExecutor.getInstance(brokerUrl);
        featureExecutor.start();
    }

    @Override
    public boolean healthCheck() {
        log.info("Listener health check passed");
        return true;
    }

    @Override
    public void shutDown() {
        try {
            Naming.unbind(LISTENER_URL);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            log.error("Ошибка гашения нод ", e);
        }
    }
}
