package pl.kregiel.kuzio.psk.projekt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

// klasa narzędziowa do bezpiecznego hashowania haseł
public class Haslo_Utility {

    private static final String ALGORYTM = "SHA-256";
    private static final int DLUGOSC_SALT = 16;

    // wygenerowanie losowej soli
    public static String generuj_salt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[DLUGOSC_SALT];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // zahashowanie hasła z użyciem soli
    public static String hashuj_haslo(String haslo, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORYTM);
        
        // połączenie hasła z solą
        String haslo_z_sola = haslo + salt;
        
        // hashowanie
        byte[] hash = digest.digest(haslo_z_sola.getBytes());
        
        // konwersja do Base64 dla łatwego przechowywania
        return Base64.getEncoder().encodeToString(hash);
    }

    // weryfikacja hasła
    public static boolean weryfikuj_haslo(String haslo, String zahashowane_haslo, String salt) {
        try {
            String nowy_hash = hashuj_haslo(haslo, salt);
            return nowy_hash.equals(zahashowane_haslo);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    // pomocnicza metoda - hashowanie z automatycznym generowaniem soli
    // zwraca tablicę: [0] = hash, [1] = salt
    public static String[] hashuj_haslo_z_nowa_sola(String haslo) throws NoSuchAlgorithmException {
        String salt = generuj_salt();
        String hash = hashuj_haslo(haslo, salt);
        return new String[]{hash, salt};
    }
}
