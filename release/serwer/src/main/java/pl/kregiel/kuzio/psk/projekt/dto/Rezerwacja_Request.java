package pl.kregiel.kuzio.psk.projekt.dto;

// klasa zawierająca informacje niezbędne do utworzenia rezerwacji
public class Rezerwacja_Request {
    private int id_prowadzacy;
    private String data_rezerwacji;
    private String poczatek_rezerwacji;
    private String koniec_rezerwacji;
    private String kod_totp;

    public Rezerwacja_Request(int id_prowadzacy, String data_rezerwacji, String poczatek_rezerwacji, String koniec_rezerwacji, String kod_totp) {
        this.id_prowadzacy = id_prowadzacy;
        this.data_rezerwacji = data_rezerwacji;
        this.poczatek_rezerwacji = poczatek_rezerwacji;
        this.koniec_rezerwacji = koniec_rezerwacji;
        this.kod_totp = kod_totp;
    }

    public int getId_prowadzacy() {
        return id_prowadzacy;
    }
    public void setId_Prowadzacy(int id_prowadzacy){
        this.id_prowadzacy = id_prowadzacy;
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
    public String getKod_totp() {
        return kod_totp;
    }
    public void setKod_totp(String kod_totp) {
        this.kod_totp = kod_totp;
    }
}

