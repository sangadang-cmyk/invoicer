package tech.sangdang.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;
import tech.sangdang.config.LocalStackTestConfig;
import tech.sangdang.invoicer.InvoicerApplication;
import tech.sangdang.invoicer.common.constants.AppSecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Testcontainers(disabledWithoutDocker = true)
@Import(LocalStackTestConfig.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ContextConfiguration(classes = {ScenarioContext.class})
// because ScenarioContext is located in the test package instead of the main package
@SpringBootTest(classes = InvoicerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberStepParent {
    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getRoleJwt(
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

//    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor generateRoleJwt(
//            String userId,
//            String email,
//            String... roles
//    ) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if (roles.length == 0) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + AppSecurity.Role.USER));
//        } else {
//            Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
//        }
//
//        return SecurityMockMvcRequestPostProcessors.jwt()
//                .jwt(builder -> builder
//                        .subject(userId)
//                        .claim("custom:email", email)
//                )
//                .authorities(authorities);
//    }
//
//    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor generateScopeJwt(
//            String userId,
//            String email,
//            String... scopes
//    ) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if (scopes.length == 0) {
//            authorities.add(new SimpleGrantedAuthority(AppSecurity.Scope.DEFAULT));
//        } else {
//            Arrays.stream(scopes).forEach(scope -> authorities.add(new SimpleGrantedAuthority(scope)));
//        }
//
//        return SecurityMockMvcRequestPostProcessors.jwt()
//                .jwt(builder -> builder
//                        .subject(userId)
//                        .claim("custom:email", email)
//                )
//                .authorities(authorities);
//    }
//
//    public static MockHttpServletRequestBuilder ensureAuth(
//            MockHttpServletRequestBuilder requestBuilder,
//            SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor auth
//    ) {
//        if (auth != null) {
//            return requestBuilder.with(auth);
//        } else {
//            return requestBuilder;
//        }
//    }
//
//    public static MockHttpServletRequestBuilder ensureAuth(
//            MockHttpServletRequestBuilder requestBuilder
//    ) {
//        return ensureAuth(requestBuilder, null);
//    }
}
