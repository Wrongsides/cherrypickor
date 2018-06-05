package wrongsides.cherrypickor.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Asteroid;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.domain.Measure;
import wrongsides.cherrypickor.domain.Unit;
import wrongsides.cherrypickor.repository.IdRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsteroidsServiceTest {

    private AsteroidsService asteroidsService;

    @Mock
    private IdRepository idRepository;
    @Mock
    private ValuationService valuationService;

    @Before
    public void setUp() {
        asteroidsService = new AsteroidsService(idRepository, valuationService);
    }

    @Test
    public void sortByValue_givenAsteroids_appraiseEachAndSortByValueDescending() {
        Asteroid spod1 = new Asteroid("Bright Spodumain", 100, null, null, BigDecimal.ONE);
        Asteroid spod2 = new Asteroid("Bright Spodumain", 90, null, null, BigDecimal.TEN);
        Asteroid crock = new Asteroid("Sharp Crokite", 50, null, null, BigDecimal.TEN);
        List<Asteroid> asteroids = Arrays.asList(spod1, spod2, crock);
        when(idRepository.findRegionId(anyString())).thenReturn(Optional.of("regionId"));
        when(idRepository.findItemTypeId(any(Item.class))).thenReturn(Optional.of("itemTypeId"));
        when(valuationService.appraise(anyString(), anyString(), eq(100))).thenReturn(new BigDecimal("100"));
        when(valuationService.appraise(anyString(), anyString(), eq(90))).thenReturn(new BigDecimal("900"));
        when(valuationService.appraise(anyString(), anyString(), eq(50))).thenReturn(new BigDecimal("500"));

        asteroidsService.sortByValue(asteroids);

        assertThat(asteroids).containsExactly(spod2, crock, spod1);
    }

    @Test
    public void parseScannerOutput_givenScannerOutput_returnsAsteroids() throws IOException {
        String scannerOutput = "Bright Spodumain\t2,829\t45,264 m3\t217 km\nSharp Crokite\t10,015\t160,240 m3\t234 km";
        Asteroid expectedSpod = new Asteroid("Bright Spodumain", 2829, new Measure(45264, Unit.CUBIC_METRES), new Measure(217, Unit.KILOMETRES), null);
        Asteroid expectedCrok = new Asteroid("Sharp Crokite", 10015, new Measure(160240, Unit.CUBIC_METRES), new Measure(234, Unit.KILOMETRES), null);

        List<Asteroid> asteroids = asteroidsService.parseScannerOutput(scannerOutput);

        assertThat(asteroids.get(0)).isEqualToComparingFieldByFieldRecursively(expectedSpod);
        assertThat(asteroids.get(1)).isEqualToComparingFieldByFieldRecursively(expectedCrok);
    }
}