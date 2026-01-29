package tech.sangdang.invoicer.modules.notifications.app.service;

import tech.sangdang.invoicer.modules.notifications.app.dto.req.SubscribeToEmailNotificationsCommand;

public interface NotificationManagementService {
    void subscribeToEmailNotifications(SubscribeToEmailNotificationsCommand command);
}
