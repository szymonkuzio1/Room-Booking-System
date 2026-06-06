package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    @FXML
    private TextField email_field;
    @FXML
    private PasswordField haslo_field;
    @FXML
    private Label komunikat;

    private HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    public void handle_logowanie(ActionEvent event) throws Exception {
        String email = email_field.getText();
        String haslo = haslo_field.getText();

        // walidacja danych
        // 1. brak danych w formularzu
        if(email.isEmpty() || haslo.isEmpty()) {
            komunikat.setText("Prosze podac e-mail i haslo!");
            return;
        }
        String json = "{"
                + "\"email\":\"" + email + "\","
                + "\"haslo\":\"" + haslo + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/uzytkownicy/logowanie"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        // 2. brak e-maila
        if (body.contains("\"status\":false") && body.contains("nie znaleziono")) {
            komunikat.setText("Prosze podac e-mail i haslo!");
            return;
        }

        // 3. błędne hasło
        if (body.contains("\"status\":false") && body.contains("haslo")) {
            komunikat.setText("Wprowadzono bledne haslo!");
            return;
        }

        if (body.contains("\"status\":true")) {
            int id_prowadzacy = Integer.parseInt(wyciagnijPole(body, "id"));
            String rola = wyciagnijPole(body, "rola");
            String login = wyciagnijPole(body, "nazwa_uzytkownika");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("pietra-view.fxml"));
            Parent root = loader.load();

            PietraController controller = loader.getController();
            controller.setDane(id_prowadzacy, login, rola);

            Stage stage = (Stage) email_field.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 800));
            stage.show();
        }
        else {
            komunikat.setText("Nieprawidłowy e-mail lub hasło!");
        }
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) email_field.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    private String wyciagnijPole(String json, String pole) {
        String szukane = "\"" + pole + "\":\"";
        int start = json.indexOf(szukane);
        if (start == -1) {
            return "";
        }
        start = start + szukane.length();
        int koniec = json.indexOf("\"", start);

        return json.substring(start, koniec);
    }
}
