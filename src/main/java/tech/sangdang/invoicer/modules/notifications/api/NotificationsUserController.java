package tech.sangdang.invoicer.modules.notifications.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import tech.sangdang.invoicer.common.constants.AppSecurity;

@SecurityRequirement(name = AppSecurity.AUTH_CODE)
@Tag(name = "Notifications")
public interface NotificationsUserController {
    String PATH = "/user/notifications";
    
    @Operation(summary = "Subscribe to email notifications")
    @PostMapping("/email/subscribe")
    ResponseEntity<Void> subscribeToEmailNotifications(@AuthenticationPrincipal() Jwt principal);
}
