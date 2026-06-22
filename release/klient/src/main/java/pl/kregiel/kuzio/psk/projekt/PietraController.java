package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class PietraController {
    @FXML
    private Label komunikat;
    @FXML
    private Button pietro_1, pietro_2, pietro_3, pietro_4, pietro_5;
    private String rola;
    private int id_prowadzacy;

    public void setDane(int id_prowadzacy, String login, String rola) {
        this.id_prowadzacy = id_prowadzacy;
        this.rola = rola;

        komunikat.setText("Witaj, " + login + "!");
    }

    public void setDane(int id_prowadzacy, String rola) {
        this.id_prowadzacy = id_prowadzacy;
        this.rola = rola;
    }

    public void handle_pietro(ActionEvent event) throws Exception {
        Button button = (Button) event.getSource();
        int pietro = 0;

        switch (button.getId()) {
            case "pietro_1":
                pietro = 1;
                break;
            case "pietro_2":
                pietro = 2;
                break;
            case "pietro_3":
                pietro = 3;
                break;
            case "pietro_4":
                pietro = 4;
                break;
            case "pietro_5":
                pietro = 5;
                break;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("pietro-view.fxml"));
        Parent root = loader.load();

        PietroController controller = loader.getController();
        controller.setDane(id_prowadzacy, rola, pietro);

        Stage window = (Stage) button.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();

        Stage window = (Stage) pietro_1.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.show();
    }
}
