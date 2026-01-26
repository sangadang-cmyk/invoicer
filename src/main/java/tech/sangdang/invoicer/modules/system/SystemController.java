package tech.sangdang.invoicer.modules.system;

import io.swagger.v3.oas.annotations.Operation;
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

import java.time.Instant;

@Tag(name = "_System")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/system")
public class SystemController {
    private final SystemConfig systemConfig;
    
    @Operation(summary = "[system] Root health check. This should emit a log in the console")
    @GetMapping("root-health-check")
    public String rootHealthCheck() {
        log.info("Health check endpoint called at " + Instant.now().toString());
        return "Sang Dang says: SYSTEM IS RUNNING OK";
    }
    
    @Operation(summary = "[system] Get running version of the application")
    @GetMapping("/version")
    public String version() {
        log.info("Version endpoint called at " + Instant.now().toString());
        return systemConfig.getDeploymentVersion();
    }
}
