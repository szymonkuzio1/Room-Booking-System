package pl.kregiel.kuzio.psk.projekt.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TotpUtil_Test {
    @Test
    void test_generateSecret() {
        String secret1 = TotpUtil.generateSecret();
        String secret2 = TotpUtil.generateSecret();

        // sekrety powinny być różne
        assertNotEquals(secret1, secret2);
    }

    @Test
    void test_verifyCode() {
        String secret = TotpUtil.generateSecret();
        boolean wynik = TotpUtil.verifyCode(secret, "000000");

        assertFalse(wynik);
    }

    @Test
    void test_generateTotpUri() {
        String secret = "JBSWY3DPEHPK3PXP";
        String uri = TotpUtil.generateTotpUri("SystemRezerwacji", "jan", secret);

        // powinno zawierać dane
        assertTrue(uri.startsWith("otpauth://totp/"));
        assertTrue(uri.contains("SystemRezerwacji"));
        assertTrue(uri.contains("jan"));
        assertTrue(uri.contains(secret));
    }

    @Test
    void test_generateQrCode() {
        String secret = "JBSWY3DPEHPK3PXP";
        String uri = TotpUtil.generateTotpUri("SystemRezerwacji", "jan", secret);
        String qr = TotpUtil.generateQrCode(uri);

        // powinien zwrócić Base64 kodu QR
        assertNotNull(qr);
        assertTrue(qr.startsWith("data:image/png;base64,"));
    }
}