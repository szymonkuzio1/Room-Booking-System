package pl.kregiel.kuzio.psk.projekt.controller;

import java.util.List;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Response;
import pl.kregiel.kuzio.psk.projekt.model.Rezerwacja;
import pl.kregiel.kuzio.psk.projekt.service.Pietro_Service;
import pl.kregiel.kuzio.psk.projekt.service.Rezerwacja_Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pietra")
public class Controller {
    private Pietro_Service pietro_service;
    private Rezerwacja_Service rezerwacja_service;
    public Controller(Pietro_Service pietro_service, Rezerwacja_Service rezerwacja_service) {
        this.pietro_service = pietro_service;
        this.rezerwacja_service = rezerwacja_service;
    }

    @GetMapping("/{pietro}/sale")
    public List<Integer> pobierz_sale(@PathVariable int pietro) throws Exception {
        return pietro_service.sale(pietro);
    }

    @PostMapping("/{pietro}/sale/{sala}/rezerwacja")
    public Rezerwacja_Response utworz_rezerwacje(@PathVariable int sala, @RequestBody Rezerwacja_Request request
    ) throws Exception {
        boolean utworzono_rezerwacje = rezerwacja_service.utworz_rezerwacje(sala, request);
        if(utworzono_rezerwacje) {
            return new Rezerwacja_Response(true, "Utworzono rezerwacje!");
        }
        else {
            return new Rezerwacja_Response(false, "Nie utworzono rezerwacji!");
        }
    }

    @GetMapping("/{pietro}/sale/{sala}/rezerwacje")
    public List<Rezerwacja> pobierz_rezerwacje(@PathVariable int pietro, @PathVariable int sala) throws Exception {
        return rezerwacja_service.pobierz_rezerwacje(sala);
    }
}
