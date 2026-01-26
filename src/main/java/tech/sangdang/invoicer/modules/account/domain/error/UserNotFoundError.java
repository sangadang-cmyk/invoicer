package tech.sangdang.invoicer.modules.account.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class UserNotFoundError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    public UserNotFoundError(String field, String value) {
        super("User not found with " + field + "=" + value);
    }
}
