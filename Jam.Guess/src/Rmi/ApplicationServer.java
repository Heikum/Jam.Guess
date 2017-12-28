package Rmi;

import main.Jam.Guess.Jam.Guess.fontyspublisher.RemotePublisher;
import main.Jam.Guess.Jam.Guess.fontyspublisher.SharedData;
import main.Jam.Guess.Jam.Guess.log.Logger;
import aud.io.rmi.IPartyManager;
import aud.io.rmi.PartyManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;

public class ApplicationServer {
    private static int port;
    private static String serverName;
    private static String publisherName;
    private static Logger logger;

    public static void main(String[] args) {
        logger = new Logger("ApplicationServer", Level.ALL, Level.ALL);
        initSharedData();

        try {
            logger.log(Level.FINE, "Server will start.");
            RemotePublisher publisher = new RemotePublisher();
            IPartyManager server = new PartyManager(publisher);
            Registry registry = LocateRegistry.createRegistry(port);
            logger.log(Level.INFO, "Registry created");
            registry.rebind(publisherName, publisher);
            logger.log(Level.INFO, "Publisher bound to registry");
            registry.rebind(serverName, server);
            logger.log(Level.INFO, "Server name bound to registry");
        } catch (RemoteException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        logger.log(Level.FINE, "Server has started, type 'exit' to stop.");

        boolean loop = true;

        while (loop) {
            if (scanner.nextLine().equals("exit")) {
                loop = false;
                logger.log(Level.INFO, "Server is shutting down.");
            } else {
                logger.log(Level.INFO, "Unknown command, type 'exit' to stop.");
            }
        }
        System.exit(0);
    }

    private static void initSharedData() {
        port = SharedData.getPort();
        serverName = SharedData.getServerName();
        publisherName = SharedData.getPublisherName();
        System.setProperty("java.rmi.server.hostname", SharedData.getRegistryName());
    }
}
