package tech.sangdang.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.sangdang.cucumber.CucumberSpringParent;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AuthenticationSteps extends CucumberSpringParent {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult result;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor auth;
    
    @Given("I am an {string} user with role {string}")
    public void i_am_an_authenticated_user_with_role(String authentication, String role) {
        if(Objects.equals(authentication, "unauthenticated")) {
            auth = null;
            return;
        }
        
        var userId = UUID.randomUUID().toString();
        var email = role.toLowerCase() + "@gmail.com";
        auth = generateRoleJwt(userId, email, role);
    }
    
    @When("I attempt to access a {string} resource")
    public void i_attempt_to_access_a_resource_of_type(String resourceType) throws Exception {
        String url = switch (resourceType.toLowerCase()) {
            case "private" -> "/api/private/test";
            case "public" -> "/api/public/test";
            case "user" -> "/api/user/test";
            case "admin" -> "/api/admin/test";
            default -> throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        };
        result = mockMvc.perform(ensureAuth(get(url), auth))
                .andReturn();
    }
    
    @Then("I should receive a {string} response")
    public void i_should_receive_a_response(String responseType) throws Exception {
        int expectedStatus = switch (responseType.toLowerCase()) {
            case "success" -> HttpStatus.OK.value();
            case "forbidden" -> HttpStatus.FORBIDDEN.value();
            case "unauthorized" -> HttpStatus.UNAUTHORIZED.value();
            default -> throw new IllegalArgumentException("Unknown response type: " + responseType);
        };
        assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
    }

}
