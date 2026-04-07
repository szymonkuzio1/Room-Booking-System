package pl.kregiel.kuzio.psk.projekt.controller;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Response;
import pl.kregiel.kuzio.psk.projekt.service.Rezerwacja_Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rezerwacje")
public class Rezerwacja_Controller {
    private Rezerwacja_Service rezerwacja_service;
    public Rezerwacja_Controller(Rezerwacja_Service rezerwacja_service) {
        this.rezerwacja_service = rezerwacja_service;
    }

    @PostMapping
    public Rezerwacja_Response utworz_rezerwacje(@RequestBody Rezerwacja_Request request) throws Exception {
        boolean utworzono_rezerwacje = rezerwacja_service.utworz_rezerwacje(request);
        if(utworzono_rezerwacje) {
            return new Rezerwacja_Response(true, "Utworzono rezerwacje!");
        }
        else {
            return new Rezerwacja_Response(false, "Nie utworzono rezerwacji!");
        }
    };
}
