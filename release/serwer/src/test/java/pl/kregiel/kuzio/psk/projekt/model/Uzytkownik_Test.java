package pl.kregiel.kuzio.psk.projekt.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Uzytkownik_Test {
    @Test
    void test_konstruktor_prowadzacy() {
        Uzytkownik uzytkownik = new Uzytkownik(
                "10",
                "jkowalski",
                "jkowalski@tu.kielce.pl",
                "test",
                "salt",
                "Prowadzacy"
        );

        // powinien utworzyć użytkownika w roli Prowadzącego
        assertEquals("10", uzytkownik.getId_uzytkownika());
        assertEquals("jkowalski", uzytkownik.getNazwa_uzytkownika());
        assertEquals("jkowalski@tu.kielce.pl", uzytkownik.getEmail());
        assertEquals("test", uzytkownik.getZahashowane_haslo());
        assertEquals("salt", uzytkownik.getSalt());
        assertEquals("Prowadzacy", uzytkownik.getRola());
    }

    @Test
    void test_setters_administrator() {
        Uzytkownik uzytkownik = new Uzytkownik();
        uzytkownik.setId_uzytkownika("20");
        uzytkownik.setNazwa_uzytkownika("tmucha");
        uzytkownik.setEmail("tmucha@tu.kielce.pl");
        uzytkownik.setZahashowane_haslo("test_admin");
        uzytkownik.setSalt("salt");
        uzytkownik.setRola("Administrator");

        // powinien utworzyć użytkownika w roli Administratora
        assertEquals("20", uzytkownik.getId_uzytkownika());
        assertEquals("tmucha", uzytkownik.getNazwa_uzytkownika());
        assertEquals("tmucha@tu.kielce.pl", uzytkownik.getEmail());
        assertEquals("test_admin", uzytkownik.getZahashowane_haslo());
        assertEquals("salt", uzytkownik.getSalt());
        assertEquals("Administrator", uzytkownik.getRola());
    }
}
