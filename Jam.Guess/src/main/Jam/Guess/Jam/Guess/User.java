package main.Jam.Guess.Jam.Guess;

import main.Jam.Guess.Jam.Guess.IUser;

public class User implements IUser {

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username)
    {
        this.username = username;
    }
    public User(int accountID, String username)
    {
        this.id = accountID;
        this.username = username;
    }
    public User(String username, String password) {this.username = username; this.password = password;}


    public int getId() {
        return id;
    }

    public int id;


    public String getUsername() {
        return username;
    }

    public String username;


    public String getEmail() {
        return email;
    }

    public String email;
    private String password;


}
