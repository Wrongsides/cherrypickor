package wrongsides.cherrypickor.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions implements En {

    private Traverson traverson;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private ResponseEntity<Asteroids> asteroidsResponse;

    public StepDefinitions() throws URISyntaxException {

        CherrypickorApplication.main(new String[]{});
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig()); //No-args constructor will start on port 8080, no HTTPS
        wireMockServer.start();
        stubForRegion();
        stubForSpod();
        stubForSpodBuyOrder();
        stubForCrok();
        stubForCrokBuyOrder();

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

    private void stubForRegion() {
        stubFor(get("/latest/search/?datasource=tranquility&categories=region&search=The%20Forge&strict=true")
                .willReturn(okJson("{\"region\":[10000002]}")));
    }

    private void stubForSpod() {
        stubFor(get("/latest/search/?datasource=tranquility&categories=inventory_type&search=Bright%20Spodumain&strict=true")
                .willReturn(okJson("{\"inventory_type\":[17466]}")));
    }

    private void stubForSpodBuyOrder() {
        stubFor(get("/latest/markets/10000002/orders/?datasource=tranquility&order_type=buy&type_id=17466")
                .willReturn(okJson("[{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-24T18:55:25Z\",\"location_id\":60004240,\"min_volume\":1,\"order_id\":5144382023,\"price\":2256.0,\"range\":\"region\",\"system_id\":30000128,\"type_id\":17466,\"volume_remain\":41838,\"volume_total\":42000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-03-03T06:08:15Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5102339341,\"price\":0.01,\"range\":\"10\",\"system_id\":30000142,\"type_id\":17466,\"volume_remain\":1000000,\"volume_total\":1000000},{\"duration\":30,\"is_buy_order\":true,\"issued\":\"2018-04-17T15:45:38Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5129544024,\"price\":3050.68,\"range\":\"1\",\"system_id\":30000142,\"type_id\":17466,\"volume_remain\":24326,\"volume_total\":30000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-05-01T21:55:18Z\",\"location_id\":1023164547009,\"min_volume\":1,\"order_id\":5049308512,\"price\":3050.7,\"range\":\"1\",\"system_id\":30000144,\"type_id\":17466,\"volume_remain\":70111,\"volume_total\":100000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-05-02T06:02:18Z\",\"location_id\":1025949280066,\"min_volume\":100,\"order_id\":5131189070,\"price\":3050.71,\"range\":\"20\",\"system_id\":30000144,\"type_id\":17466,\"volume_remain\":88407,\"volume_total\":100000}]")));
    }

    private void stubForCrok() {
        stubFor(get("/latest/search/?datasource=tranquility&categories=inventory_type&search=Sharp%20Crokite&strict=true")
                .willReturn(okJson("{\"inventory_type\":[17432]}")));
    }

    private void stubForCrokBuyOrder() {
        stubFor(get("/latest/markets/10000002/orders/?datasource=tranquility&order_type=buy&type_id=17432")
                .willReturn(okJson("[{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-24T12:29:57Z\",\"location_id\":60004240,\"min_volume\":1,\"order_id\":5144139716,\"price\":1927.0,\"range\":\"region\",\"system_id\":30000128,\"type_id\":17432,\"volume_remain\":50000,\"volume_total\":50000},{\"duration\":30,\"is_buy_order\":true,\"issued\":\"2018-04-07T15:20:12Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5127497244,\"price\":2483.21,\"range\":\"1\",\"system_id\":30000142,\"type_id\":17432,\"volume_remain\":21805,\"volume_total\":35000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-07T08:31:40Z\",\"location_id\":1023164547009,\"min_volume\":1,\"order_id\":4801695195,\"price\":2483.2,\"range\":\"1\",\"system_id\":30000144,\"type_id\":17432,\"volume_remain\":74192,\"volume_total\":100000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-17T18:23:03Z\",\"location_id\":1023075604524,\"min_volume\":100,\"order_id\":5138782414,\"price\":2483.22,\"range\":\"20\",\"system_id\":30000144,\"type_id\":17432,\"volume_remain\":98324,\"volume_total\":100000}]")));
    }
}
