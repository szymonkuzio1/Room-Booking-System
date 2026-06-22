package pl.kregiel.kuzio.psk.projekt.service;

import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

import pl.kregiel.kuzio.psk.projekt.config.Database_Connector;
import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.model.Rezerwacja;
import pl.kregiel.kuzio.psk.projekt.util.TotpUtil;
import pl.kregiel.kuzio.psk.projekt.exception.*;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Timestamp;

// klasa reprezentująca główny proces logiczny
@Service
public class Rezerwacja_Service {
    // utworzenie nowej rezerwacji
    public boolean utworz_rezerwacje(int prowadzacy, int sala, Rezerwacja_Request request) throws Exception {
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
            throw new NiepoprawneZapytanieException("Nie mozna utworzyc rezerwacji w przeszlosci!");
        }
        // 2. koniec rezerwacji nie jest późniejszy od początku
        if (!czas_koniec_rezerwacji.isAfter(czas_poczatek_rezerwacji)) {
            throw new NiepoprawneZapytanieException("Godzina zakonczenia musi byc pozniejsza od godziny rozpoczęcia!");
        }

        Connection connection = Database_Connector.getConnection();

        // 3. weryfikacja uprawnień
        var sprawdz_uzytkownika = connection.prepareStatement("""
            SELECT rola, totp_secret
            FROM Uzytkownicy
            WHERE id = ?
        """);
        sprawdz_uzytkownika.setInt(1, prowadzacy);

        var wynik_user = sprawdz_uzytkownika.executeQuery();
        if (!wynik_user.next()) {
            connection.close();
            throw new BrakUprawnienException("Uzytkownik nie istnieje!"); // brak użytkownika
        }

        String rola = wynik_user.getString("rola");
        if (!rola.equals("Prowadzacy") && !rola.equals("Administrator")) {
            connection.close();
            throw new BrakUprawnienException("Uzytkownik nie posiada uprawnien!");
        }

        // 4. weryfikacja kodu TOTP
        String secret = wynik_user.getString("totp_secret");
        if (secret == null || secret.isEmpty()) {
            connection.close();
            throw new NiepoprawnyTotpException("Nie utworzono sekretu TOTP!");
        }

        if (request.getKod_totp() == null || request.getKod_totp().isEmpty()) {
            connection.close();
            throw new NiepoprawnyTotpException("Nie podano kodu TOTP!");
        }

        if (!TotpUtil.verifyCode(secret, request.getKod_totp())) {
            connection.close();
            throw new NiepoprawnyTotpException("Nieprawidlowy lub nieaktywny kod TOTP!");
        }

        // 5. sprawdzenie konfliktów godzin
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
            throw new SalaZajetaException(
                    sala,
                    data_rezerwacji.toString(),
                    poczatek_rezerwacji.toString(),
                    koniec_rezerwacji.toString()
            );
        }

        // 6. dodanie nowej rezerwacji po spełnieniu wszystkich warunków
        try {
            var dodaj_rezerwacje = connection.prepareStatement("""
        INSERT INTO Rezerwacje
        (prowadzacy_id, sala_id, godzina_rozpoczecia, godzina_zakonczenia)
        VALUES (?, ?, ?, ?)
    """);

            dodaj_rezerwacje.setInt(1, prowadzacy);
            dodaj_rezerwacje.setInt(2, sala);
            dodaj_rezerwacje.setTimestamp(3, Timestamp.valueOf(czas_poczatek_rezerwacji));
            dodaj_rezerwacje.setTimestamp(4, Timestamp.valueOf(czas_koniec_rezerwacji));
            dodaj_rezerwacje.executeUpdate();
        }
        catch (Exception e) {
            connection.close();
            throw new BladZapisuException(e);
        }
        connection.close();
        return true;
    }

    // pobranie historii rezerwacji dla wybranej sali
    public List<Rezerwacja> pobierz_rezerwacje(int sala) throws Exception {

        Connection conn = Database_Connector.getConnection();
        var statement = conn.prepareStatement("""
            SELECT id, godzina_rozpoczecia, godzina_zakonczenia
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
                    result.getInt("id"),
                    data_rezerwacji,
                    poczatek_rezerwacji,
                    koniec_rezerwacji
            ));
        }
        conn.close();

        return lista;
    }

    // usuwanie rezerwacji
    public boolean usun_rezerwacje(int prowadzacy, int id_rezerwacji, String kod_totp) throws Exception {
        Connection connection = Database_Connector.getConnection();

        var sprawdz_role = connection.prepareStatement("""
            SELECT rola, totp_secret
            FROM Uzytkownicy
            WHERE id = ?
        """);
        sprawdz_role.setInt(1, prowadzacy);

        var wynik = sprawdz_role.executeQuery();
        if (!wynik.next()) {
            connection.close();
            throw new BrakUprawnienException("Uzytkownik nie istnieje!");
        }

        String rola = wynik.getString("rola");
        String secret = wynik.getString("totp_secret");

        if (!"Administrator".equals(rola)) {
            connection.close();
            throw new BrakUprawnienException("Brak uprawnien do usuwania rezerwacji!");
        }
        if(!TotpUtil.verifyCode(secret, kod_totp)) {
            connection.close();
            throw new NiepoprawnyTotpException("Nieprawidlowy lub nieaktywny kod TOTP!");
        }

        try {
            var usun = connection.prepareStatement("""
                DELETE FROM Rezerwacje
                WHERE id = ?
            """);
            usun.setInt(1, id_rezerwacji);

            int usunieto = usun.executeUpdate();
            if (usunieto == 0) {
                connection.close();
                throw new NiepoprawneZapytanieException("Rezerwacja nie istnieje!");
            }
        }
        catch (NiepoprawneZapytanieException e) {
            throw e;
        }
        catch (Exception e) {
            connection.close();
            throw new BladZapisuException(e);
        }
        connection.close();
        return true;
    }
}