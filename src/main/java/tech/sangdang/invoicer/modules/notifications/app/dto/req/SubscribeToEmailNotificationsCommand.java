package tech.sangdang.invoicer.modules.notifications.app.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SubscribeToEmailNotificationsCommand {
    @NotBlank
    private String userId;
}
