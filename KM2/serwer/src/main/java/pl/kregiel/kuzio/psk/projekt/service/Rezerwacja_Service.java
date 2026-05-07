package pl.kregiel.kuzio.psk.projekt.service;

import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.model.Rezerwacja;

import org.springframework.stereotype.Service;

import pl.kregiel.kuzio.psk.projekt.config.Database_Connector;
import java.sql.Connection;
import java.sql.Timestamp;

// klasa reprezentująca główny proces logiczny
@Service
public class Rezerwacja_Service {
    // utworzenie nowej rezerwacji
    public boolean utworz_rezerwacje(int sala, Rezerwacja_Request request) throws Exception {
        // formatowanie godziny
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDate data_rezerwacji = LocalDate.parse(request.getData_rezerwacji());
        LocalTime poczatek_rezerwacji = LocalTime.parse(request.getPoczatek_rezerwacji(), formatter);
        LocalTime koniec_rezerwacji = LocalTime.parse(request.getKoniec_rezerwacji(), formatter);

        // połączenie daty i godziny w jeden obiekt
        LocalDateTime czas_poczatek_rezerwacji = LocalDateTime.of(data_rezerwacji, poczatek_rezerwacji);
        LocalDateTime czas_koniec_rezerwacji = LocalDateTime.of(data_rezerwacji, koniec_rezerwacji);

        // aktualna data i godzina
        LocalDateTime czas_obecny = LocalDateTime.now();

        // walidacja danych
        // 1. rezerwacja pochodzi z przeszłości
        if(czas_poczatek_rezerwacji.isBefore(czas_obecny)) {
            return false;
        }
        // 2. koniec rezerwacji nie jest późniejszy od początku
        if (!czas_koniec_rezerwacji.isAfter(czas_poczatek_rezerwacji)) {
            return false;
        }

        Connection connection = Database_Connector.getConnection();

        // 3. sprawdzenie konfliktów godzin
        var sprawdz_konflikt = connection.prepareStatement("""
            SELECT COUNT(*) AS liczba
            FROM Rezerwacje
            WHERE sala_id = ?
              AND godzina_rozpoczecia < ?
              AND godzina_zakonczenia > ?
        """);
        sprawdz_konflikt.setInt(1, sala);
        sprawdz_konflikt.setTimestamp(2, Timestamp.valueOf(czas_koniec_rezerwacji));
        sprawdz_konflikt.setTimestamp(3, Timestamp.valueOf(czas_poczatek_rezerwacji));
        var wynik = sprawdz_konflikt.executeQuery();
        wynik.next();

        int liczba_konfliktow = wynik.getInt("liczba");
        if (liczba_konfliktow > 0) {
            connection.close();
            return false;
        }

        // 4. dodanie nowej rezerwacji po spełnieniu wszystkich warunków
        var dodaj_rezerwacje = connection.prepareStatement("""
            INSERT INTO Rezerwacje
            (prowadzacy_id, sala_id, godzina_rozpoczecia, godzina_zakonczenia)
            VALUES (?, ?, ?, ?)
        """);
        dodaj_rezerwacje.setInt(1, 1); // na razie prowadzący na sztywno
        dodaj_rezerwacje.setInt(2, sala);
        dodaj_rezerwacje.setTimestamp(3, Timestamp.valueOf(czas_poczatek_rezerwacji));
        dodaj_rezerwacje.setTimestamp(4, Timestamp.valueOf(czas_koniec_rezerwacji));
        dodaj_rezerwacje.executeUpdate();

        connection.close();
        return true;
    }

    // pobranie historii rezerwacji dla wybranej sali
    public List<Rezerwacja> pobierz_rezerwacje(int sala) throws Exception {

        Connection connection = Database_Connector.getConnection();
        var statement = connection.prepareStatement("""
            SELECT godzina_rozpoczecia, godzina_zakonczenia
            FROM Rezerwacje
            WHERE sala_id = ?
            ORDER BY godzina_rozpoczecia
        """);

        statement.setInt(1, sala);
        var result = statement.executeQuery();

        List<Rezerwacja> lista = new ArrayList<>();

        while (result.next()) {
            var poczatek = result.getTimestamp("godzina_rozpoczecia").toLocalDateTime();
            var koniec = result.getTimestamp("godzina_zakonczenia").toLocalDateTime();

            String data_rezerwacji = poczatek.toLocalDate().toString();
            String poczatek_rezerwacji = poczatek.toLocalTime().toString();
            String koniec_rezerwacji = koniec.toLocalTime().toString();

            lista.add(new Rezerwacja(
                    data_rezerwacji,
                    poczatek_rezerwacji,
                    koniec_rezerwacji
            ));
        }
        connection.close();

        return lista;
    }
}