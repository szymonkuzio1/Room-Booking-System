package pl.kregiel.kuzio.psk.projekt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.kregiel.kuzio.psk.projekt.config.Database_Connector;
import java.util.ArrayList;
import java.sql.Connection;

// klasa umożliwiająca odczyt listy sal na danym piętrze
@Service
public class Pietro_Service {

    // wypisanie listy sal na danym piętrze
    public List<Integer> sale(int pietro) throws Exception {

        Connection connection = Database_Connector.getConnection();
        var statement = connection.prepareStatement(
                "SELECT id FROM Sale WHERE pietro_id = ?"
        );
        statement.setInt(1, pietro);

        var result = statement.executeQuery();
        List<Integer> sale = new ArrayList<>();

        while (result.next()) {
            sale.add(result.getInt("id"));
        }
        connection.close();
        return sale;
    }
}
