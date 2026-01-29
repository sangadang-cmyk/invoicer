package tech.sangdang.invoicer.modules.invoice.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;

@Mapper(
        componentModel = "spring",
        uses = {},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InvoiceMapper {
    InvoiceResponseDto toResponse(Invoice invoice);

    void mapNonNull(UpdateInvoiceCommand source, @MappingTarget Invoice target);
}
