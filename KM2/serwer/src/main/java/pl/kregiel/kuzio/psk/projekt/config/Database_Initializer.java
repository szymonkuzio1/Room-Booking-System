package pl.kregiel.kuzio.psk.projekt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class Database_Initializer {

    @PostConstruct
    public void init() throws Exception {

        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();

        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS defensywne_projekt");
        statement.execute("USE defensywne_projekt");

        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Pietra (
                id INT PRIMARY KEY,
                nazwa VARCHAR(255)
            )
        """);

        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Sale (
                id INT PRIMARY KEY,
                nazwa VARCHAR(255),
                pietro_id INT,
                FOREIGN KEY (pietro_id) REFERENCES Pietra(id)
            )
        """);

        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Uzytkownicy (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nazwa_uzytkownika VARCHAR(255),
                email VARCHAR(255),
                zahashowane_haslo VARCHAR(255),
                salt VARCHAR(255),
                rola VARCHAR(50)
            )
        """);

        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Rezerwacje (
                id INT PRIMARY KEY AUTO_INCREMENT,
                prowadzacy_id INT,
                sala_id INT,
                godzina_rozpoczecia DATETIME,
                godzina_zakonczenia DATETIME,
                FOREIGN KEY (prowadzacy_id) REFERENCES Uzytkownicy(id),
                FOREIGN KEY (sala_id) REFERENCES Sale(id)
            )
        """);

        // Utworzenie testowego konta administratora
        statement.executeUpdate("""
            INSERT IGNORE INTO Uzytkownicy (id, nazwa_uzytkownika, email, zahashowane_haslo, salt, rola) VALUES
            (1, 'admin', 'admin@tu.kielce.pl', 'test', 'test', 'Administrator')
        """);
        statement.executeUpdate("""
            INSERT IGNORE INTO Pietra (id, nazwa) VALUES
            (1, 'Pietro nr 1'),
            (2, 'Pietro nr 2'),
            (3, 'Pietro nr 3'),
            (4, 'Pietro nr 4'),
            (5, 'Pietro nr 5')
        """);

        for (int pietro = 1; pietro <= 5; pietro++) {
            for (int nr = 1; nr <= 20; nr++) {
                int sala = pietro * 100 + nr;
                statement.executeUpdate(
                        "INSERT IGNORE INTO Sale (id, nazwa, pietro_id) VALUES (" +
                                sala + ", 'Sala nr " + sala + "', " + pietro + ")"
                );
            }
        }
        connection.close();
    }
}
