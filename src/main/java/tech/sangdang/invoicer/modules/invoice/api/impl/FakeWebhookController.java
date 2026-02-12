package tech.sangdang.invoicer.modules.invoice.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sangdang.invoicer.modules.invoice.app.service.FakePaymentService;

import java.util.HashMap;
import java.util.Map;

/**
 * Fake webhook controller for simulating payment gateway callbacks
 */
@Slf4j
@RestController
@RequestMapping("/api/fake/webhooks")
@RequiredArgsConstructor
public class FakeWebhookController {

    private final FakePaymentService fakePaymentService;

    @PostMapping("/payment/success")
    public ResponseEntity<Map<String, String>> handleFakePaymentSuccess(
            @RequestHeader("X-Fake-Signature") String signature,
            @RequestBody Map<String, Object> payload) {
        
        log.info("Received fake payment success webhook with signature: {}", 
                signature.substring(0, 12) + "...");
        
        try {
            // Fake webhook validation
            boolean isValid = fakePaymentService.validateFakeWebhook(signature, payload.toString());
            
            if (!isValid) {
                log.warn("Invalid fake webhook signature");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid signature", "status", "failed"));
            }
            
            String paymentId = (String) payload.get("payment_id");
            String invoiceId = (String) payload.get("invoice_id");
            
            log.info("Processing fake payment success for invoice: {}, payment: {}", 
                    invoiceId, paymentId);
            
            // Simulate processing delay
            Thread.sleep(50);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "processed");
            response.put("payment_id", paymentId);
            response.put("invoice_id", invoiceId);
            response.put("fake_confirmation", "payment_confirmed_" + System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing fake payment webhook", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Processing failed", "status", "error"));
        }
    }

    @PostMapping("/payment/failed")
    public ResponseEntity<Map<String, String>> handleFakePaymentFailure(
            @RequestHeader("X-Fake-Signature") String signature,
            @RequestBody Map<String, Object> payload) {
        
        log.warn("Received fake payment failure webhook");
        
        String paymentId = (String) payload.get("payment_id");
        String errorCode = (String) payload.getOrDefault("error_code", "UNKNOWN");
        String errorMessage = (String) payload.getOrDefault("error_message", "Payment failed");
        
        log.error("Fake payment failed - ID: {}, Error: {} - {}", paymentId, errorCode, errorMessage);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "failed");
        response.put("payment_id", paymentId);
        response.put("error_code", errorCode);
        response.put("fake_failure_id", "fail_" + System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notification/sent")
    public ResponseEntity<Map<String, String>> handleFakeNotificationWebhook(
            @RequestBody Map<String, Object> payload) {
        
        log.debug("Received fake notification webhook");
        
        String notificationId = (String) payload.get("notification_id");
        String status = (String) payload.get("status");
        String recipient = (String) payload.get("recipient");
        
        log.info("Fake notification {} sent to {} with status: {}", 
                notificationId, recipient, status);
        
        return ResponseEntity.ok(Map.of(
                "status", "acknowledged",
                "notification_id", notificationId,
                "fake_ack_time", String.valueOf(System.currentTimeMillis())
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getFakeWebhookStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("fake_webhooks_received_today", 42);
        status.put("fake_payment_success_rate", 0.95);
        status.put("fake_notification_delivery_rate", 0.98);
        status.put("last_webhook_received", System.currentTimeMillis() - 300000); // 5 min ago
        status.put("webhook_endpoint_health", "healthy");
        
        return ResponseEntity.ok(status);
    }
}
