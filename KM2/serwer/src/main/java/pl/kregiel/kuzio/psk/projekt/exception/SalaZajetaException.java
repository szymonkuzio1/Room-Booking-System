package pl.kregiel.kuzio.psk.projekt.exception;

public class SalaZajetaException extends RezerwacjaException {

    public static final String KOD = "SALA_ZAJETA";

    public SalaZajetaException(int idSali, String data, String poczatek, String koniec) {
        super("Sala " + idSali + " jest już zajęta w dniu " + data
                + " w godzinach " + poczatek + "–" + koniec + ".", KOD);
    }
}
