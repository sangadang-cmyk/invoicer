package tech.sangdang.invoicer.modules.notifications.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sangdang.invoicer.modules.account.domain.error.UserNotFoundError;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;
import tech.sangdang.invoicer.modules.notifications.app.dto.req.SubscribeToEmailNotificationsCommand;
import tech.sangdang.invoicer.modules.notifications.app.service.NotificationManagementService;
import tech.sangdang.invoicer.modules.notifications.domain.ports.EmailNotificationPort;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationManagementServiceImpl implements NotificationManagementService {
    private final EmailNotificationPort emailNotificationPort;
    private final AccountQueryPort accountQueryPort;
    
    @Override
    public void subscribeToEmailNotifications(SubscribeToEmailNotificationsCommand command) {
        var userDetails = accountQueryPort.getUserById(command.getUserId())
                .orElseThrow(() -> new UserNotFoundError("ID", command.getUserId()));
        
        emailNotificationPort.subscribeToEmailNotifications(userDetails.getEmail());
    }
}
