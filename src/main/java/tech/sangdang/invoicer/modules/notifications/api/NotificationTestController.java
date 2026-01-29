package tech.sangdang.invoicer.modules.notifications.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "_Tests")
public interface NotificationTestController {
    String PATH = "/public/test/notifications";
    
    @PostMapping("/send-email")
    void sendTestEmailNotification();
}
