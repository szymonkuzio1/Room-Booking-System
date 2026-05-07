package pl.kregiel.kuzio.psk.projekt.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import pl.kregiel.kuzio.psk.projekt.model.Uzytkownik;
import pl.kregiel.kuzio.psk.projekt.util.Haslo_Utility;

// serwis do zarządzania użytkownikami z bezpiecznym przechowywaniem haseł
@Service
public class Uzytkownik_Service {
    
    private ObjectMapper objectMapper;
    private static final String PLIK_UZYTKOWNIKOW = "uzytkownicy.json";

    public Uzytkownik_Service(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // rejestracja nowego użytkownika
    public boolean zarejestruj_uzytkownika(String nazwa_uzytkownika, String email, String haslo, String rola) throws Exception {
        Path sciezka_plik = Path.of(PLIK_UZYTKOWNIKOW);
        
        // utworzenie pliku jeśli nie istnieje
        if (!Files.exists(sciezka_plik)) {
            objectMapper.writeValue(sciezka_plik.toFile(), new ArrayList<Uzytkownik>());
        }

        // odczyt istniejących użytkowników
        List<Uzytkownik> uzytkownicy = objectMapper.readValue(
            sciezka_plik.toFile(),
            new TypeReference<List<Uzytkownik>>() {}
        );

        // sprawdzenie czy nazwa użytkownika już istnieje
        for (Uzytkownik uzytkownik : uzytkownicy) {
            if (uzytkownik.getNazwa_uzytkownika().equals(nazwa_uzytkownika)) {
                return false; // użytkownik już istnieje
            }
        }

        // hashowanie hasła z automatycznym generowaniem soli
        String[] dane_hasla = Haslo_Utility.hashuj_haslo_z_nowa_sola(haslo);
        String zahashowane_haslo = dane_hasla[0];
        String salt = dane_hasla[1];

        // utworzenie nowego użytkownika
        String id = UUID.randomUUID().toString();
        Uzytkownik nowy_uzytkownik = new Uzytkownik(
            id, 
            nazwa_uzytkownika, 
            email, 
            zahashowane_haslo, 
            salt, 
            rola
        );

        // dodanie do listy i zapis
        uzytkownicy.add(nowy_uzytkownik);
        objectMapper.writeValue(sciezka_plik.toFile(), uzytkownicy);

        return true;
    }

    // logowanie użytkownika - weryfikacja hasła
    public Uzytkownik zaloguj_uzytkownika(String nazwa_uzytkownika, String haslo) throws Exception {
        Path sciezka_plik = Path.of(PLIK_UZYTKOWNIKOW);

        // sprawdzenie czy plik istnieje
        if (!Files.exists(sciezka_plik)) {
            return null; // brak użytkowników
        }

        // odczyt użytkowników
        List<Uzytkownik> uzytkownicy = objectMapper.readValue(
            sciezka_plik.toFile(),
            new TypeReference<List<Uzytkownik>>() {}
        );

        // wyszukanie użytkownika i weryfikacja hasła
        for (Uzytkownik uzytkownik : uzytkownicy) {
            if (uzytkownik.getNazwa_uzytkownika().equals(nazwa_uzytkownika)) {
                // weryfikacja hasła z użyciem zapisanej soli
                boolean czy_poprawne = Haslo_Utility.weryfikuj_haslo(
                    haslo,
                    uzytkownik.getZahashowane_haslo(),
                    uzytkownik.getSalt()
                );

                if (czy_poprawne) {
                    return uzytkownik; // logowanie udane
                } else {
                    return null; // błędne hasło
                }
            }
        }

        return null; // użytkownik nie znaleziony
    }

    // zmiana hasła użytkownika
    public boolean zmien_haslo(String nazwa_uzytkownika, String stare_haslo, String nowe_haslo) throws Exception {
        Path sciezka_plik = Path.of(PLIK_UZYTKOWNIKOW);

        if (!Files.exists(sciezka_plik)) {
            return false;
        }

        List<Uzytkownik> uzytkownicy = objectMapper.readValue(
            sciezka_plik.toFile(),
            new TypeReference<List<Uzytkownik>>() {}
        );

        for (Uzytkownik uzytkownik : uzytkownicy) {
            if (uzytkownik.getNazwa_uzytkownika().equals(nazwa_uzytkownika)) {
                // weryfikacja starego hasła
                boolean czy_poprawne = Haslo_Utility.weryfikuj_haslo(
                    stare_haslo,
                    uzytkownik.getZahashowane_haslo(),
                    uzytkownik.getSalt()
                );

                if (!czy_poprawne) {
                    return false; // stare hasło niepoprawne
                }

                // generowanie nowego hashu z nową solą
                String[] dane_hasla = Haslo_Utility.hashuj_haslo_z_nowa_sola(nowe_haslo);
                uzytkownik.setZahashowane_haslo(dane_hasla[0]);
                uzytkownik.setSalt(dane_hasla[1]);

                // zapis zmian
                objectMapper.writeValue(sciezka_plik.toFile(), uzytkownicy);
                return true;
            }
        }

        return false; // użytkownik nie znaleziony
    }

    // pobranie wszystkich użytkowników (bez haseł)
    public List<Uzytkownik> pobierz_wszystkich_uzytkownikow() throws Exception {
        Path sciezka_plik = Path.of(PLIK_UZYTKOWNIKOW);

        if (!Files.exists(sciezka_plik)) {
            return new ArrayList<>();
        }

        List<Uzytkownik> uzytkownicy = objectMapper.readValue(
            sciezka_plik.toFile(),
            new TypeReference<List<Uzytkownik>>() {}
        );

        // usunięcie wrażliwych danych przed zwróceniem
        for (Uzytkownik uzytkownik : uzytkownicy) {
            uzytkownik.setZahashowane_haslo(null);
            uzytkownik.setSalt(null);
        }

        return uzytkownicy;
    }
}
