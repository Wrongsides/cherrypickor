package wrongsides.cherrypicker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypicker.controller.resource.AsteroidsResource;
import wrongsides.cherrypicker.domain.Criteria;
import wrongsides.cherrypicker.domain.collections.Asteroids;
import wrongsides.cherrypicker.service.AsteroidsService;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsteroidsControllerTest {

    private AsteroidsController asteroidsController;
    private ObjectMapper objectMapper;

    @Mock
    private AsteroidsService asteroidsService;
    @Mock
    private ObjectMapper mockObjectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        asteroidsController = new AsteroidsController(asteroidsService, mockObjectMapper);
    }

    @Test
    public void post_givenAsteroids_callsSortOnAsteroids() throws Exception {
        String body = FileUtils.readFileToString(new File("src/test/resources/AsteroidsRequestBody.json"), StandardCharsets.UTF_8);
        Asteroids asteroids = objectMapper.readValue(body, Asteroids.class);
        when(mockObjectMapper.readValue(anyString(), eq(Asteroids.class))).thenReturn(asteroids);

        AsteroidsResource asteroidsResource = asteroidsController.post(body);

        verify(asteroidsService).sortByValue(asteroids.getAsteroids(), Criteria.VALUE);
        assertThat(asteroidsResource.getAsteroids()).hasSize(3);
        assertThat(asteroidsResource.getLinks()).hasSize(2);
    }

    @Test
    public void post_givenNull_throwsException() throws Exception {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> asteroidsController.post(null));
    }
}