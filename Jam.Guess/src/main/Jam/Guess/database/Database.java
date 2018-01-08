package main.Jam.Guess.database;
import java.sql.*;

public class Database {

    static String url = "jdbc:mysql://localhost:3306/jam.guess";
    static String username = "root";
    static String password = "loler123";

    public static void main(String[] args) {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

}
