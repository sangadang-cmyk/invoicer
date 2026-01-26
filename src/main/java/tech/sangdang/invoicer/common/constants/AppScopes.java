package tech.sangdang.invoicer.common.constants;

import org.springframework.stereotype.Component;

@Component("S")
public class AppScopes {
    public final String DEFAULT = "SCOPE_invoicer-api/default";
    public final String INVOICE_READ_OWNED = "SCOPE_invoicer-api/invoice:read:owned";
    public final String INVOICE_WRITE_OWNED = "SCOPE_invoicer-api/invoice:write:owned";
    public final String INVOICE_CREATE = "SCOPE_invoicer-api/invoice:create";
    public final String INVOICE_UPDATE_OWNED = "SCOPE_invoicer-api/invoice:update:owned";
    public final String INVOICE_DELETE_OWNED = "SCOPE_invoicer-api/invoice:delete:owned";
    
    public String withoutPrefix(String scope) {
        return scope.replace("SCOPE_", "");
    }
}
