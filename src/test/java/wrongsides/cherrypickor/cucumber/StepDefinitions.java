package wrongsides.cherrypickor.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java8.En;

public class StepDefinitions implements En {

    public StepDefinitions() {

        When("^I post a list of asteroids$", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });

        Then("^the result should be ordered highest value first$", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });

    }
}
