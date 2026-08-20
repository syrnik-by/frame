package ru.autotestframework.testdispatcher.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIListener extends Remote {

    void initProcessing(String brokerUrl, String projectDir) throws RemoteException;

    boolean healthCheck() throws RemoteException;

    void shutDown() throws RemoteException;
}
