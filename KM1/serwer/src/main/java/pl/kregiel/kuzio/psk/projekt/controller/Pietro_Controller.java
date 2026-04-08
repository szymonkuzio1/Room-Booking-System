package pl.kregiel.kuzio.psk.projekt.controller;

import java.util.List;
import pl.kregiel.kuzio.psk.projekt.service.Pietro_Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pietra")
public class Pietro_Controller {
    private Pietro_Service pietro_service;
    public Pietro_Controller(Pietro_Service pietro_service) {
        this.pietro_service = pietro_service;
    }

    @GetMapping("/{pietro}/sale")
    public List<Integer> pobierz_sale(@PathVariable int pietro) throws Exception {
        return pietro_service.sale(pietro);
    }
}
