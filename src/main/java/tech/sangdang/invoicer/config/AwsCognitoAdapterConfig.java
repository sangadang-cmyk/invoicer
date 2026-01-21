package tech.sangdang.invoicer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class AwsCognitoAdapterConfig {
    private static final String COGNITO_GROUPS_CLAIM = "cognito:groups"; // type array

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // assign a converter from jwt to authorities (how do I get authorities from my jwt?)
        converter.setJwtGrantedAuthoritiesConverter((jwt) -> {
            @SuppressWarnings("unchecked") List<String> rawAuthorities = (List<String>) jwt.getClaims().getOrDefault(COGNITO_GROUPS_CLAIM, Collections.<String>emptyList());

            log.debug("Raw Authorities from JWT: {}", rawAuthorities);
            List<GrantedAuthority> authorities = new ArrayList<>();
            rawAuthorities
                    .stream()
                    .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                    .forEach(authorities::add);

            log.debug("Mapped Authorities: {}", authorities);
            return authorities;    
        });
        
        return converter;
    }
}
