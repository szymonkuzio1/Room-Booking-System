package pl.kregiel.kuzio.psk.projekt.exception;

public class NiepoprawnyTotpException extends RezerwacjaException {

    public static final String KOD = "NIEPOPRAWNY_TOTP";

    public NiepoprawnyTotpException(String komunikat) {
        super(komunikat, KOD);
    }
}
