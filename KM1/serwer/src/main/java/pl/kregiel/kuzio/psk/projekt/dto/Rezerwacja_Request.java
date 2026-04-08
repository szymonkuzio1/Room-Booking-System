package pl.kregiel.kuzio.psk.projekt.dto;

// klasa zawierająca informacje niezbędne do utworzenia rezerwacji
public class Rezerwacja_Request {
    private int sala;
    private String data_rezerwacji;
    private String poczatek_rezerwacji;
    private String koniec_rezerwacji;

    public Rezerwacja_Request(int sala, String data_rezerwacji, String poczatek_rezerwacji, String koniec_rezerwacji) {
        this.sala = sala;
        this.data_rezerwacji = data_rezerwacji;
        this.poczatek_rezerwacji = poczatek_rezerwacji;
        this.koniec_rezerwacji = koniec_rezerwacji;
    }

    public int getSala() {
        return sala;
    }
    public void setSala(int sala) {
        this.sala = sala;
    }
    public String getData_rezerwacji() {
        return data_rezerwacji;
    }
    public void setData_rezerwacji(String data_rezerwacji) {
        this.data_rezerwacji = data_rezerwacji;
    }
    public String getPoczatek_rezerwacji() {
        return poczatek_rezerwacji;
    }
    public void setPoczatek_rezerwacji(String poczatek_rezerwacji) {
        this.poczatek_rezerwacji = poczatek_rezerwacji;
    }
    public String getKoniec_rezerwacji() {
        return koniec_rezerwacji;
    }
    public void setKoniec_rezerwacji(String koniec_rezerwacji) {
        this.koniec_rezerwacji = koniec_rezerwacji;
    }
}

