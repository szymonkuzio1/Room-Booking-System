package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
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
    private Label komunikat;

    private int sala;
    private int pietro;
    private HttpClient httpClient = HttpClient.newHttpClient();

    public void setDane(int sala, int pietro) {
        this.pietro = pietro;
        this.sala = sala;
        sala_label.setText("Sala: " + sala);
    }

    @FXML
    public void handle_historia_rezerwacji(ActionEvent event) throws Exception {

        String url = "http://localhost:8080/api/pietra/" + pietro + "/sale/" + sala + "/rezerwacje";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String historia_rezerwacji = format(response.body());
        komunikat.setText(historia_rezerwacji);
    }

    @FXML
    public void handle_rezerwacja(ActionEvent event) throws Exception {
        String poczatek = poczatek_rezerwacji.getText();
        String koniec = koniec_rezerwacji.getText();

        // walidacja danych - brak danych w formularzu
        if (data_rezerwacji.getValue() == null || poczatek.isEmpty() || koniec.isEmpty()) {
            komunikat.setText("Uzupełnij wszystkie pola!");
            return;
        }
        String data = data_rezerwacji.getValue().toString();

        String json = "{"
                + "\"data_rezerwacji\":\"" + data + "\","
                + "\"poczatek_rezerwacji\":\"" + poczatek + "\","
                + "\"koniec_rezerwacji\":\"" + koniec + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/pietra/" + pietro + "/sale/" + sala + "/rezerwacja"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        String body = response.body();
        if(body.contains("\"status\":true")) {
            komunikat.setText("Utworzono rezerwację!");
        }
        else {
            komunikat.setText("Nie utworzono rezerwacji!");
        }
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pietro-view.fxml"));
        Parent root = loader.load();

        PietroController controller = loader.getController();
        controller.setDane(pietro);

        Stage window = (Stage) sala_label.getScene().getWindow();
        window.setScene(new Scene(root, 600, 600));
    }

    private String format(String json) {
        if (json.equals("[]")) {
            return "Brak rezerwacji!";
        }

        json = json.replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "");
        String[] rezerwacje = json.split(",");

        StringBuilder wynik = new StringBuilder("Historia rezerwacji:\n");
        for (int i=0; i<rezerwacje.length; i+=3) {
            String data = rezerwacje[i].replace("data_rezerwacji:", "").trim();
            String poczatek = rezerwacje[i + 1].replace("poczatek_rezerwacji:", "").trim();
            String koniec = rezerwacje[i + 2].replace("koniec_rezerwacji:", "").trim();
            wynik.append(data)
                    .append(" | ")
                    .append(poczatek)
                    .append(" - ")
                    .append(koniec)
                    .append("\n");
        }
        return wynik.toString();
    }
}
