package tech.sangdang.invoicer.modules.notifications.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "_Test")
public interface NotificationTestController {
    String PATH = "/public/test/notifications";
    
    @PostMapping("/send-email")
    void sendTestEmailNotification(@RequestParam String recipientEmail);
}
