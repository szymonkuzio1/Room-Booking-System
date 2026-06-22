package pl.kregiel.kuzio.psk.projekt.controller;

import org.springframework.web.bind.annotation.*;

import pl.kregiel.kuzio.psk.projekt.model.Uzytkownik;
import pl.kregiel.kuzio.psk.projekt.service.Uzytkownik_Service;

import java.util.Map;

@RestController
@RequestMapping("/api/uzytkownicy")
public class Uzytkownik_Controller {

    private final Uzytkownik_Service uzytkownik_service;

    public Uzytkownik_Controller(Uzytkownik_Service uzytkownik_service) {
        this.uzytkownik_service = uzytkownik_service;
    }

    @PostMapping("/rejestracja")
    public Map<String, Object> rejestracja(@RequestBody Map<String, String> body) throws Exception {
        String qr_code = uzytkownik_service.zarejestruj(
                body.get("nazwa_uzytkownika"),
                body.get("email"),
                body.get("haslo")
        );

        if (qr_code == null) {
            return Map.of("status", false);
        }

        return Map.of("status", true, "qr_code", qr_code);
    }

    @PostMapping("/logowanie")
    public Map<String, Object> logowanie(@RequestBody Map<String, String> body) throws Exception {
        Uzytkownik uzytkownik = uzytkownik_service.zaloguj(
                body.get("email"),
                body.get("haslo")
        );

        if (uzytkownik == null) {
            return Map.of("status", false);
        }

        return Map.of(
                "status", true,
                "id", uzytkownik.getId_uzytkownika(),
                "nazwa_uzytkownika", uzytkownik.getNazwa_uzytkownika(),
                "email", uzytkownik.getEmail(),
                "rola", uzytkownik.getRola()
        );
    }
}