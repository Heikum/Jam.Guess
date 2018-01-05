package main.Jam.Guess.Jam.Guess;

import java.rmi.RemoteException;

public interface IGameManager {
    User login(String username, String password);

    boolean registerUser(User newuser);

    void addFriend(String username);

    void acceptFriend(String username);

    boolean createGame(User Host, User opponent);

    boolean logout(User user);

}
