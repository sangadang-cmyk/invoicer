package tech.sangdang.invoicer.modules.invoice.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class InvoiceCannotBeUpdatedError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public InvoiceCannotBeUpdatedError(String invoiceId) {
        super("Invoice with id=" + invoiceId + " cannot be updated in its current state.");
    }
}
