package pl.kregiel.kuzio.psk.projekt.exception;

public class BrakUprawnienException extends RezerwacjaException {

    public static final String KOD = "BRAK_UPRAWNIEN";

    public BrakUprawnienException(String komunikat) {
        super(komunikat, KOD);
    }
}
