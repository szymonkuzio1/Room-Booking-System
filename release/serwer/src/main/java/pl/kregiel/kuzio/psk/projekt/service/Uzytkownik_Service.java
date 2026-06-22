package pl.kregiel.kuzio.psk.projekt.service;

import org.springframework.stereotype.Service;
import pl.kregiel.kuzio.psk.projekt.config.Database_Connector;
import pl.kregiel.kuzio.psk.projekt.model.Uzytkownik;
import pl.kregiel.kuzio.psk.projekt.util.Haslo_Utility;
import pl.kregiel.kuzio.psk.projekt.util.TotpUtil;

import java.sql.Connection;
import java.sql.ResultSet;

@Service
public class Uzytkownik_Service {

    public String zarejestruj(String nazwa, String email, String haslo) throws Exception {
        Connection connection = Database_Connector.getConnection();

        var sprawdz = connection.prepareStatement(
                "SELECT COUNT(*) AS liczba FROM Uzytkownicy WHERE nazwa_uzytkownika = ?"
        );
        sprawdz.setString(1, nazwa);

        ResultSet wynik = sprawdz.executeQuery();
        wynik.next();

        if (wynik.getInt("liczba") > 0) {
            connection.close();
            return null;
        }

        String[] dane_hasla = Haslo_Utility.hashuj_haslo_z_nowa_sola(haslo);
        String zahashowane_haslo = dane_hasla[0];
        String salt = dane_hasla[1];
        String totp_secret = TotpUtil.generateSecret();
        String uri = TotpUtil.generateTotpUri("System rezerwacji sal laboratoryjnych", nazwa, totp_secret);
        String qr_code = TotpUtil.generateQrCode(uri);

        var statement = connection.prepareStatement("""
            INSERT INTO Uzytkownicy
            (nazwa_uzytkownika, email, zahashowane_haslo, salt, rola, totp_secret)
            VALUES (?, ?, ?, ?, ?, ?)
        """);

        statement.setString(1, nazwa);
        statement.setString(2, email);
        statement.setString(3, zahashowane_haslo);
        statement.setString(4, salt);
        statement.setString(5, "Prowadzacy");
        statement.setString(6, totp_secret);

        statement.executeUpdate();
        connection.close();

        // debug
        System.out.println("TOTP secret dla " + nazwa + ": " + totp_secret);

        return qr_code;
    }

    public Uzytkownik zaloguj(String email, String haslo) throws Exception {
        Connection connection = Database_Connector.getConnection();

        var statement = connection.prepareStatement("""
            SELECT id, nazwa_uzytkownika, email, zahashowane_haslo, salt, rola
            FROM Uzytkownicy
            WHERE email = ?
        """);

        statement.setString(1, email);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            boolean poprawne = Haslo_Utility.weryfikuj_haslo(
                    haslo,
                    result.getString("zahashowane_haslo"),
                    result.getString("salt")
            );

            if (poprawne) {
                Uzytkownik uzytkownik = new Uzytkownik(
                        String.valueOf(result.getInt("id")),
                        result.getString("nazwa_uzytkownika"),
                        result.getString("email"),
                        result.getString("zahashowane_haslo"),
                        result.getString("salt"),
                        result.getString("rola")
                );

                connection.close();
                return uzytkownik;
            }
        }

        connection.close();
        return null;
    }
}
