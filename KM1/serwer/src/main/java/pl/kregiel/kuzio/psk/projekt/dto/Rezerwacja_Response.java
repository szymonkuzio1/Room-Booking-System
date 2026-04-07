package pl.kregiel.kuzio.psk.projekt.dto;

public class Rezerwacja_Response {
    private boolean status;
    private String odpowiedz;

    public Rezerwacja_Response() {}

    public Rezerwacja_Response(boolean status, String odpowiedz) {
        this.status = status;
        this.odpowiedz = odpowiedz;
    }

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getOdpowiedz() {
        return odpowiedz;
    }
    public void setOdpowiedz(String odpowiedz) {
        this.odpowiedz = odpowiedz;
    }
}
