package pl.kregiel.kuzio.psk.projekt.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// test demonstracyjny dla hashowania haseł
class Haslo_Utility_Test {

    @Test
    void test_generowania_soli() {
        // generowanie dwóch soli
        String salt1 = Haslo_Utility.generuj_salt();
        String salt2 = Haslo_Utility.generuj_salt();

        // każda sól powinna być różna
        assertNotNull(salt1);
        assertNotNull(salt2);
        assertNotEquals(salt1, salt2);
    }

    @Test
    void test_hashowania_hasla() throws Exception {
        String haslo = "MojeSuperbezpieczneHaslo123!";
        String salt = Haslo_Utility.generuj_salt();

        // hashowanie tego samego hasła z tą samą solą
        String hash1 = Haslo_Utility.hashuj_haslo(haslo, salt);
        String hash2 = Haslo_Utility.hashuj_haslo(haslo, salt);

        // powinny być identyczne
        assertNotNull(hash1);
        assertEquals(hash1, hash2);

        // różna sól = różny hash
        String inna_salt = Haslo_Utility.generuj_salt();
        String hash3 = Haslo_Utility.hashuj_haslo(haslo, inna_salt);
        assertNotEquals(hash1, hash3);
    }

    @Test
    void test_weryfikacji_hasla() throws Exception {
        String haslo = "TestoweHaslo456";
        String salt = Haslo_Utility.generuj_salt();
        String hash = Haslo_Utility.hashuj_haslo(haslo, salt);

        // prawidłowe hasło powinno przejść weryfikację
        assertTrue(Haslo_Utility.weryfikuj_haslo(haslo, hash, salt));

        // nieprawidłowe hasło nie powinno przejść
        assertFalse(Haslo_Utility.weryfikuj_haslo("ZleHaslo", hash, salt));
    }

    @Test
    void test_hashowania_z_automatyczna_sola() throws Exception {
        String haslo = "AutomatyczneSol2024";

        // hashowanie z automatycznym generowaniem soli
        String[] wynik = Haslo_Utility.hashuj_haslo_z_nowa_sola(haslo);
        String hash = wynik[0];
        String salt = wynik[1];

        // weryfikacja
        assertNotNull(hash);
        assertNotNull(salt);
        assertTrue(Haslo_Utility.weryfikuj_haslo(haslo, hash, salt));
    }

    @Test
    void test_praktycznego_uzycia() throws Exception {
        // PRZYKŁAD UŻYCIA W PRAKTYCE:
        
        // 1. Rejestracja użytkownika - tworzenie nowego hasła
        String haslo_uzytkownika = "NowyUzytkownik123!";
        String[] dane_hasla = Haslo_Utility.hashuj_haslo_z_nowa_sola(haslo_uzytkownika);
        String zahashowane_haslo = dane_hasla[0];
        String salt = dane_hasla[1];
        
        // te wartości zapisujemy do bazy/pliku JSON
        System.out.println("Hash do zapisania: " + zahashowane_haslo);
        System.out.println("Salt do zapisania: " + salt);
        
        // 2. Logowanie użytkownika - weryfikacja hasła
        // odczytujemy zahashowane_haslo i salt z bazy/pliku
        String podane_haslo = "NowyUzytkownik123!";
        boolean czy_poprawne = Haslo_Utility.weryfikuj_haslo(podane_haslo, zahashowane_haslo, salt);
        
        assertTrue(czy_poprawne, "Hasło powinno być poprawne");
        
        // 3. Niepoprawne hasło
        String bledne_haslo = "ZleHaslo";
        boolean czy_bledne = Haslo_Utility.weryfikuj_haslo(bledne_haslo, zahashowane_haslo, salt);
        
        assertFalse(czy_bledne, "Błędne hasło nie powinno przejść weryfikacji");
    }
}
