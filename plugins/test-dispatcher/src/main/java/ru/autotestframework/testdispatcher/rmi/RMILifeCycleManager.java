package ru.autotestframework.testdispatcher.rmi;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;

@Slf4j
public class RMILifeCycleManager {
    private static final String SERVICE_NAME = "bddlistener";
    private static final String LISTENER_URL_TEMPLATE = "//%s:1101/%s%s";
    // TODO remove atomic (добавить все листенеры из списка naming)
    private AtomicInteger localIds = new AtomicInteger(4);
    private Map<String, RMIListener> rmiHosts = new HashMap<>();

    public Collection<RMIListener> initHosts(boolean failOnException) {
        String hosts = System.getProperty("hosts");
        List<String> hostsList;
        if (hosts != null && !hosts.isEmpty()) {
            hostsList = Arrays.asList(hosts.split(","));
        } else {
            hostsList = new ArrayList<>();
            hostsList.add("localhost");
        }
        hostsList.forEach(host -> initHost(host, failOnException));
        return rmiHosts.values();
    }

    private void initHost(String host, boolean failOnException) {

        String url = String.format(LISTENER_URL_TEMPLATE, host, SERVICE_NAME, localIds.get());
        String name = String.format("%s%s", SERVICE_NAME, localIds.getAndIncrement());
        try {
            Awaitility.await()
                    .ignoreException(ConnectException.class)
                    .until(() -> Naming.list(url.toLowerCase()).length > 0);

            RMIListener rmiListener = (RMIListener) Naming.lookup(Arrays.stream(Naming.list(url.toLowerCase()))
                    .filter(x -> x.contains(name))
                    .findFirst()
                    .get());
            rmiHosts.put(url, rmiListener);

            log.info("Host added " + host + " : " + url);
        } catch (Exception e) {
            log.error("Обработка очереди не удалась", e);
        }
    }

    public void setExecutionNodes() {
        ArrayList<String> hostsList = new ArrayList<>(rmiHosts.keySet());
    }

    public void healthCheck() {
        rmiHosts.forEach((host, rmi) -> {
            try {
                rmi.healthCheck();
            } catch (RemoteException e) {
                log.error(e.getMessage());
            }
        });
    }

    public void shutDownNodes() {
        rmiHosts.forEach((host, rmi) -> {
            try {
                rmi.shutDown();
            } catch (RemoteException e) {
                log.error(e.getMessage());
            }
        });
    }
}
