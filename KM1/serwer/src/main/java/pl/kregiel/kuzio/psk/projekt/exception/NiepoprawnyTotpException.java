package pl.kregiel.kuzio.psk.projekt.exception;

public class NiepoprawnyTotpException extends RezerwacjaException {

    public static final String KOD = "NIEPOPRAWNY_TOTP";

    public NiepoprawnyTotpException() {
        super("Podany kod TOTP jest niepoprawny lub wygasł. Operacja nie została zatwierdzona.", KOD);
    }
}
