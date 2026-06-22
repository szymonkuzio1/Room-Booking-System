package pl.kregiel.kuzio.psk.projekt.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pl.kregiel.kuzio.psk.projekt.dto.Rezerwacja_Request;
import pl.kregiel.kuzio.psk.projekt.exception.NiepoprawneZapytanieException;

class Rezerwacja_Service_Test {
    @Test
    void test_utworzRezerwacje_data_z_przeszlosci() {
        Rezerwacja_Service service = new Rezerwacja_Service();
        Rezerwacja_Request request = new Rezerwacja_Request(
                5,
                "2025-06-22",
                "10:00",
                "12:00",
                "123456"
        );

        // powinien rzucić wyjątek dla daty z przeszłości
        assertThrows(
                NiepoprawneZapytanieException.class,
                () -> service.utworz_rezerwacje(1, 306, request)
        );
    }

    @Test
    void test_utworzRezerwacje_godzina_zakonczenia_wczesniejsza_niz_godzina_poczatku() {
        Rezerwacja_Service service = new Rezerwacja_Service();
        Rezerwacja_Request request = new Rezerwacja_Request(
                5,
                "2025-06-25",
                "12:00",
                "10:00",
                "123456"
        );

        // powinien rzucić wyjątek dla godziny rozpoczęcia i zakończenia
        assertThrows(
                NiepoprawneZapytanieException.class,
                () -> service.utworz_rezerwacje(1, 306, request)
        );
    }

    @Test
    void test_utworzRezerwacje_takie_same_godziny() {
        Rezerwacja_Service service = new Rezerwacja_Service();
        Rezerwacja_Request request = new Rezerwacja_Request(
                5,
                "2025-06-25",
                "10:00",
                "10:00",
                "123456"
        );

        // powinien rzucić wyjątek dla takiej samej godziny rozpoczęcia i zakończenia
        assertThrows(
                NiepoprawneZapytanieException.class,
                () -> service.utworz_rezerwacje(1, 306, request)
        );
    }
}