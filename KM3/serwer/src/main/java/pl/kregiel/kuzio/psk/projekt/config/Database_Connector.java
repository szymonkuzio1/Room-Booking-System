package pl.kregiel.kuzio.psk.projekt.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database_Connector {

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/defensywne_projekt",
                "root",
                ""
        );
    }
}
