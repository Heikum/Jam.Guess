package Rmi;


import main.Jam.Guess.IGUIController;
import main.Jam.Guess.Jam.Guess.IGameManager;
import main.Jam.Guess.Jam.Guess.fontyspublisher.IRemotePublisherForListener ;
import main.Jam.Guess.Jam.Guess.fontyspublisher.SharedData ;
import main.Jam.Guess.Jam.Guess.log.Logger;
import main.Jam.Guess.Jam.Guess.rmi.ClientManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;

public class ApplicationClient {

    private static ClientManager manager;

    private static CLIClientGUIController controller;

    private static int port;
    private static String registryName;
    private static String serverName;
    private static String publisherName;
    private static boolean allowRun;
    private static Logger logger;

    public static void main(String[] args) {
        logger = new Logger("ApplicationClient", Level.ALL, Level.SEVERE);
        initSharedData();

        try {
            Registry registry = LocateRegistry.getRegistry(registryName, port);
            IRemotePublisherForListener publisher = (IRemotePublisherForListener) registry.lookup(publisherName);
            IGameManager server = (IGameManager) registry.lookup(serverName);
            manager = new ClientManager(publisher, server);
            controller = new CLIClientGUIController(manager);
            manager.setGuiController((IGUIController) controller);

        } catch (RemoteException | NotBoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

    }



    private static void initSharedData() {
        port = SharedData.getPort();
        registryName = SharedData.getRegistryName();
        serverName = SharedData.getServerName();
        publisherName = SharedData.getPublisherName();
    }



}