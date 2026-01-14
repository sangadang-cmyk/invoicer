package tech.sangdang.invoicer.modules.invoice.domain;

public enum InvoiceStatus {
    AWAITING_UPLOAD,
    PENDING_VALIDATION,
    VALIDATED,
    REJECTED
}
