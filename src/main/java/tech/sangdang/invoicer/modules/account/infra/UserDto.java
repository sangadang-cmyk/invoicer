package tech.sangdang.invoicer.modules.account.infra;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String sub;
    private String email;
}
