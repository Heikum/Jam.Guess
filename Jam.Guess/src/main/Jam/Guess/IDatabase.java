package main.Jam.Guess;

import main.Jam.Guess.Jam.Guess.User;

public interface IDatabase {
    public void registerUser(String Username, String Password);
    public User loginUser(String Username, String Password);

}
