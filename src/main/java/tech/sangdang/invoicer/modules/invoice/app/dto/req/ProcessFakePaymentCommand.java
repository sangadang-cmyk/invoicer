package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Fake payment processing command DTO for development testing
 */
@Data
@NoArgsConstructor
public class ProcessFakePaymentCommand {
    private String invoiceId;
    private BigDecimal amount;
    private String currency;
    private String fakePaymentMethod;
    private String fakeCustomerId;
}
