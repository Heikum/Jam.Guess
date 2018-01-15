package main.Jam.Guess.GUI.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Jam.Guess.GUI.Message;
import main.Jam.Guess.GUI.Views.RegisterView;
import main.Jam.Guess.Jam.Guess.User;
import main.Jam.Guess.database.Database;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Objects;

public class MenuController {
    private Stage stage;
    private Database database;

    public void setStage(Stage stage)  {
        this.stage = stage;
        database = new Database();
    }
}
