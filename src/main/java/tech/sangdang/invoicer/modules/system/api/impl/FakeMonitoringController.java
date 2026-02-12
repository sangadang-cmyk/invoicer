package tech.sangdang.invoicer.modules.system.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Fake monitoring endpoints for development testing
 */
@Slf4j
@RestController
@RequestMapping("/api/fake/monitoring")
public class FakeMonitoringController {

    @Value("${system.config.fake-services.monitoring.fake-metrics:true}")
    private boolean fakeMetricsEnabled;

    private final Random random = new Random();

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getFakeHealthMetrics() {
        log.debug("Generating fake health metrics");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("fake_cpu_usage", random.nextDouble() * 0.3 + 0.1); // 10-40%
        health.put("fake_memory_usage", random.nextDouble() * 0.4 + 0.3); // 30-70%
        health.put("fake_disk_usage", random.nextDouble() * 0.2 + 0.1); // 10-30%
        health.put("fake_active_connections", random.nextInt(100) + 50);
        health.put("fake_response_time_avg_ms", random.nextInt(200) + 50);
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getFakeApplicationMetrics() {
        log.debug("Generating fake application metrics");
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("fake_requests_total", random.nextInt(10000) + 5000);
        metrics.put("fake_errors_total", random.nextInt(50));
        metrics.put("fake_invoice_count", random.nextInt(500) + 100);
        metrics.put("fake_payment_success_rate", 0.95 + random.nextDouble() * 0.04);
        metrics.put("fake_encryption_operations", random.nextInt(1000));
        metrics.put("fake_rate_limit_hits", random.nextInt(20));
        metrics.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/security")
    public ResponseEntity<Map<String, Object>> getFakeSecurityMetrics() {
        log.debug("Generating fake security metrics");
        
        Map<String, Object> security = new HashMap<>();
        security.put("fake_failed_auth_attempts", random.nextInt(10));
        security.put("fake_blocked_ips", random.nextInt(5));
        security.put("fake_suspicious_requests", random.nextInt(3));
        security.put("fake_encryption_failures", random.nextInt(2));
        security.put("fake_jwt_validations", random.nextInt(1000) + 500);
        security.put("fake_last_vulnerability_scan", LocalDateTime.now().minusHours(2));
        security.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(security);
    }

    @GetMapping("/performance") 
    public ResponseEntity<Map<String, Object>> getFakePerformanceMetrics() {
        log.debug("Generating fake performance metrics");
        
        Map<String, Object> performance = new HashMap<>();
        performance.put("fake_avg_response_time", random.nextInt(150) + 25);
        performance.put("fake_p95_response_time", random.nextInt(300) + 100);
        performance.put("fake_p99_response_time", random.nextInt(500) + 200);
        performance.put("fake_throughput_rps", random.nextInt(100) + 50);
        performance.put("fake_database_query_time", random.nextInt(50) + 5);
        performance.put("fake_cache_hit_rate", 0.75 + random.nextDouble() * 0.2);
        performance.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(performance);
    }
}
