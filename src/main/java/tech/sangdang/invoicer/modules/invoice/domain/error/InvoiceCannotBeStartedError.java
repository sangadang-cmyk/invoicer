package tech.sangdang.invoicer.modules.invoice.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class InvoiceCannotBeStartedError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return InvoiceCannotBeStartedError.class.getSimpleName().toUpperCase();
    }

    public InvoiceCannotBeStartedError(String id) {
        super("Invoice with id=" + id + " cannot be started in its current state.");
    }
}
