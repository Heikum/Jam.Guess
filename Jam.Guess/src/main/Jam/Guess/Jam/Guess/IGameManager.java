package main.Jam.Guess.Jam.Guess;

public interface IGameManager {
    boolean login(String username, String password);

    boolean registerUser(User newuser);

    void addFriend(String username);

    void acceptFriend(String username);

    boolean createGame(User Host, User opponent);
}
