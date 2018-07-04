package wrongsides.cherrypickor.cucumber;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import wrongsides.cherrypickor.CherrypickorApplication;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features"}, glue = {"wrongsides.cherrypickor.cucumber"})
public class CucumberRunner {

    @BeforeClass
    public static void cucumberSetup() {
        CherrypickorApplication.main();
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
    }
}
