package pl.kregiel.kuzio.psk.projekt.exception;

public class BladZapisuException extends RezerwacjaException {

    public static final String KOD = "BLAD_ZAPISU";

    public BladZapisuException(String przyczyna) {
        super("Nie udało się zapisać rezerwacji: " + przyczyna, KOD);
    }

    public BladZapisuException(Throwable przyczyna) {
        super("Nie udało się zapisać rezerwacji: " + przyczyna.getMessage(), KOD);
    }
}
