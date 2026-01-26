package tech.sangdang.invoicer.modules.system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.common.constants.AppSecurity;

@Tag(name = "_Test")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class TestController {
    @GetMapping("/public/test")
    public String publicTest() {
        return "public test success";
    }
    
    @GetMapping("/private/test")
    @SecurityRequirement(name = AppSecurity.AUTH_CODE)
    public String privateTest(@AuthenticationPrincipal Jwt jwt) {
        log.info("JWT Claims: {}", jwt.getClaims());
        return "private test success";
    }
    
    @GetMapping("/admin/test")
    @SecurityRequirement(name = AppSecurity.AUTH_CODE)
    public String adminTest(@AuthenticationPrincipal Jwt principal) {
        log.info("Admin Attributes: {}", principal.getClaims());
        return "admin test success";
    }
    
    @GetMapping("/user/test")
    @SecurityRequirement(name = AppSecurity.AUTH_CODE)
    public String userTest(@AuthenticationPrincipal Jwt principal) {
        log.info("User Attributes: {}", principal.getClaims());
        return "user test success";
    }
}
