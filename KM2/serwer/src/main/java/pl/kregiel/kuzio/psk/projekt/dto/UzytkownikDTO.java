package pl.kregiel.kuzio.psk.projekt.dto;

public class UzytkownikDTO {

    private String idUzytkownika;

    private String imie;

    private String nazwisko;

    private String email;

    public UzytkownikDTO() {}

    public UzytkownikDTO(String idUzytkownika, String imie, String nazwisko, String email) {
        this.idUzytkownika = idUzytkownika;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
    }

    public String getIdUzytkownika() { return idUzytkownika; }
    public void setIdUzytkownika(String idUzytkownika) { this.idUzytkownika = idUzytkownika; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
