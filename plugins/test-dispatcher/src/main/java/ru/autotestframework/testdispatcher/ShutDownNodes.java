package ru.autotestframework.testdispatcher;

import ru.autotestframework.testdispatcher.rmi.RMILifeCycleManager;

public class ShutDownNodes {

    public static void main(String[] args) {
        RMILifeCycleManager rmiLifeCycleManager = new RMILifeCycleManager();
        rmiLifeCycleManager.initHosts(false);
        rmiLifeCycleManager.shutDownNodes();
    }
}
