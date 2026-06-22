package pl.kregiel.kuzio.psk.projekt.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Exception_Test {
    @Test
    void test_bladZapisuException() {
        BladZapisuException exception = new BladZapisuException("blad");

        // powinien zwrócić kod błędu
        assertEquals("BLAD_ZAPISU", exception.getKodBledu());
        assertTrue(exception.getMessage().contains("blad"));
    }

    @Test
    void test_brakSaliException() {
        BrakSaliException exception = new BrakSaliException(999);

        // powinien zwrócić kod błędu
        assertEquals("BRAK_SALI", exception.getKodBledu());
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void test_brakUprawnienException() {
        BrakUprawnienException exception = new BrakUprawnienException("Brak uprawnien!");

        // powinien zwrócić kod błędu
        assertEquals("BRAK_UPRAWNIEN", exception.getKodBledu());
        assertEquals("Brak uprawnien!", exception.getMessage());
    }

    @Test
    void test_niepoprawneZapytanieException() {
        NiepoprawneZapytanieException exception =
                new NiepoprawneZapytanieException("Niepoprawne zapytanie!");

        // powinien zwrócić kod błędu
        assertEquals("NIEPOPRAWNE_ZAPYTANIE", exception.getKodBledu());
        assertEquals("Niepoprawne zapytanie!", exception.getMessage());
    }

    @Test
    void test_niepoprawnyTotpException() {
        NiepoprawnyTotpException exception =
                new NiepoprawnyTotpException("Nieprawidlowy kod TOTP!");

        // powinien zwrócić kod błędu
        assertEquals("NIEPOPRAWNY_TOTP", exception.getKodBledu());
        assertEquals("Nieprawidlowy kod TOTP!", exception.getMessage());
    }

    @Test
    void test_salaZajetaException() {
        SalaZajetaException exception =
                new SalaZajetaException(306, "2026-06-30", "10:00", "12:00");

        // powinien zwrócić kod błędu
        assertEquals("SALA_ZAJETA", exception.getKodBledu());
        assertTrue(exception.getMessage().contains("306"));
        assertTrue(exception.getMessage().contains("2026-06-30"));
    }
}
