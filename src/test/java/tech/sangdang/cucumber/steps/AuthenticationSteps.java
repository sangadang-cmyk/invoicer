package tech.sangdang.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import tech.sangdang.cucumber.CucumberStepParent;
import tech.sangdang.cucumber.ScenarioContext;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
public class AuthenticationSteps extends CucumberStepParent {

    @Autowired
    private ScenarioContext context;
    @Autowired
    private MockMvc mockMvc;

    @Given("I am not logged in")
    public void i_am_not_logged_in() {
        context.clearLoggedInSession();
    }

    @Given("I am logged in as {string}")
    public void i_am_logged_in_as(String userRole) {
        String userId = "test-user-" + System.currentTimeMillis();
        context.setLoggedInSession(
                getRoleJwt(userId, userRole.toUpperCase())
        );
    }

    @When("I access a {string}-protected resource")
    public void i_access_a_protected_resource(String protectionLevel) throws Exception {
        var mvcResult = mockMvc.perform(
                ensureAuth(
                        get("/" + protectionLevel.toLowerCase() + "/test"),
                        context.getLoggedInSession()
                )
        ).andReturn();

        context.addApiResult(mvcResult);
    }

    @Then("^I (should|shouldnt) be granted access")
    public void i_be_granted_access(String accessOutcome) {
        log.info("Resultant status: {}", context.getLatestApiResult().getResponse().getStatus());
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
