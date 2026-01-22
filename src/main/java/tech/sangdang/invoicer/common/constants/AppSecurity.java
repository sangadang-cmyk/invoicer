package tech.sangdang.invoicer.common.constants;

public class AppSecurity {
    public static final String OAUTH2 = "OAuth2";
    
    public static class Role {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }
    
    public static class Scope {
        public static final String DEFAULT = "SCOPE_invoicer-api/default";
        public static final String READ_ANY = "SCOPE_invoicer-api/read-any";
        public static final String CREATE = "SCOPE_invoicer-api/create";
        public static final String UPDATE = "SCOPE_invoicer-api/update";
        public static final String DELETE = "SCOPE_invoicer-api/delete";
    }
}
