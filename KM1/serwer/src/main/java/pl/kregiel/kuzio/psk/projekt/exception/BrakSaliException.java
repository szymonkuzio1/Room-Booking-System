package pl.kregiel.kuzio.psk.projekt.exception;

public class BrakSaliException extends RezerwacjaException {

    public static final String KOD = "BRAK_SALI";

    public BrakSaliException(int idSali) {
        super("Sala o identyfikatorze " + idSali + " nie istnieje w systemie.", KOD);
    }
}
