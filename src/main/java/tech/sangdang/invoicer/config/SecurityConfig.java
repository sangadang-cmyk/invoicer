package tech.sangdang.invoicer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import tech.sangdang.invoicer.common.constants.AppSecurity;

@EnableMethodSecurity(proxyTargetClass = true)
@Configuration
public class SecurityConfig {

    public static final String[] DEV_URLS = {
            "/swagger-ui/**",
            "/api/v3/api-docs/**",
            "/actuator/**",
    };

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(DEV_URLS).permitAll()
                        .requestMatchers("/api/public/**", "/api/system/**").permitAll()
                        .requestMatchers("/api/internal/**").hasAuthority(AppSecurity.Scope.DEFAULT)
                        .requestMatchers("/api/admin/**").hasRole(AppSecurity.Role.ADMIN)
                        .requestMatchers("/api/user/**").hasRole(AppSecurity.Role.USER)
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
                ))
        ;
        return http.build();
    }
    
    @Bean
    RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(AppSecurity.Role.ADMIN).implies(AppSecurity.Role.USER)
                .build();
    }
}
