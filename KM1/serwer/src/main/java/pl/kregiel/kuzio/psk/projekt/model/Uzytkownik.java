package pl.kregiel.kuzio.psk.projekt.model;

// klasa reprezentująca użytkownika systemu
public class Uzytkownik {
    
    private String id_uzytkownika;
    private String nazwa_uzytkownika;
    private String email;
    private String zahashowane_haslo;
    private String salt;
    private String rola; // np. "ADMIN", "USER"

    public Uzytkownik() {}

    public Uzytkownik(String id_uzytkownika, String nazwa_uzytkownika, String email, 
                     String zahashowane_haslo, String salt, String rola) {
        this.id_uzytkownika = id_uzytkownika;
        this.nazwa_uzytkownika = nazwa_uzytkownika;
        this.email = email;
        this.zahashowane_haslo = zahashowane_haslo;
        this.salt = salt;
        this.rola = rola;
    }

    public String getId_uzytkownika() {
        return id_uzytkownika;
    }
    public void setId_uzytkownika(String id_uzytkownika) {
        this.id_uzytkownika = id_uzytkownika;
    }

    public String getNazwa_uzytkownika() {
        return nazwa_uzytkownika;
    }
    public void setNazwa_uzytkownika(String nazwa_uzytkownika) {
        this.nazwa_uzytkownika = nazwa_uzytkownika;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getZahashowane_haslo() {
        return zahashowane_haslo;
    }
    public void setZahashowane_haslo(String zahashowane_haslo) {
        this.zahashowane_haslo = zahashowane_haslo;
    }

    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRola() {
        return rola;
    }
    public void setRola(String rola) {
        this.rola = rola;
    }
}
