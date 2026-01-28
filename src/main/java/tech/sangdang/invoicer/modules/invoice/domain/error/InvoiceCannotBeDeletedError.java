package tech.sangdang.invoicer.modules.invoice.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class InvoiceCannotBeDeletedError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return InvoiceCannotBeDeletedError.class.getSimpleName().toUpperCase();
    }

    public InvoiceCannotBeDeletedError(String id) {
        super("Invoice with id=" + id + " cannot be deleted in its current state.");
    }
}
