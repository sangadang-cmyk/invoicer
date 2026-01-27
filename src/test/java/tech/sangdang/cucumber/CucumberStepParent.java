package tech.sangdang.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;
import tech.sangdang.config.LocalstackConfig;
import tech.sangdang.invoicer.InvoicerApplication;
import tech.sangdang.invoicer.common.constants.AppScopes;
import tech.sangdang.invoicer.common.constants.AppSecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ContextConfiguration(classes = {ScenarioContext.class})
@SpringBootTest(
        classes = {InvoicerApplication.class, LocalstackConfig.class}, 
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.aws.region.static=ap-southeast-1"
        }
)
public class CucumberStepParent {

    @Autowired
    private AppScopes scopes;
    
    public SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getRoleJwt(
            String userId,
            String... roles
    ) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles.length == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + AppSecurity.Role.USER));
        } else {
            Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(builder -> builder
                        .subject(userId)
                )
                .authorities(authorities);
    }
    
    public SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getScopeJwt(
            String sub,
            String... selectedScopes
    ) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (selectedScopes.length == 0) {
            authorities.add(new SimpleGrantedAuthority(scopes.DEFAULT));
        } else {
            Arrays.stream(selectedScopes).forEach(scope -> authorities.add(new SimpleGrantedAuthority(scope)));
        }

        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(builder -> builder
                        .subject(sub)
                )
                .authorities(authorities);
    }
    
    public static MockHttpServletRequestBuilder ensureAuth(
            MockHttpServletRequestBuilder requestBuilder,
            SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor auth
    ) {
        if (auth != null) {
            return requestBuilder.with(auth);
        } else {
            return requestBuilder;
        }
    }
}
