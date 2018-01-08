package main.Jam.Guess.Jam.Guess;

import main.Jam.Guess.Jam.Guess.fontyspublisher.RemotePublisher;
import main.Jam.Guess.Jam.Guess.log.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GameManager implements IGameManager {


    private RemotePublisher publisher;
    private Logger logger;

    public GameManager(RemotePublisher publisher) throws RemoteException {
        this.publisher = publisher;
        logger = new Logger("PartyManager", Level.ALL, Level.SEVERE);
    }


    public User login(String username, String password){ return new User(username); }

    public boolean registerUser(User newuser){return true;}

    public void addFriend(String username){}

    public void acceptFriend(String username){}

    public boolean createGame(User Host, User opponent){return true;}
    public boolean logout(User user) {return true ;}

}
