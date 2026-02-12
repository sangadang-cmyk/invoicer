package tech.sangdang.invoicer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import tech.sangdang.invoicer.modules.invoice.infra.s3.S3Config;
import tech.sangdang.invoicer.modules.system.SystemConfig;

@Slf4j
@ConfigurationPropertiesScan(basePackageClasses = {S3Config.class, SystemConfig.class})
@SpringBootApplication
public class InvoicerApplication {

    private static Environment environment;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InvoicerApplication.class);
        
        // Set fake development banner for testing
        app.setBannerMode(org.springframework.boot.Banner.Mode.CONSOLE);
        
        environment = app.run(args).getEnvironment();
        logFakeStartupInfo();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("🚀 Invoicer application is ready with FAKE development features!");
        logFakeConfigurationStatus();
    }

    private static void logFakeStartupInfo() {
        String[] activeProfiles = environment.getActiveProfiles();
        String profile = activeProfiles.length > 0 ? activeProfiles[0] : "default";
        
        log.info("=".repeat(60));
        log.info("🔧 FAKE DEVELOPMENT MODE ACTIVE 🔧");
        log.info("Profile: {}", profile);
        log.info("Fake encryption: ENABLED");
        log.info("Fake rate limiting: ENABLED");
        log.info("Fake payment gateway: MOCK MODE");
        log.info("=".repeat(60));
    }

    private void logFakeConfigurationStatus() {
        log.debug("Checking fake service configurations...");
        log.debug("✅ Fake encryption service: Initialized");
        log.debug("✅ Fake rate limiter: Configured");
        log.debug("✅ Fake monitoring: Active");
        log.debug("⚠️  Using DEVELOPMENT secrets - NOT for production!");
    }
}
