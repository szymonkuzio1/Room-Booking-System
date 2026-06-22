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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class RejestracjaController {
    @FXML
    private TextField login_field;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField haslo_field;

    @FXML
    private Label komunikat;

    @FXML
    private ImageView qr_obraz;

    @FXML
    private Label qr_komunikat;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    public void handle_rejestracja(ActionEvent event) throws Exception {
        String login = login_field.getText();
        String email = email_field.getText();
        String haslo = haslo_field.getText();

        String json = "{"
                + "\"nazwa_uzytkownika\":\"" + login + "\","
                + "\"email\":\"" + email + "\","
                + "\"haslo\":\"" + haslo + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/uzytkownicy/rejestracja"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        System.out.println(body);
        if (body.contains("\"status\":true")) {
            String qr_code = extract_qr_code(body);
            qr_code = qr_code.replace("data:image/png;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(qr_code);
            Image image = new Image(new ByteArrayInputStream(imageBytes));

            qr_obraz.setImage(image);
            qr_obraz.setVisible(true);
            qr_obraz.setManaged(true);

            komunikat.setText("Rejestracja zakończona sukcesem!");
            qr_komunikat.setText("Zeskanuj kod QR");
        } else {
            komunikat.setText("Nie udało się zarejestrować użytkownika!");
        }
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) login_field.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    private String extract_qr_code(String body) {
        String szukane = "\"qr_code\":\"";
        int start = body.indexOf(szukane);

        if (start == -1) {
            return "";
        }
        start += szukane.length();
        int end = body.indexOf("\"", start);
        return body.substring(start, end);
    }
}
