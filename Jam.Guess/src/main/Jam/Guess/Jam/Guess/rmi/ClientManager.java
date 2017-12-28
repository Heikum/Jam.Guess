package main.Jam.Guess.Jam.Guess.rmi;


import main.Jam.Guess.Jam.Guess.audioplayer.AudioPlayer ;
import main.Jam.Guess.Jam.Guess.fontyspublisher.IRemotePropertyListener ;
import main.Jam.Guess.Jam.Guess.fontyspublisher.IRemotePublisherForListener ;
import main.Jam.Guess.Jam.Guess.log.Logger ;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

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
    private IPartyManager server;

    private IGUIController guiController;

    private Party currentParty;

    private RegisteredUser registeredUser;
    private TemporaryUser temporaryUser;
    private List<Votable> votables;

    private static IPlayer player;
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    private Logger logger;
    private static final String MSG_NOT_IN_PARTY = "You're not in a party";
    private static final String MSG_ALREADY_IN_PARTY = "You're already in a party";
    private static final String MSG_NOT_LOGGED_IN = "You're not logged in";
    private static final String MSG_ALREADY_LOGGED_IN = "You're already logged in";

    public ClientManager(IRemotePublisherForListener publisher, IPartyManager server) throws RemoteException {
        logger = new Logger("ClientManager", Level.ALL, Level.SEVERE);
        this.publisher = publisher;
        this.server = server;
        (new NativeDiscovery()).discover();

        player = new AudioPlayer(pool, new AudioMediaPlayerComponent());
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        currentParty = (Party) evt.getNewValue();
//        System.out.println(currentParty.getPartyMessage());
        if (guiController != null){
            guiController.update();
        }
    }

    public void setGuiController(IGUIController controller){
        guiController = controller;
    }

    public String createParty(String partyName) throws RemoteException {
        if (currentParty != null) {
            return MSG_ALREADY_IN_PARTY;
        }

        //TODO: Verify with server if logged in?

        if (getUser() != null) {
            if (getUser() instanceof RegisteredUser) {
                Party party = (Party) server.createParty((RegisteredUser) getUser(), partyName);

                publisher.subscribeRemoteListener(this, party.getPartyKey());
                currentParty = party;
                logger.log(Level.INFO, String.format("%s created the party: %s with the key: %s", getUser().getNickname(), party.getName(), party.getPartyKey()));
                return String.format("You have created the party: %s with the key: %s", party.getName(), party.getPartyKey());
            } else {
                logger.log(Level.FINE, String.format("%s wasn't a registered user", getUser().getNickname()));
                return "To create a party you have to be a registered user.";
            }
        } else {
            logger.log(Level.FINE, String.format("%s wasn't logged in", getUser().getNickname()));
            return "You need to be logged in to create a party.";
        }
    }

    public String leaveParty() throws RemoteException {
        if (currentParty == null) {
            return MSG_NOT_IN_PARTY;
        }

        if (getUser() == null) {
            return MSG_NOT_LOGGED_IN;
        }

        publisher.unsubscribeRemoteListener(this, currentParty.getPartyKey());
        server.leaveParty(getUser(), currentParty.getPartyKey());

        currentParty = null;
        logger.log(Level.INFO, String.format("%s left party: name: %s key: %s",
                getUser().getNickname(), getParty().getName(), getParty().getPartyKey()));
        return "You left the party.";
    }

    public String joinParty(String partyKey) throws RemoteException {
        if (currentParty != null) {
            return MSG_ALREADY_IN_PARTY;
        }

        if (getUser() == null) {
            return MSG_NOT_LOGGED_IN;
        }

        IParty iParty = server.joinParty(partyKey, getUser());
        if (iParty == null) {
            return "This is not a valid key";
        }

        Party party = (Party) iParty;
        publisher.subscribeRemoteListener(this, party.getPartyKey());
        currentParty = party;

        logger.log(Level.INFO, String.format("%s joined party: name: %s key: %s",
                getUser().getNickname(), party.getName(), party.getPartyKey()));
        return String.format("You have joined the party: %s with the key: %s", party.getName(), party.getPartyKey());
    }

    public List<Votable> getPartyVotables() {
        if (currentParty.getPlaylist() == null)
            //addMedia hasn't run yet
            return new ArrayList<>();
        return currentParty.getPlaylist();
    }

    public List<Votable> getAllVotables() throws RemoteException {

        if (currentParty == null) {
            //return MSG_NOT_IN_PARTY;
            logger.log(Level.WARNING, "no current party");
            return null;
        }

        if (getUser() == null) {
            //return MSG_NOT_LOGGED_IN;
            logger.log(Level.WARNING, "no current user");
            return null;
        }

        return server.getAllVotables();
    }

    public List<Votable> searchVotablesWithSearchTerm(String searchTerm) throws RemoteException {

        if (currentParty == null) {
            //return MSG_NOT_IN_PARTY;
            return null;
        }

        if (getUser() == null) {
            //return MSG_NOT_LOGGED_IN;
            return null;
        }

        return server.searchVotablesWithSearchTerm(searchTerm);
    }

    public void addMedia(Votable media) throws RemoteException {

        if (currentParty == null) {
            //return MSG_NOT_IN_PARTY;
            return;
        }

        if (getUser() == null) {
            //return MSG_NOT_LOGGED_IN;
            return;
        }

        server.addMedia(media, currentParty.getPartyKey(), getUser());
        logger.log(Level.INFO, String.format("%s added %s to party: name: %s key: %s",
                getUser().getNickname(), media.getName(), currentParty.getName(), currentParty.getPartyKey()));
    }

    public RegisteredUser login(String username, String password) throws RemoteException {
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

        registeredUser = (RegisteredUser) user;
        logger.log(Level.INFO, String.format("%s logged in", user.getNickname()));
        return registeredUser;
        //return String.format("You logged in as: %s", user.getNickname());
    }

    public String logout() throws RemoteException {
        if (getUser() == null) {
            return MSG_NOT_LOGGED_IN;
        }
        String partyKey = "";

        if (currentParty != null) {
            partyKey = currentParty.getPartyKey();
            publisher.unsubscribeRemoteListener(this, partyKey);
            currentParty = null;
        }

        User u = getUser();
        server.logout(u, partyKey);
        clearUsers();

        logger.log(Level.INFO, String.format("%s logged out", u.getNickname()));
        return "You logged out.";
    }

    public String getTemporaryUser(String nickname) throws RemoteException {
        if (getUser() != null) {
            return MSG_ALREADY_LOGGED_IN;
        }

        User user = server.getTemporaryUser(nickname);
        temporaryUser = (TemporaryUser) user;

        logger.log(Level.INFO, String.format("Temporary User %s created", user.getNickname()));
        return String.format("You logged in as: %s", user.getNickname());
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
        if (currentParty == null) {
            return MSG_NOT_IN_PARTY;
        }

        Votable votable = currentParty.getNextSong();

        if (server.mediaIsPlayed(votable, currentParty.getPartyKey(), user)) {
            player.play(votable);
            logger.log(Level.INFO, String.format("%s started playing %s", user.getNickname(), votable.getName()));
            return String.format("You started playing %s", votable.getName());
        }

        return "This action is not allowed.";
    }

    public List<Votable> getVotablesToVoteOn(){
        User user = getUser();
        if (user == null) {
            return null;
        }
        if (currentParty == null) {
            return null;
        }

        return currentParty.generateVoteList(user);

    }

    public void removeVotable(Votable votable) throws RemoteException {
        if (currentParty == null) {
            //return MSG_NOT_IN_PARTY;
            return;
        }

        if (getUser() == null) {
            //return MSG_NOT_LOGGED_IN;
            return;
        }

        server.removeVotable(getUser(),currentParty.getPartyKey(),votable);
    }

    public void voteOnVotable(Votable votable, Vote vote) throws RemoteException {
        if (currentParty == null) {
            //return MSG_NOT_IN_PARTY;
            return;
        }

        if (getUser() == null) {
            //return MSG_NOT_LOGGED_IN;
            return;
        }

        server.vote(votable,vote,getUser(),currentParty.getPartyKey());
    }

    public void createUser(String email, String password, String nickname) throws RemoteException {
        server.createUser(email,password,nickname);
    }

    public String getPartyInfo() {
        if (currentParty == null) {
            return MSG_NOT_IN_PARTY;
        }

        return currentParty.toString();
    }

    public Party getParty() {
        if (currentParty == null) {
            return null;
        }
        return currentParty;
    }

    public User getUser() {
        if (registeredUser != null) {
            return registeredUser;
        } else if (temporaryUser != null) {
            return temporaryUser;
        } else return null;
    }

    private void clearUsers() {
        registeredUser = null;
        temporaryUser = null;
    }
}
