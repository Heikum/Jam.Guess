package main.Jam.Guess.Jam.Guess.rmi;


import main.Jam.Guess.IGUIController;
import main.Jam.Guess.Jam.Guess.IGameManager;
import main.Jam.Guess.Jam.Guess.User;
import main.Jam.Guess.Jam.Guess.audioplayer.AudioPlayer ;
import main.Jam.Guess.Jam.Guess.fontyspublisher.IRemotePropertyListener ;
import main.Jam.Guess.Jam.Guess.fontyspublisher.IRemotePublisherForListener ;
import main.Jam.Guess.Jam.Guess.log.Logger ;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import main.Jam.Guess.IPlayer;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class ClientManager extends UnicastRemoteObject implements IRemotePropertyListener {

    private IRemotePublisherForListener publisher;
    private IGameManager server;

    private IGUIController guiController;


    private User registeredUser;
    private static IPlayer player;
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    private Logger logger;
    private static final String MSG_NOT_IN_PARTY = "You're not in a party";
    private static final String MSG_NOT_LOGGED_IN = "You're not logged in";
    private static final String MSG_ALREADY_LOGGED_IN = "You're already logged in";

    public ClientManager(IRemotePublisherForListener publisher, IGameManager server) throws RemoteException {
        logger = new Logger("ClientManager", Level.ALL, Level.SEVERE);
        this.publisher = publisher;
        this.server = server;
        (new NativeDiscovery()).discover();

        player  = new AudioPlayer(pool, new AudioMediaPlayerComponent());
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) throws RemoteException {
//        System.out.println(currentParty.getPartyMessage());
        if (guiController != null){
            guiController.update();
        }
    }

    public void setGuiController(IGUIController controller){
        guiController = controller;
    }


    public User login(String username, String password) throws RemoteException {
        if (getUser() != null) {
            //return MSG_ALREADY_LOGGED_IN;
            return null;
        }
        User user = server.login(username, password);

        if (user == null) {
            logger.log(Level.WARNING, String.format("%s tried to login", username));
            //return "Incorrect username or password.";
            return null;
        }

        registeredUser = (User) user;
        logger.log(Level.INFO, String.format("%s logged in", user.getUsername());
        return registeredUser;
        //return String.format("You logged in as: %s", user.getNickname());
    }

    public String logout() throws RemoteException {
        if (getUser() == null) {
            return MSG_NOT_LOGGED_IN;
        }
        String partyKey = "";



        User u = getUser();
        server.logout(u);

        logger.log(Level.INFO, String.format("%s logged out", u.getUsername()));
        return "You logged out.";
    }


    public void resumePlaying(){
        player.play();
    }

    public void pause(){
        player.pause();
    }

    public void stop(){
        player.stop();
    }

    public String play() throws RemoteException {
        User user = getUser();
        if (user == null) {
            return MSG_NOT_LOGGED_IN;
        }

        return "This action is not allowed.";
    }



    public User getUser() {
        if (registeredUser != null) {
            return registeredUser;
        } else return null;
    }

}
