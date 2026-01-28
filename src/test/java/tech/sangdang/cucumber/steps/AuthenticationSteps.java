package tech.sangdang.cucumber.steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import tech.sangdang.cucumber.CucumberStepParent;
import tech.sangdang.cucumber.ScenarioContext;
import tech.sangdang.invoicer.common.constants.AppScopes;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
public class AuthenticationSteps extends CucumberStepParent {

    @Autowired
    private ScenarioContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppScopes scopes;
    @Autowired
    private AccountQueryPort accountQueryPort;

    @Given("I am not logged in")
    public void i_am_not_logged_in() {
        context.clearLoggedInSession();
    }

    @Given("I am logged in as mock {string}")
    public void i_am_logged_in_as_mock(String userRole) {
        context.setLoggedInSession(
                getRoleJwt("temp-user-id", userRole.toUpperCase())
        );
    }
    
    @Given("I am logged in as role USER with the user ID I have")
    public void i_am_logged_in_as() {
        String userId = context.getData("userId");
        context.setLoggedInSession(
                getRoleJwt(userId, AppSecurity.Role.USER)
        );
    }
    
    @Given("I have valid client credentials")
    public void i_have_valid_client_credentials() {
        String systemId = "test-system-" + System.currentTimeMillis();
        context.setLoggedInSession(
                getScopeJwt(systemId, scopes.DEFAULT, scopes.INVOICE_WRITE_OWNED, scopes.INVOICE_READ_OWNED)
        );
    }
    
    @Given("I have a valid user ID")
    public void i_have_a_valid_user_id() {
        var accounts = accountQueryPort.listUsers(AppSecurity.Role.USER);
        String userId = accounts.getFirst().getSub();
        context.putData("userId", userId);
    }

    @And("I have an invalid user ID")
    public void iHaveAnInvalidUserID() {
        context.putData("userId", UUID.randomUUID().toString());
    }

    @When("I access a {string}-protected resource")
    public void i_access_a_protected_resource(String protectionLevel) throws Exception {
        var mvcResult = mockMvc.perform(
                ensureAuth(
                        get("/api/" + protectionLevel.toLowerCase() + "/test"),
                        context.getLoggedInSession()
                )
        ).andReturn();

        context.addApiResult(mvcResult);
    }

    @Then("^I (should|shouldnt) be granted access")
    public void i_be_granted_access(String accessOutcome) throws Exception {
        if (accessOutcome.equalsIgnoreCase("should")) {
            assertEquals("Expect 200", 200, context.getLatestApiResult().getResponse().getStatus());
            return;
        }
        if (accessOutcome.equalsIgnoreCase("shouldnt")) {
            assertEquals("Expect 401 or 403", true,
                    (context.getLatestApiResult().getResponse().getStatus() == 401)
                            || (context.getLatestApiResult().getResponse().getStatus() == 403));
            return;
        }
        log.warn("Unknown access outcome: {}", accessOutcome);
    }
}
