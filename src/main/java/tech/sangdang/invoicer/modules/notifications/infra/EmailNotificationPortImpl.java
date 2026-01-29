package tech.sangdang.invoicer.modules.notifications.infra;

import io.awspring.cloud.sns.core.SnsOperations;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
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
        try {
            log.debug("Attempting to subscribe email {} to SNS topic {}", email, systemConfig.getSnsTopicArn());

            String filterPolicy = String.format("{\"recipient_id\": [\"%s\"]}", email);

            snsClient.subscribe(builder -> builder
                    .protocol("email")
                    .endpoint(email)
                    .topicArn(systemConfig.getSnsTopicArn())
                    .attributes(Map.of("FilterPolicy", filterPolicy))
            );

            log.debug("Subscribed email {} to SNS topic {}", email, systemConfig.getSnsTopicArn());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void sendEmail(String subject, String body, String recipientEmail) {
        try {
            log.debug("Attempting to send email with subject '{}' to SNS topic {}", subject, systemConfig.getSnsTopicArn());

            var message = MessageBuilder
                    .withPayload(body)
                    .setHeader("Subject", subject)
                    .setHeader("recipient_id", recipientEmail)
                    .build();
            snsTemplate.send(systemConfig.getSnsTopicArn(), message);

            log.debug("Sent email with subject '{}' to SNS topic {}", subject, systemConfig.getSnsTopicArn());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
