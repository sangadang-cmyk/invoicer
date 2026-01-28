package tech.sangdang.invoicer.modules.invoice.domain.error;

import org.springframework.http.HttpStatus;
import tech.sangdang.invoicer.common.core.BusinessError;

public class InvoiceAccessDeniedError extends BusinessError {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getErrorCode() {
        return InvoiceAccessDeniedError.class.getSimpleName().toUpperCase();
    }

    public InvoiceAccessDeniedError() {
        super("Access to the requested invoice is denied.");
    }
}
