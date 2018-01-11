package main.Jam.Guess.database;
import main.Jam.Guess.IDatabase;
import main.Jam.Guess.Jam.Guess.User;

import java.sql.*;

public class Database implements IDatabase{

    private DatabaseConnection databaseconnection;
    private int accountid;
    private String Username;
    private Connection connection;
    Statement stmt = null;

    public Database()
    {
        databaseconnection = new DatabaseConnection();
        connection = databaseconnection.connect();
    }

    public void registerUser(String inputUsername, String inputPassword)
    {
        try {
            //stmt = connection.createStatement();
            //stmt.executeUpdate("INSERT INTO users (Username, Password)" + "VALUES (inputUsername, Password)");
            //System.out.println("Registered");

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `users`(Username, Password) VALUES (?, ?)");
            pstmt.setString(1, inputUsername);
            pstmt.setString(2, inputPassword);
            pstmt.executeUpdate();
            System.out.println("Registered");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


    }

    public User loginUser(String inputUsername, String inputPassword)
    {
        try {
            PreparedStatement pstmtlogin = connection.prepareStatement("select id, Username, Password FROM Users where Username = (?) AND Password = (?)");
            pstmtlogin.setString(1, inputUsername);
            pstmtlogin.setString(2, inputPassword);
            ResultSet rs = pstmtlogin.executeQuery();
            if (rs != null)
            {
                while (rs.next()) {
                    if(rs.getInt(1) == 0)
                    {
                        return null;
                    }
                    else
                    {
                        accountid = rs.getInt(1);
                        Username = rs.getString(2);
                    }
                }
                User loggedinuser = new User(accountid, Username);
                System.out.println("Logged in succesfully!");
                return loggedinuser;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
