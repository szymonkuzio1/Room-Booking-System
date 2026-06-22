package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HelloController {

    @FXML
    private Label komunikat;
    @FXML
    protected void handle_logowanie(ActionEvent event) throws Exception {
        Button button = (Button) event.getSource();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();

        Stage window = (Stage) button.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
    }

    @FXML
    protected void handle_rejestracja(ActionEvent event) throws Exception {
        Button button = (Button) event.getSource();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rejestracja-view.fxml"));
        Parent root = loader.load();

        Stage window = (Stage) button.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
    }

    public void setKomunikat(String tekst) {
        komunikat.setText(tekst);
    }
}
