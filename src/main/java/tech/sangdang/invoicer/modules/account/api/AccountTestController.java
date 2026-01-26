package tech.sangdang.invoicer.modules.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.modules.account.infra.UserDto;

import java.util.List;

@Tag(name = "_Test")
public interface AccountTestController {
    String PATH = "/public/test/account";

    @GetMapping("/batch")
    default List<UserDto> listUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @GetMapping("/{accountId}/exists")
    default boolean existsAccountId(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SecurityRequirement(name = AppSecurity.AUTH_CODE)
    @Operation(summary = "[system] Get current authorization code account")
    @GetMapping("/auth/ac/self")
    default ResponseEntity<?> getAuthCodeSelf(@AuthenticationPrincipal Jwt jwt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @SecurityRequirement(name = AppSecurity.CLIENT_CREDENTIALS)
    @Operation(summary = "[system] Get current client credentials account")
    @GetMapping("/auth/cc/self")
    default ResponseEntity<?> getClientCredentialsSelf(@AuthenticationPrincipal Jwt jwt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
