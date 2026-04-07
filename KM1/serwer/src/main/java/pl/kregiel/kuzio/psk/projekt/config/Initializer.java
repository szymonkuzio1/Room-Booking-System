package pl.kregiel.kuzio.psk.projekt.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class Initializer {
    private ObjectMapper objectMapper;
    public Initializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws Exception {
        Path pietra_sciezka = Path.of("pietra");
        Path sale_sciezka = pietra_sciezka.resolve("sale");

        if (!Files.exists(pietra_sciezka)) {
            Files.createDirectories(pietra_sciezka);
        }
        if (!Files.exists(sale_sciezka)) {
            Files.createDirectories(sale_sciezka);
        }
        utworz_pietro(1, 101, 120);
        utworz_pietro(2, 201, 220);
        utworz_pietro(3, 301, 320);
        utworz_pietro(4, 401, 420);
        utworz_pietro(5, 501, 520);
    }
    private void utworz_pietro(int pietro, int sala_poczatek, int sala_koniec) throws Exception {
        Path plik_pietro_sciezka = Path.of("pietra", "pietro_" + pietro + ".json");
        if(!Files.exists(plik_pietro_sciezka)) {
            List<Integer> sale = new ArrayList<>();

            for(int sala = sala_poczatek; sala <= sala_koniec; sala++) {
                sale.add(sala);
            }

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(plik_pietro_sciezka.toFile(), sale);
        }
    }
}
