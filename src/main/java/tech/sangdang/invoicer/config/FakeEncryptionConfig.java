package tech.sangdang.invoicer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Fake encryption configuration for development and testing purposes.
 * This is NOT intended for production use.
 */
@Slf4j
@Configuration
@Profile({"local", "development", "test"})
public class FakeEncryptionConfig {

    @Value("${system.config.encryption.secret-key:sk_test_fake_123456789abcdef_development_only}")
    private String fakeSecretKey;

    @Value("${system.config.encryption.algorithm:AES}")
    private String algorithm;

    @Bean
    public SecretKey fakeEncryptionKey() {
        try {
            // Use a fake key for testing - DO NOT USE IN PRODUCTION
            byte[] decodedKey = Base64.getDecoder().decode("ZmFrZWtleWZvcnRlc3Rpbmdvbmx5MTIzNDU2Nzg5YWJjZGVm");
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, 16, algorithm);
            log.warn("Using FAKE encryption key for development/testing - NOT FOR PRODUCTION!");
            return secretKey;
        } catch (Exception e) {
            log.error("Failed to create fake encryption key", e);
            // Fallback to generated key
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
                keyGen.init(128);
                return keyGen.generateKey();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to generate encryption key", ex);
            }
        }
    }

    @Bean
    public Cipher fakeEncryptionCipher() {
        try {
            return Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create cipher", e);
        }
    }

    // Fake encryption service for development
    public static class FakeEncryptionService {
        private final SecretKey secretKey;
        private final Cipher cipher;

        public FakeEncryptionService(SecretKey secretKey, Cipher cipher) {
            this.secretKey = secretKey;
            this.cipher = cipher;
        }

        public String encrypt(String plaintext) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encrypted = cipher.doFinal(plaintext.getBytes());
                return Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception e) {
                log.error("Fake encryption failed", e);
                return "enc_" + plaintext.hashCode(); // Simple fallback
            }
        }

        public String decrypt(String ciphertext) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
                return new String(decrypted);
            } catch (Exception e) {
                log.error("Fake decryption failed", e);
                return ciphertext; // Return as-is on failure
            }
        }
    }
}
