package tech.sangdang.invoicer.modules.notifications.infra;

import io.awspring.cloud.sns.core.SnsOperations;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import tech.sangdang.invoicer.modules.notifications.domain.ports.EmailNotificationPort;
import tech.sangdang.invoicer.modules.system.SystemConfig;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailNotificationPortImpl implements EmailNotificationPort {
    private final SnsTemplate snsTemplate;
    private final SnsOperations snsOperations;
    private final SnsClient snsClient;
    private final SystemConfig systemConfig;

    @Override
    public void subscribeToEmailNotifications(String email) {
        log.debug("Attempting to subscribe email {} to SNS topic {}", email, systemConfig.getSnsTopicArn());

        String filterPolicy = String.format("{\"recipient_id\": [\"%s\"]}", email);
        
        snsClient.subscribe(builder -> builder
                .protocol("email")
                .endpoint(email)
                .topicArn(systemConfig.getSnsTopicArn())
                .attributes(Map.of("FilterPolicy", filterPolicy))
        );
        
        log.debug("Subscribed email {} to SNS topic {}", email, systemConfig.getSnsTopicArn());
    }

    @Override
    public void sendEmail(String subject, String body) {
        log.debug("Attempting to send email with subject '{}' to SNS topic {}", subject, systemConfig.getSnsTopicArn());
        
        snsTemplate.sendNotification(systemConfig.getSnsTopicArn(), body, subject);
        
        log.debug("Sent email with subject '{}' to SNS topic {}", subject, systemConfig.getSnsTopicArn());
    }
}
