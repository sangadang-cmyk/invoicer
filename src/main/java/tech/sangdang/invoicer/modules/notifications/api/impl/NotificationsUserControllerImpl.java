package tech.sangdang.invoicer.modules.notifications.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.notifications.api.NotificationsUserController;
import tech.sangdang.invoicer.modules.notifications.app.dto.req.SubscribeToEmailNotificationsCommand;
import tech.sangdang.invoicer.modules.notifications.app.service.NotificationManagementService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(NotificationsUserController.PATH)
public class NotificationsUserControllerImpl implements NotificationsUserController {
    private final NotificationManagementService notificationManagementService;

    @Override
    public ResponseEntity<Void> subscribeToEmailNotifications(Jwt principal) {
        notificationManagementService.subscribeToEmailNotifications(SubscribeToEmailNotificationsCommand.builder()
                .userId(principal.getSubject())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
