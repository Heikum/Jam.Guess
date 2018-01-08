package main.Jam.Guess.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Jam.Guess.GUI.Message;
import main.Jam.Guess.database.Database;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {
    private Database database;
    private Stage stage;
    @FXML
    private PasswordField tbPassword;
    @FXML
    private TextField tbUsername;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void register(ActionEvent actionEvent) throws IOException {
        String username = tbUsername.getText();
        String password = tbPassword.getText();
        database = new Database();
        database.registerUser(username, password);
    }
}
