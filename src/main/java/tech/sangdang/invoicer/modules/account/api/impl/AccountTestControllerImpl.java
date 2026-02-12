package tech.sangdang.invoicer.modules.account.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.account.api.AccountTestController;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;
import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AccountTestController.PATH)
public class AccountTestControllerImpl implements AccountTestController {
    private final AccountQueryPort accountQueryPort;

    @Override
    public List<UserDto> listUsers() {
        return accountQueryPort.listUsers();
    }

    @Override
    public List<UserDto> listUsers(String role) {
        return accountQueryPort.listUsers(role);
    }

    @Override
    public boolean existsAccountId(String accountId) {
        return accountQueryPort.existsUserById(accountId);
    }

    @Override
    public ResponseEntity<?> getAuthCodeSelf(Jwt jwt) {
        return ResponseEntity.ok(jwt.getClaims());
    }

    @Override
    public ResponseEntity<?> getClientCredentialsSelf(Jwt jwt) {
        return ResponseEntity.ok(jwt.getClaims());
    }

    // New method for testing rate limiting
    public ResponseEntity<?> testRateLimit(String testApiKey) {
        log.info("Testing rate limit with API key: {}", testApiKey.substring(0, 8) + "...");
        return ResponseEntity.ok().body("Rate limit test completed");
    }

    // New method for testing encryption
    public ResponseEntity<?> testEncryption(String payload) {
        // Fake encryption logic for testing
        String fakeEncryptedPayload = "enc_" + payload.hashCode();
        log.debug("Encrypted payload: {}", fakeEncryptedPayload);
        return ResponseEntity.ok().body(fakeEncryptedPayload);
    }
}
