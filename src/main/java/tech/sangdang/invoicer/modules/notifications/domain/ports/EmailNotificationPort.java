package tech.sangdang.invoicer.modules.notifications.domain.ports;

public interface EmailNotificationPort {
    void subscribeToEmailNotifications(String email);
    
    void sendEmail(String subject, String body, String recipientEmail);
}
