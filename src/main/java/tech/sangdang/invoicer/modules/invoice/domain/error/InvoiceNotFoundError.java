package tech.sangdang.invoicer.modules.invoice.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class InvoiceNotFoundError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return InvoiceNotFoundError.class.getSimpleName().toUpperCase();
    }

    public InvoiceNotFoundError(String field, String value) {
        super("Invoice not found with " + field + "=" + value);
    }
}
