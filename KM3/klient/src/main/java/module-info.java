module pl.kregiel.kuzio.psk.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens pl.kregiel.kuzio.psk.projekt to javafx.fxml;
    exports pl.kregiel.kuzio.psk.projekt;
}