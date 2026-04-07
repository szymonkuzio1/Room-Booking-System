package pl.kregiel.kuzio.psk.projekt.service;

import java.time.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.model.Rezerwacja;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class Rezerwacja_Service {
    private ObjectMapper objectMapper;

    public Rezerwacja_Service(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean utworz_rezerwacje(Rezerwacja_Request request) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDate data_rezerwacji = LocalDate.parse(request.getData_rezerwacji());
        LocalTime poczatek_rezerwacji = LocalTime.parse(request.getPoczatek_rezerwacji(), formatter);
        LocalTime koniec_rezerwacji = LocalTime.parse(request.getKoniec_rezerwacji(), formatter);

        LocalDateTime czas_poczatek_rezerwacji = LocalDateTime.of(data_rezerwacji, poczatek_rezerwacji);
        LocalDateTime czas_obecny = LocalDateTime.now();

        if(czas_poczatek_rezerwacji.isBefore(czas_obecny)) {
            return false;
        }
        if(!koniec_rezerwacji.isAfter(poczatek_rezerwacji)) {
            return false;
        }

        Path sala_path = Path.of("pietra", "sale", request.getSala() + ".json");
        if(!Files.exists(sala_path)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    sala_path.toFile(),
                    new ArrayList<Rezerwacja>()
            );
        }

        List<Rezerwacja> istniejace_rezerwacje = objectMapper.readValue(
                sala_path.toFile(),
                new TypeReference<List<Rezerwacja>>() {}
        );
        for(Rezerwacja istniejaca_rezerwacja : istniejace_rezerwacje) {
            if(istniejaca_rezerwacja.getData_rezerwacji().equals(request.getData_rezerwacji())) {
                LocalTime istniejacy_poczatek_rezerwacji = LocalTime.parse(istniejaca_rezerwacja.getPoczatek_rezerwacji(), formatter);
                LocalTime istniejacy_koniec_rezerwacji = LocalTime.parse(istniejaca_rezerwacja.getKoniec_rezerwacji(), formatter);

                boolean overlap = poczatek_rezerwacji.isBefore(istniejacy_koniec_rezerwacji) && koniec_rezerwacji.isAfter(istniejacy_poczatek_rezerwacji);
                if(overlap) {
                    return false;
                }
            }
        }

        Rezerwacja nowa_rezerwacja = new Rezerwacja(
                request.getData_rezerwacji(),
                request.getPoczatek_rezerwacji(),
                request.getKoniec_rezerwacji()
        );
        istniejace_rezerwacje.add(nowa_rezerwacja);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                sala_path.toFile(),
                istniejace_rezerwacje
        );

        return true;
    }
}
