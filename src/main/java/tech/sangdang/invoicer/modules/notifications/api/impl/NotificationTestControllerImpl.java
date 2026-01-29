package tech.sangdang.invoicer.modules.notifications.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.notifications.api.NotificationTestController;
import tech.sangdang.invoicer.modules.notifications.domain.ports.EmailNotificationPort;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(NotificationTestController.PATH)
public class NotificationTestControllerImpl implements NotificationTestController {
    private final EmailNotificationPort emailNotificationPort;
    
    @Override
    public void sendTestEmailNotification() {
        emailNotificationPort.sendEmail("Test Email", "This is a test email from Invoicer application.");
    }
}
