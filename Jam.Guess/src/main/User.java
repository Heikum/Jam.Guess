package main;

public class User {

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

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
