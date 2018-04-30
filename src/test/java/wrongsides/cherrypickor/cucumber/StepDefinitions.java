package wrongsides.cherrypickor.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java8.En;
import org.apache.commons.io.FileUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.CherrypickorApplication;
import wrongsides.cherrypickor.config.environment.LocalConfig;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.collections.Asteroids;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions implements En {

    private Traverson traverson;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ResponseEntity<Asteroids> asteroidsResponse;

    public StepDefinitions() throws URISyntaxException {

        CherrypickorApplication.main(new String[]{});

        this.traverson = new Traverson(new URI(new LocalConfig().getApplicationRoot()), MediaTypes.HAL_JSON);
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        When("^I post a list of asteroids$", () -> {
            String body = FileUtils.readFileToString(new File("src/test/resources/AsteroidsRequestBody.json"), StandardCharsets.UTF_8);
            Asteroids asteroidsBody = objectMapper.readValue(body, Asteroids.class);
            HttpEntity<Asteroids> request = new HttpEntity<>(asteroidsBody);

            Link asteroidsUri = traverson.follow("asteroids").asLink();
            asteroidsResponse = restTemplate.postForEntity(asteroidsUri.getHref(), request, Asteroids.class);
            assertThat(asteroidsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        });

        Then("^the result should be ordered highest value first$", () -> {
            List<Asteroid> asteroids = asteroidsResponse.getBody().getAsteroids();
            List<BigDecimal> values = asteroids.stream().map(Asteroid::getValue).collect(Collectors.toList());

            assertThat(values).isSortedAccordingTo(Comparator.reverseOrder());
        });

    }
}
