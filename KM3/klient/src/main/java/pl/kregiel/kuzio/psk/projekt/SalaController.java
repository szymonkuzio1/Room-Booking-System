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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SalaController {
    @FXML
    private VBox historia_box;
    @FXML
    private Button historia_button;
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
    @FXML
    private TextField kod_totp;

    private int id_prowadzacy;
    private String rola;
    private int sala;
    private int pietro;
    private HttpClient httpClient = HttpClient.newHttpClient();

    public void setDane(int id_prowadzacy, String rola, int pietro, int sala) {
        this.id_prowadzacy = id_prowadzacy;
        this.rola = rola;
        this.pietro = pietro;
        this.sala = sala;
        sala_label.setText("Sala: " + sala);

        if (rola.equals("Administrator")) {
            historia_button.setVisible(true);
        }
    }
    @FXML
    public void handle_historia_rezerwacji(ActionEvent event) throws Exception {

        String url = "http://localhost:8080/api/pietra/" + pietro + "/sale/" + sala + "/rezerwacje";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        historia_box.getChildren().clear();
        historia_box.setVisible(true);
        historia_box.setManaged(true);

        if (json.equals("[]")) {
            komunikat.setText("Brak rezerwacji!");
            return;
        }
        komunikat.setText("Historia rezerwacji:");

        json = json.substring(1, json.length()-1);
        String[] wpisy = json.split("\\},\\{");
        for (String wpis : wpisy) {
            wpis = wpis.replace("{", "").replace("}", "").replace("\"", "");
            String[] pola = wpis.split(",");

            String id = "";
            String data_rezerwacji = "";
            String poczatek_rezerwacji = "";
            String koniec_rezerwacji = "";

            for (String pole : pola) {
                pole = pole.trim();
                if (pole.startsWith("id:")) {
                    id = pole.replace("id:", "").trim();
                }
                if (pole.startsWith("data_rezerwacji:")) {
                    data_rezerwacji = pole.replace("data_rezerwacji:", "").trim();
                }
                if (pole.startsWith("poczatek_rezerwacji:")) {
                    poczatek_rezerwacji = pole.replace("poczatek_rezerwacji:", "").trim();
                }
                if (pole.startsWith("koniec_rezerwacji:")) {
                    koniec_rezerwacji = pole.replace("koniec_rezerwacji:", "").trim();
                }
            }

            int id_rezerwacji = Integer.parseInt(id);

            HBox wiersz = new HBox();
            wiersz.setSpacing(10);
            wiersz.setAlignment(Pos.CENTER);

            Label opis = new Label(data_rezerwacji + " | " + poczatek_rezerwacji + " - " + koniec_rezerwacji);
            opis.setStyle("-fx-font-size: 16px;");

            Button usun_button = new Button("Usuń");
            usun_button.setStyle("-fx-font-size: 14px;");
            usun_button.setOnAction(e -> {
                try {
                    usun_rezerwacje(id_rezerwacji);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            wiersz.getChildren().addAll(opis, usun_button);
            historia_box.getChildren().add(wiersz);
        }
    }

    @FXML
    public void handle_rezerwacja(ActionEvent event) throws Exception {
        String poczatek = poczatek_rezerwacji.getText();
        String koniec = koniec_rezerwacji.getText();
        String kod = kod_totp.getText();

        // walidacja danych - brak danych w formularzu
        if (data_rezerwacji.getValue() == null || poczatek.isEmpty() || koniec.isEmpty() || kod.isEmpty()) {
            komunikat.setText("Uzupełnij wszystkie pola!");
            return;
        }
        String data = data_rezerwacji.getValue().toString();

        String json = "{"
                + "\"id_prowadzacy\":" + id_prowadzacy + ","
                + "\"kod_totp\":\"" + kod + "\","
                + "\"data_rezerwacji\":\"" + data + "\","
                + "\"poczatek_rezerwacji\":\"" + poczatek + "\","
                + "\"koniec_rezerwacji\":\"" + koniec + "\""
                + "}";

        System.out.println(json);

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
        controller.setDane(id_prowadzacy, rola, pietro);

        Stage window = (Stage) sala_label.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
    }

    private void usun_rezerwacje(int id_rezerwacji) throws Exception {
        String kod = kod_totp.getText();
        if (kod.isEmpty()) {
            komunikat.setText("Podaj kod TOTP!");
            return;
        }

        String json = "{"
                + "\"id_prowadzacy\":\"" + id_prowadzacy + "\","
                + "\"kod_totp\":\"" + kod + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/pietra/" + pietro + "/sale/" + sala + "/rezerwacje/" + id_rezerwacji))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.body().contains("\"status\":true")) {
            komunikat.setText("Usunięto rezerwację!");
            handle_historia_rezerwacji(null);
        }
        else {
            komunikat.setText("Nie usunięto rezerwacji!");
        }
    }
}
