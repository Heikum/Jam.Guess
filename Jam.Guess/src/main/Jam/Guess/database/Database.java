package main.Jam.Guess.database;
import main.Jam.Guess.IDatabase;
import main.Jam.Guess.Jam.Guess.User;

import java.sql.*;

public class Database implements IDatabase{

    private static DatabaseConnection databaseconnection;
    private Connection connection;
    Statement stmt = null;

    public void registerUser(String Username, String Password)
    {
        Connection connection = databaseconnection.connect();
        try {
            stmt = connection.createStatement();
            stmt.executeQuery("INSERT INTO users " + "VALUES ('Hicham', 'Test')");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


    }

    public User loginUser(String Username, String Password)
    {
        return new User("hich", "lol");
    }

}
