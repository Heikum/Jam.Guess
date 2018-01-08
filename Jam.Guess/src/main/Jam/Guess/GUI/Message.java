package main.Jam.Guess.GUI;

import javafx.scene.control.Alert;

public class Message {

    public static void Show(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.show();
    }
}
