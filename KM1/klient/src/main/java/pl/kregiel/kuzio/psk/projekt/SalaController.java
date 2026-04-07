package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SalaController {
    @FXML
    private Label sala_label;
    @FXML
    private DatePicker data_rezerwacji;
    @FXML
    private TextField poczatek_rezerwacji;
    @FXML
    private TextField koniec_rezerwacji;
    @FXML
    private Label komunikat_label;

    private int sala;
    private int pietro;
    private HttpClient httpClient = HttpClient.newHttpClient();

    public void setSala(int sala, int pietro) {
        this.pietro = pietro;
        this.sala = sala;
        sala_label.setText("Sala: " + sala);
    }

    @FXML
    public void handle_rezerwacja(ActionEvent event) throws Exception {
        String data = data_rezerwacji.getValue().toString();
        String poczatek = poczatek_rezerwacji.getText();
        String koniec = koniec_rezerwacji.getText();

        String json = "{"
                + "\"sala\":" + sala + ","
                + "\"data_rezerwacji\":\"" + data + "\","
                + "\"poczatek_rezerwacji\":\"" + poczatek + "\","
                + "\"koniec_rezerwacji\":\"" + koniec + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/api/rezerwacje"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        String body = response.body();
        if(body.contains("\"status\":true")) {
            komunikat_label.setText("Utworzono rezerwacje!");
        }
        else {
            komunikat_label.setText("Nie utworzono rezerwacji!");
        }
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Stage window = (Stage) sala_label.getScene().getWindow();
        window.setScene(new Scene(root, 600, 600));
    }
}
