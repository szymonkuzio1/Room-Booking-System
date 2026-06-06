package pl.kregiel.kuzio.psk.projekt.exception;

public abstract class RezerwacjaException extends RuntimeException {

    private final String kodBledu;

    protected RezerwacjaException(String komunikat, String kodBledu) {
        super(komunikat);
        this.kodBledu = kodBledu;
    }

    public String getKodBledu() {
        return kodBledu;
    }
}
