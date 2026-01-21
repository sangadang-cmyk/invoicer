package tech.sangdang.invoicer.modules.system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.common.constants.AppSecurity;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
    
    @SecurityRequirement(name = AppSecurity.OAUTH2)
    @GetMapping
    public String test() {
        log.info("Test endpoint called");
        return "Test endpoint is working";
    }
    
    @SecurityRequirement(name = AppSecurity.OAUTH2)
    @GetMapping("/me")
    public String testMe(@AuthenticationPrincipal Jwt jwt) {
        log.info("Test /me endpoint called");
        log.info(jwt.getClaims().toString());
        return "Test /me endpoint is working";
    }
}
