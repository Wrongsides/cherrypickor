package wrongsides.cherrypickor.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features"}, glue = {"wrongsides.cherrypickor.cucumber"})
public class CucumberRunner {

}
