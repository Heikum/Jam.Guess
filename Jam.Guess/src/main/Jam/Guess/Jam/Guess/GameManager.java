package main.Jam.Guess.Jam.Guess;

public class GameManager implements IGameManager {


    public User login(String username, String password){ return new User(username); }

    public boolean registerUser(User newuser){return true;}

    public void addFriend(String username){}

    public void acceptFriend(String username){}

    public boolean createGame(User Host, User opponent){return true;}
    public boolean logout(User user) {return true ;}

}
