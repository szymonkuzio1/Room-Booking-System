package pl.kregiel.kuzio.psk.projekt.service;

import java.time.*;
import java.util.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.model.Rezerwacja;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

// klasa reprezentująca główny proces logiczny
@Service
public class Rezerwacja_Service {
    private ObjectMapper objectMapper;

    public Rezerwacja_Service(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // utworzenie nowej rezerwacji
    public boolean utworz_rezerwacje(int sala, Rezerwacja_Request request) throws Exception {
        // formatowanie godziny
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDate data_rezerwacji = LocalDate.parse(request.getData_rezerwacji());
        LocalTime poczatek_rezerwacji = LocalTime.parse(request.getPoczatek_rezerwacji(), formatter);
        LocalTime koniec_rezerwacji = LocalTime.parse(request.getKoniec_rezerwacji(), formatter);

        // połączenie daty i godziny w jeden obiekt
        LocalDateTime czas_poczatek_rezerwacji = LocalDateTime.of(data_rezerwacji, poczatek_rezerwacji);
        // aktualna data i godzina
        LocalDateTime czas_obecny = LocalDateTime.now();

        // walidacja danych
        // 1. rezerwacja pochodzi z przeszłości
        if(czas_poczatek_rezerwacji.isBefore(czas_obecny)) {
            return false;
        }
        // 2. koniec rezerwacji nie jest późniejszy od początku
        if(!koniec_rezerwacji.isAfter(poczatek_rezerwacji)) {
            return false;
        }

        // utworzenie pliku .json dla danej sali
        Path sala_path = Path.of("pietra", "sale", sala + ".json");
        // jeśli plik nie istnieje -> utworzenie nowej listy z rezerwacjami
        if(!Files.exists(sala_path)) {
            objectMapper.writeValue(
                    sala_path.toFile(),
                    new ArrayList<Rezerwacja>()
            );
        }

        List<Rezerwacja> istniejace_rezerwacje = objectMapper.readValue(
                sala_path.toFile(),
                new TypeReference<List<Rezerwacja>>() {}
        );
        // walidacja danych
        // 3. sprawdzenie konfliktów godzin
        for(Rezerwacja istniejaca_rezerwacja : istniejace_rezerwacje) {
            // sprawdzenie rezerwacji w danym dniu
            if(istniejaca_rezerwacja.getData_rezerwacji().equals(request.getData_rezerwacji())) {
                LocalTime istniejacy_poczatek_rezerwacji = LocalTime.parse(istniejaca_rezerwacja.getPoczatek_rezerwacji(), formatter);
                LocalTime istniejacy_koniec_rezerwacji = LocalTime.parse(istniejaca_rezerwacja.getKoniec_rezerwacji(), formatter);
                // wykrywanie konfliktu godzin
                boolean overlap = poczatek_rezerwacji.isBefore(istniejacy_koniec_rezerwacji) && koniec_rezerwacji.isAfter(istniejacy_poczatek_rezerwacji);
                if(overlap) {
                    return false;
                }
            }
        }

        // walidacja danych
        // 4. dodanie nowej rezerwacji
        // utworzenie obiektu rezerwacji
        Rezerwacja nowa_rezerwacja = new Rezerwacja(
                request.getData_rezerwacji(),
                request.getPoczatek_rezerwacji(),
                request.getKoniec_rezerwacji()
        );
        // dodanie obiektu do listy
        istniejace_rezerwacje.add(nowa_rezerwacja);
        objectMapper.writeValue(
                sala_path.toFile(),
                istniejace_rezerwacje
        );

        return true;
    }

    // wyświetlenie historii rezerwacji
    public List<Rezerwacja> pobierz_rezerwacje(int sala) throws Exception {
        Path sala_path = Path.of("pietra", "sale", sala + ".json");
        if(!Files.exists(sala_path)) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                sala_path.toFile(),
                new TypeReference<List<Rezerwacja>>() {}
        );
    }
}
