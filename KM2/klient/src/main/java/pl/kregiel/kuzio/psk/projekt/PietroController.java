package pl.kregiel.kuzio.psk.projekt;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PietroController {
    @FXML
    private Label label;
    @FXML
    private GridPane grid;

    private int pietro;
    private HttpClient httpClient = HttpClient.newHttpClient();

    public void setDane(int pietro) throws Exception {
        this.pietro = pietro;
        label.setText("Piętro: " + pietro);
        wczytaj_sale();
    }

    public void wczytaj_sale() throws Exception {
        String url = "http://localhost:8080/api/pietra/" + pietro + "/sale";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Integer> sale = parseSale(response.body());
        utworz_przyciski(sale);
    }

    private List<Integer> parseSale(String json) {
        List<Integer> sale = new ArrayList<>();
        json = json.replace("[", "").replace("]", "").trim();
        if (json.isEmpty()) {
            return sale;
        }
        String[] parts = json.split(",");
        for(String part : parts) {
            sale.add(Integer.parseInt(part.trim()));
        }
        return sale;
    }

    private void utworz_przyciski(List<Integer> sale) throws Exception {
        grid.getChildren().clear();
        for(int i=0; i<sale.size(); i++) {
            int sala = sale.get(i);
            Button button = new Button(String.valueOf(sala));
            button.setStyle("-fx-font-size: 20px;");
            button.setOnAction(event -> {
                try {
                    handle_sala(event, sala);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            int kolumna = i%5;
            int rzad = i/5;
            grid.add(button, kolumna, rzad);
        }
    }

    public void handle_sala(ActionEvent event, int sala) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sala-view.fxml"));
        Parent root = loader.load();

        SalaController controller = loader.getController();
        controller.setDane(sala, pietro);

        Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 600, 600));
    }

    @FXML
    public void handle_wstecz(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Stage window = (Stage) grid.getScene().getWindow();
        window.setScene(new Scene(root, 600, 600));
    }
}
