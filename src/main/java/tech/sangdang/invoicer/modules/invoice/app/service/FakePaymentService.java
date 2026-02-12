package tech.sangdang.invoicer.modules.invoice.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.ProcessFakePaymentCommand;

/**
 * Fake payment processing service for development and testing.
 * This simulates payment gateway interactions without real transactions.
 */
@Slf4j
@Service
public class FakePaymentService {

    @Value("${system.config.integrations.payment-gateway.api-key:pk_test_fake_default}")
    private String fakeApiKey;

    @Value("${system.config.integrations.payment-gateway.webhook-secret:whsec_fake_default}")
    private String fakeWebhookSecret;

    private static final String ACCESS_CODE = "DEVELOPMENT_12D12";

    public void processFakePayment(ProcessFakePaymentCommand command) {
        log.info("Processing fake payment with API key: {}...", 
                fakeApiKey.substring(0, Math.min(8, fakeApiKey.length())));
        
        // Simulate payment processing delay
        try {
            Thread.sleep(100); // 100ms fake processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Fake payment validation
        if (command.getAmount() != null && command.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            log.debug("Fake payment processed successfully: ${}", command.getAmount());
            
            // Simulate webhook verification
            if (fakeWebhookSecret.startsWith("whsec_")) {
                log.debug("Fake webhook signature verified");
            }
        } else {
            log.warn("Fake payment failed: Invalid amount");
            throw new RuntimeException("Fake payment validation failed");
        }
        
        log.info("Fake payment processing completed");
    }

    public String generateFakePaymentToken() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "tok_fake_" + timestamp.substring(timestamp.length() - 8);
    }

    public boolean validateFakeWebhook(String signature, String payload) {
        // Simple fake webhook validation
        return signature.startsWith("sha256=fake_") && !payload.isEmpty();
    }
}
