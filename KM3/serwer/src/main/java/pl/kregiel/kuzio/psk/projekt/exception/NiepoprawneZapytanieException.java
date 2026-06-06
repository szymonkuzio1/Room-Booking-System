package pl.kregiel.kuzio.psk.projekt.exception;

public class NiepoprawneZapytanieException extends RezerwacjaException {

    public static final String KOD = "NIEPOPRAWNE_ZAPYTANIE";

    public NiepoprawneZapytanieException(String komunikat) {
        super(komunikat, KOD);
    }
}
