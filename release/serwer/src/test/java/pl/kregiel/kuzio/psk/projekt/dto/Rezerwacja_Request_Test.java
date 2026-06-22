package pl.kregiel.kuzio.psk.projekt.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Rezerwacja_Request_Test {
    @Test
    void test_konstruktor() {

        Rezerwacja_Request request = new Rezerwacja_Request(
                5,
                "2026-06-30",
                "10:00",
                "12:00",
                "123456"
        );

        // powinien dokonać rezerwacji
        assertEquals(5, request.getId_prowadzacy());
        assertEquals("2026-06-30", request.getData_rezerwacji());
        assertEquals("10:00", request.getPoczatek_rezerwacji());
        assertEquals("12:00", request.getKoniec_rezerwacji());
        assertEquals("123456", request.getKod_totp());
    }
}
