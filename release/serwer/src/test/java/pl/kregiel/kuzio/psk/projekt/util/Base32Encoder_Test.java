package pl.kregiel.kuzio.psk.projekt.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base32Encoder_Test {
    @Test
    void test_encode() {
        byte[] dane = "test".getBytes();

        // operacja kodowania Base32
        String wynik = Base32Encoder.encode(dane);

        // wynik nie powinien być pusty
        assertNotNull(wynik);
        assertFalse(wynik.isEmpty());
    }

    @Test
    void test_decode() {
        byte[] dane = "sekret".getBytes();

        // operacja dekodowania Base32
        String zakodowane = Base32Encoder.encode(dane);
        byte[] odkodowane = Base32Encoder.decode(zakodowane);
        String oryginal = new String(dane);
        String wynik = new String(odkodowane);

        // powinny być identyczne
        assertEquals(oryginal, wynik);
    }
}