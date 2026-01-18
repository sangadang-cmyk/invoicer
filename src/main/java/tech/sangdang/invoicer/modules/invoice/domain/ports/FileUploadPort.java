package tech.sangdang.invoicer.modules.invoice.domain.ports;

import tech.sangdang.invoicer.modules.invoice.domain.Invoice;

public interface FileUploadPort {
    String uploadFile(Invoice invoice);
}
