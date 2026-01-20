package tech.sangdang.invoicer;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "_System")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/system")
public class SystemController {
    @GetMapping("root-health-check")
    public String rootHealthCheck() {
        log.info("Health check endpoint called at " + Instant.now().toString());
        return "Sang Dang says: SYSTEM IS RUNNING OK";
    }
    
    @GetMapping("/version")
    public String version() {
        return "2.2.2";
    }
}
