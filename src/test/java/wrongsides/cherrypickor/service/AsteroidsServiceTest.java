package wrongsides.cherrypickor.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.domain.collections.Asteroids;
import wrongsides.cherrypickor.repository.IdRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsteroidsServiceTest {

    private AsteroidsService asteroidsService;
    private ObjectMapper objectMapper;

    @Mock
    private IdRepository idRepository;
    @Mock
    private ValuationService valuationService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        asteroidsService = new AsteroidsService(idRepository, valuationService);
    }

    @Test
    public void sortByValue_givenAsteroids_appraiseEachAndSortByValueDescending() {
        Asteroid spod1 = new Asteroid();
        spod1.setName("Bright Spodumain");
        spod1.setQuantity(100);
        spod1.setValue(BigDecimal.ONE);
        Asteroid spod2 = new Asteroid();
        spod2.setName("Bright Spodumain");
        spod2.setQuantity(90);
        spod2.setValue(BigDecimal.TEN);
        Asteroid crock = new Asteroid();
        crock.setName("Sharp Crokite");
        crock.setQuantity(50);
        crock.setValue(BigDecimal.TEN);
        List<Asteroid> asteroids = Arrays.asList(spod1, spod2, crock);
        when(idRepository.findRegion(anyString())).thenReturn(Optional.of("regionId"));
        when(idRepository.findItemTypeId(anyString())).thenReturn(Optional.of("itemTypeId"));
        when(valuationService.appraise(anyString(), anyString(), eq(100), any(Criteria.class))).thenReturn(new BigDecimal("100"));
        when(valuationService.appraise(anyString(), anyString(), eq(90), any(Criteria.class))).thenReturn(new BigDecimal("900"));
        when(valuationService.appraise(anyString(), anyString(), eq(50), any(Criteria.class))).thenReturn(new BigDecimal("500"));

        asteroidsService.sortByValue(asteroids, Criteria.VALUE);

        assertThat(asteroids).containsExactly(spod2, crock, spod1);
    }

    @Test
    public void parseScannerOutput_givenScannerOutput_returnsAsteroids() throws IOException {
        String body = FileUtils.readFileToString(new File("src/test/resources/SurveyScannerOutputTest"), StandardCharsets.UTF_8);
        Asteroids expectedAsteroids = objectMapper.readValue(FileUtils.readFileToString(new File("src/test/resources/AsteroidsRequestBody.json"), StandardCharsets.UTF_8), Asteroids.class);
        List<Asteroid> asteroidsAsteroids = expectedAsteroids.getAsteroids();

        List<Asteroid> asteroids = asteroidsService.parseScannerOutput(body);

        assertThat(asteroids).isNotNull();
    }
}