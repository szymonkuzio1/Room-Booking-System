# System Bezpiecznego Przechowywania Haseł

## ✅ WERYFIKACJA - BEZPIECZNA INTEGRACJA

**Status**: ✅ **GOTOWE DO INTEGRACJI** - Brak konfliktów z najnowszą wersją projektu!

### Zmiany w projekcie współpracownika:
- ✅ Połączono kontrolery w jeden `Controller.java`
- ✅ Dodano endpoint pobierania rezerwacji
- ✅ Zmieniono parametry API (sala przesyłana jako @PathVariable)
- ✅ Brak zmian w zależnościach (pom.xml)

### Pliki dodane przez system hashowania:
- ✅ `util/Haslo_Utility.java` - NOWY katalog i plik
- ✅ `model/Uzytkownik.java` - NOWY plik
- ✅ `service/Uzytkownik_Service.java` - NOWY plik
- ✅ `test/util/Haslo_Utility_Test.java` - NOWY katalog i plik

**Wszystkie pliki są nowe i nie kolidują z istniejącym kodem!** 🎉

---

## Opis

System hashowania haseł przy użyciu **SHA-256** z solą (salt) dla projektu rezerwacji sal.

## Stworzone komponenty

### 1. `Haslo_Utility.java` (util)
Klasa narzędziowa do hashowania i weryfikacji haseł.

**Główne metody:**
- `generuj_salt()` - generuje losową sól (16 bajtów)
- `hashuj_haslo(haslo, salt)` - hashuje hasło z podaną solą
- `weryfikuj_haslo(haslo, hash, salt)` - weryfikuje czy hasło jest poprawne
- `hashuj_haslo_z_nowa_sola(haslo)` - hashuje hasło z automatycznie wygenerowaną solą

### 2. `Uzytkownik.java` (model)
Model użytkownika z polami do przechowywania zahashowanego hasła i soli.

**Pola:**
- `id_uzytkownika` - unikalny identyfikator
- `nazwa_uzytkownika` - nazwa logowania
- `email` - adres email
- `zahashowane_haslo` - hash hasła (SHA-256)
- `salt` - losowa sól użyta do hashowania
- `rola` - rola użytkownika (np. "ADMIN", "USER")

### 3. `Uzytkownik_Service.java` (service)
Serwis do zarządzania użytkownikami.

**Funkcje:**
- Rejestracja nowego użytkownika
- Logowanie (weryfikacja hasła)
- Zmiana hasła
- Pobieranie listy użytkowników

### 4. `Haslo_Utility_Test.java` (test)
Testy jednostkowe i przykłady użycia.

## Przykłady użycia

### Rejestracja użytkownika

```java
// 1. Hashowanie hasła podczas rejestracji
String haslo = "MojeSuperbezpieczneHaslo123!";
String[] dane_hasla = Haslo_Utility.hashuj_haslo_z_nowa_sola(haslo);

String hash = dane_hasla[0];  // zapisz w bazie
String salt = dane_hasla[1];  // zapisz w bazie

// 2. Utworzenie obiektu użytkownika
Uzytkownik uzytkownik = new Uzytkownik(
    UUID.randomUUID().toString(),
    "jan.kowalski",
    "jan@example.com",
    hash,
    salt,
    "USER"
);
```

### Logowanie użytkownika

```java
// 1. Odczytaj użytkownika z bazy/pliku JSON
// 2. Weryfikuj hasło
String podane_haslo = "MojeSuperbezpieczneHaslo123!";
boolean czy_poprawne = Haslo_Utility.weryfikuj_haslo(
    podane_haslo,
    uzytkownik.getZahashowane_haslo(),
    uzytkownik.getSalt()
);

if (czy_poprawne) {
    // Logowanie udane
} else {
    // Błędne hasło
}
```

### Zmiana hasła

```java
// 1. Weryfikuj stare hasło
// 2. Wygeneruj nowy hash z nową solą
String[] nowe_dane = Haslo_Utility.hashuj_haslo_z_nowa_sola("NoweHaslo456!");

uzytkownik.setZahashowane_haslo(nowe_dane[0]);
uzytkownik.setSalt(nowe_dane[1]);
```

## Bezpieczeństwo

### Dlaczego używamy soli (salt)?

1. **Ochrona przed rainbow tables** - każde hasło ma unikalną sól
2. **Unikalne hashe** - to samo hasło dla dwóch użytkowników ma różne hashe
3. **Losowość** - używamy `SecureRandom` do generowania soli

### Format przechowywania w JSON

```json
{
  "id_uzytkownika": "123e4567-e89b-12d3-a456-426614174000",
  "nazwa_uzytkownika": "jan.kowalski",
  "email": "jan@example.com",
  "zahashowane_haslo": "xJhRm8u8qUvJ7T3jNdYvJpNk5dY=...",
  "salt": "aB3dE6fG7hI8jK9lM0nO1pQ2rS3tU4vW5xY6zA7=",
  "rola": "USER"
}
```

## Integracja z aktualną wersją projektu

### Przyszłe dodanie użytkownika do rezerwacji

W pliku `Rezerwacja.java` można dodać:
```java
private String id_uzytkownika; // ID użytkownika tworzącego rezerwację
```

### Przyszła weryfikacja uprawnień

W `Controller.java` można dodać endpoint:
```java
@PostMapping("/uzytkownicy/logowanie")
public ResponseEntity<?> zaloguj(@RequestBody LoginRequest request) {
    Uzytkownik uzytkownik = uzytkownik_service.zaloguj_uzytkownika(
        request.getNazwa_uzytkownika(), 
        request.getHaslo()
    );
    if (uzytkownik == null) {
        throw new BrakUprawnienException(request.getNazwa_uzytkownika());
    }
    // zwróć token/sesję
}
```

### Struktura endpointów (aktualna wersja)

**Istniejące:**
- `GET /api/pietra/{pietro}/sale` - pobierz sale
- `POST /api/pietra/{pietro}/sale/{sala}/rezerwacja` - utwórz rezerwację
- `GET /api/pietra/{pietro}/sale/{sala}/rezerwacje` - pobierz rezerwacje

**Do dodania w przyszłości:**
- `POST /api/uzytkownicy/rejestracja` - rejestracja
- `POST /api/uzytkownicy/logowanie` - logowanie
- `PUT /api/uzytkownicy/haslo` - zmiana hasła

## Uruchomienie testów

```bash
cd KM1/serwer
mvn test -Dtest=Haslo_Utility_Test
```

## Uwagi techniczne

- **Algorytm**: SHA-256
- **Długość soli**: 16 bajtów (128 bitów)
- **Kodowanie**: Base64 (dla łatwego przechowywania)
- **Bezpieczeństwo**: Każde hasło ma unikalną losową sól
- **Plik danych**: `uzytkownicy.json` (tworzone automatycznie)
- **Brak zależności**: Używa tylko standardowej biblioteki Java (java.security)

## Pliki w projekcie

```
KM1/serwer/src/main/java/pl/kregiel/kuzio/psk/projekt/
├── config/
│   ├── Initializer.java
│   └── Jackson_Config.java
├── controller/
│   └── Controller.java
├── dto/
│   ├── Rezerwacja_Request.java
│   ├── Rezerwacja_Response.java
│   └── UzytkownikDTO.java
├── exception/
│   ├── BladZapisuException.java
│   ├── BrakSaliException.java
│   ├── BrakUprawnienException.java
│   ├── NiepoprawneZapytanieException.java
│   ├── NiepoprawnyTotpException.java
│   ├── RezerwacjaException.java
│   └── SalaZajetaException.java
├── model/
│   ├── Rezerwacja.java
│   └── Uzytkownik.java ⬅️ NOWY
├── service/
│   ├── Pietro_Service.java
│   ├── Rezerwacja_Service.java
│   └── Uzytkownik_Service.java ⬅️ NOWY
└── util/
    └── Haslo_Utility.java ⬅️ NOWY

KM1/serwer/src/test/java/pl/kregiel/kuzio/psk/projekt/
└── util/
    └── Haslo_Utility_Test.java ⬅️ NOWY
```

## Instrukcja instalacji

1. **Skopiuj nowe pliki** do swojego projektu (wszystkie są w pakiecie ZIP)
2. **Struktura katalogów** zostanie automatycznie utworzona
3. **Brak zmian w pom.xml** - nie trzeba dodawać zależności
4. **Brak konfliktów** - żaden istniejący plik nie został zmieniony
5. **Gotowe do użycia** - można od razu rozpocząć testowanie

## Następne kroki (opcjonalne)

Po zakończeniu prac nad interfejsem można dodać:
1. Endpointy REST dla użytkowników
2. Sesje/tokeny JWT
3. Integrację z rezerwacjami (dodanie id_uzytkownika)
4. Role i uprawnienia (admin vs user)
5. TOTP/2FA (wykorzystując NiepoprawnyTotpException)
