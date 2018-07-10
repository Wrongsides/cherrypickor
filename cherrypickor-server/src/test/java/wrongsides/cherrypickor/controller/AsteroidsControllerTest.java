package wrongsides.cherrypickor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.ResourceSupport;
import wrongsides.cherrypickor.controller.resource.AsteroidsResource;
import wrongsides.cherrypickor.controller.resource.NamedResource;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.service.AsteroidsService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsteroidsControllerTest {

    private AsteroidsController asteroidsController;

    @Mock
    private AsteroidsService asteroidsService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private List<Asteroid> asteroids;

    @Before
    public void setUp() throws Exception {
        asteroidsController = new AsteroidsController(asteroidsService, objectMapper);
    }

    @Test
    public void get_returnsNamedResource() {
        NamedResource namedResource = asteroidsController.get();

        assertThat(namedResource.getName()).isEqualTo("asteroids");
        assertThat(namedResource.getMessage()).isEqualTo("POST scanner output or JSON asteroids for appraisal");
        verifyLinks(namedResource);
    }

    @Test
    public void post_givenAsteroids_callsMapAndSortOnAsteroids() throws IOException, ParseException {
        String body = "{ asteroids.json }";
        AsteroidsResource asteroidsResource = new AsteroidsResource();
        asteroidsResource.setAsteroids(asteroids);
        when(objectMapper.readValue(anyString(), eq(AsteroidsResource.class))).thenReturn(asteroidsResource);

        AsteroidsResource response = asteroidsController.post(body);

        verify(objectMapper).readValue(body, AsteroidsResource.class);
        verify(asteroidsService).sortByValue(asteroids);
        assertThat(response.getAsteroids()).isEqualTo(asteroids);
        verifyLinks(response);
    }

    @Test
    public void post_givenAsteroids_throwsMappingException() throws IOException {
        String body = "{ asteroids.json }";
        when(objectMapper.readValue(anyString(), eq(AsteroidsResource.class))).thenThrow(new IOException());

        assertThatIOException().isThrownBy(() -> asteroidsController.post(body));
    }

    @Test
    public void post_givenScannerOutput_callsParseAndSortOnAsteroids() throws IOException, ParseException {
        String body = "Survey scanner output string";
        when(asteroidsService.parseScannerOutput(anyString())).thenReturn(asteroids);

        AsteroidsResource reposnse = asteroidsController.post(body);

        verify(asteroidsService).parseScannerOutput(body);
        verify(asteroidsService).sortByValue(asteroids);
        assertThat(reposnse.getAsteroids()).isEqualTo(asteroids);
        verifyLinks(reposnse);
    }

    @Test
    public void post_givenNull_doesNotCallServiceAndReturnsEmptyAsteroids() throws IOException, ParseException {
        AsteroidsResource response = asteroidsController.post(null);

        verifyZeroInteractions(asteroidsService);
        assertThat(response.getAsteroids()).isEmpty();
        verifyLinks(response);
    }

    private void verifyLinks(ResourceSupport resource) {
        assertThat(resource.getLinks()).extracting("rel", "href")
                .containsExactly(Tuple.tuple("self", "/api/asteroids"),
                        Tuple.tuple("refresh", "/api/refresh"),
                        Tuple.tuple("root", "/api"));
    }
}