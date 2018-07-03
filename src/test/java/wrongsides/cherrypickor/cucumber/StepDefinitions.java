package wrongsides.cherrypickor.cucumber;

import cucumber.api.java8.En;
import org.apache.commons.io.FileUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.LocalConfig;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.collections.Asteroids;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions implements En {

    private Traverson traverson;
    private RestTemplate restTemplate;
    private ResponseEntity<Asteroids> asteroidsResponse;
    private ResponseEntity<NamedResource> refreshResponse;

    public StepDefinitions() throws URISyntaxException {

        this.traverson = new Traverson(new URI(new LocalConfig().getApiRoot()), MediaTypes.HAL_JSON);
        this.restTemplate = new RestTemplate();

        Given("^Cherrypickor is running$", () -> {
            assertThat(traverson.follow("self").toEntity(String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
        });

        And("^ESI services are available$", () -> {
            stubForMarkets();
            stubForSearches();
            stubForTypes();
            stubForGroups();
            stubForCategory();
        });

        When("^I post '(.*)' to the asteroids endpoint$", (filename) -> {
            String body = FileUtils.readFileToString(new File("src/test/resources/" + filename), StandardCharsets.UTF_8);
            HttpEntity<String> request = new HttpEntity<>(body);

            Link asteroidsUri = traverson.follow("asteroids").asLink();
            asteroidsResponse = restTemplate.postForEntity(asteroidsUri.getHref(), request, Asteroids.class);
            assertThat(asteroidsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        });

        Then("^the result should be ordered highest value first$", () -> {
            List<Asteroid> asteroids = asteroidsResponse.getBody().getAsteroids();
            List<BigDecimal> values = asteroids.stream().map(Asteroid::getValue).collect(Collectors.toList());

            assertThat(values).isNotEmpty();
            assertThat(values).isSortedAccordingTo(Comparator.reverseOrder());
        });

        When("^I post to the refresh endpoint$", () -> {
            HttpEntity<String> request = new HttpEntity<>("");

            Link refreshUri = traverson.follow("refresh").asLink();
            refreshResponse = restTemplate.postForEntity(refreshUri.getHref(), request, NamedResource.class);
            assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        });

        Then("^the asteroid types are requested from ESI$", () -> {
            verify(1, getRequestedFor(urlEqualTo("/latest/search/?datasource=tranquility&categories=inventory_type&search=Dark%20Ochre&strict=true")));
            verify(1, getRequestedFor(urlEqualTo("/latest/universe/types/17436")));
            verify(1, getRequestedFor(urlEqualTo("/latest/universe/types/22")));
            verify(1, getRequestedFor(urlEqualTo("/latest/universe/types/17425")));
        });
    }

    private void stubForMarkets() {
        stubFor(get("/latest/markets/10000002/orders/?datasource=tranquility&order_type=buy&type_id=17466")
                .willReturn(okJson("[{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-24T18:55:25Z\",\"location_id\":60004240,\"min_volume\":1,\"order_id\":5144382023,\"price\":2256.0,\"range\":\"region\",\"system_id\":30000128,\"type_id\":17466,\"volume_remain\":41838,\"volume_total\":42000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-03-03T06:08:15Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5102339341,\"price\":0.01,\"range\":\"10\",\"system_id\":30000142,\"type_id\":17466,\"volume_remain\":1000000,\"volume_total\":1000000},{\"duration\":30,\"is_buy_order\":true,\"issued\":\"2018-04-17T15:45:38Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5129544024,\"price\":3050.68,\"range\":\"1\",\"system_id\":30000142,\"type_id\":17466,\"volume_remain\":24326,\"volume_total\":30000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-05-01T21:55:18Z\",\"location_id\":1023164547009,\"min_volume\":1,\"order_id\":5049308512,\"price\":3050.7,\"range\":\"1\",\"system_id\":30000144,\"type_id\":17466,\"volume_remain\":70111,\"volume_total\":100000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-05-02T06:02:18Z\",\"location_id\":1025949280066,\"min_volume\":100,\"order_id\":5131189070,\"price\":3050.71,\"range\":\"20\",\"system_id\":30000144,\"type_id\":17466,\"volume_remain\":88407,\"volume_total\":100000}]")));

        stubFor(get("/latest/markets/10000002/orders/?datasource=tranquility&order_type=buy&type_id=17432")
                .willReturn(okJson("[{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-24T12:29:57Z\",\"location_id\":60004240,\"min_volume\":1,\"order_id\":5144139716,\"price\":1927.0,\"range\":\"region\",\"system_id\":30000128,\"type_id\":17432,\"volume_remain\":50000,\"volume_total\":50000},{\"duration\":30,\"is_buy_order\":true,\"issued\":\"2018-04-07T15:20:12Z\",\"location_id\":60003760,\"min_volume\":1,\"order_id\":5127497244,\"price\":2483.21,\"range\":\"1\",\"system_id\":30000142,\"type_id\":17432,\"volume_remain\":21805,\"volume_total\":35000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-07T08:31:40Z\",\"location_id\":1023164547009,\"min_volume\":1,\"order_id\":4801695195,\"price\":2483.2,\"range\":\"1\",\"system_id\":30000144,\"type_id\":17432,\"volume_remain\":74192,\"volume_total\":100000},{\"duration\":90,\"is_buy_order\":true,\"issued\":\"2018-04-17T18:23:03Z\",\"location_id\":1023075604524,\"min_volume\":100,\"order_id\":5138782414,\"price\":2483.22,\"range\":\"20\",\"system_id\":30000144,\"type_id\":17432,\"volume_remain\":98324,\"volume_total\":100000}]")));
    }

    private void stubForSearches() throws IOException {
        stubFor(get("/latest/search/?datasource=tranquility&categories=region&search=The%20Forge&strict=true")
                .willReturn(okJson("{\"region\":[10000002]}")));

        stubFor(get("/latest/search/?datasource=tranquility&categories=inventory_type&search=Bright%20Spodumain&strict=true")
                .willReturn(okJson("{\"inventory_type\":[17466]}")));

        stubFor(get("/latest/search/?datasource=tranquility&categories=inventory_type&search=Sharp%20Crokite&strict=true")
                .willReturn(okJson("{\"inventory_type\":[17432]}")));

        String body = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/DarkOchreSearchResponse.json"), StandardCharsets.UTF_8);
        stubFor(get("/latest/search/?datasource=tranquility&categories=inventory_type&search=Dark%20Ochre&strict=true")
                .willReturn(okJson(body)));
    }

    private void stubForTypes() throws IOException {
        String darkOchreTypeResponse = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/DarkOchreTypeResponse.json"), StandardCharsets.UTF_8);
        String onyxOchreTypeResponse = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/OnyxOchreTypeResponse.json"), StandardCharsets.UTF_8);
        String arkonorTypeResponse = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/ArkonorTypeResponse.json"), StandardCharsets.UTF_8);
        String crimsonArkonorTypeResponse = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/CrimsonArkonorTypeResponse.json"), StandardCharsets.UTF_8);

        stubFor(get("/latest/universe/types/1232").willReturn(okJson(darkOchreTypeResponse)));
        stubFor(get("/latest/universe/types/17436").willReturn(okJson(onyxOchreTypeResponse)));
        stubFor(get("/latest/universe/types/22").willReturn(okJson(arkonorTypeResponse)));
        stubFor(get("/latest/universe/types/17425").willReturn(okJson(crimsonArkonorTypeResponse)));
    }

    private void stubForGroups() throws IOException {
        String group453 = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/DarkOchreGroupResponse.json"), StandardCharsets.UTF_8);
        stubFor(get("/latest/universe/groups/453").willReturn(okJson(group453)));

        String group454 = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/ArkonorGroupResponse.json"), StandardCharsets.UTF_8);
        stubFor(get("/latest/universe/groups/454").willReturn(okJson(group454)));
    }

    private void stubForCategory() throws IOException {
        String body = FileUtils.readFileToString(new File("src/test/resources/EsiResponses/AsteroidCategoryResponse.json"), StandardCharsets.UTF_8);
        stubFor(get("/latest/universe/categories/25").willReturn(okJson(body)));
    }
}
