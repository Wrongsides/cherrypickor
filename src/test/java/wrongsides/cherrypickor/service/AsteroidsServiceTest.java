package wrongsides.cherrypickor.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.*;
import wrongsides.cherrypickor.repository.IdRepository;
import wrongsides.cherrypickor.repository.ItemRepository;

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
    private ItemRepository itemRepository;
    @Mock
    private ValuationService valuationService;
    @Mock
    private Item item;

    @Before
    public void setUp() {
        asteroidsService = new AsteroidsService(idRepository, itemRepository, valuationService);
    }

    @Test
    public void sortByValue_givenAsteroids_appraiseEachAndSortByValueDescending() {
        Asteroid spod1 = Asteroid.of("Bright Spodumain").withQuantity(100).withValue(BigDecimal.ONE).build();
        Asteroid spod2 = Asteroid.of("Bright Spodumain").withQuantity(90).withValue(BigDecimal.TEN).build();
        Asteroid crock = Asteroid.of("Sharp Crokite").withQuantity(50).withValue(BigDecimal.TEN).build();
        List<Asteroid> asteroids = Arrays.asList(spod1, spod2, crock);
        when(idRepository.findRegionId(anyString())).thenReturn("regionId");
        when(itemRepository.getByName(anyString())).thenReturn(item);
        when(item.getTypeId()).thenReturn("itemTypeId");
        when(valuationService.appraise(anyString(), anyString(), eq(100))).thenReturn(new BigDecimal("100"));
        when(valuationService.appraise(anyString(), anyString(), eq(90))).thenReturn(new BigDecimal("900"));
        when(valuationService.appraise(anyString(), anyString(), eq(50))).thenReturn(new BigDecimal("500"));

        asteroidsService.sortByValue(asteroids);

        assertThat(asteroids).containsExactly(spod2, crock, spod1);
    }

    @Test
    public void parseScannerOutput_givenScannerOutput_returnsAsteroids() throws IOException {
        String scannerOutput = "Bright Spodumain\t2,829\t45,264 m3\t217 km\nSharp Crokite\t10,015\t160,240 m3\t234 km";
        Asteroid expectedSpod = Asteroid.of("Bright Spodumain").withQuantity(2829).withVolume(new Measure(45264, Unit.CUBIC_METRES)).withDistance(new Measure(217, Unit.KILOMETRES)).build();
        Asteroid expectedCrok = Asteroid.of("Sharp Crokite").withQuantity(10015).withVolume(new Measure(160240, Unit.CUBIC_METRES)).withDistance(new Measure(234, Unit.KILOMETRES)).build();

        List<Asteroid> asteroids = asteroidsService.parseScannerOutput(scannerOutput);

        assertThat(asteroids.get(0)).isEqualToComparingFieldByFieldRecursively(expectedSpod);
        assertThat(asteroids.get(1)).isEqualToComparingFieldByFieldRecursively(expectedCrok);
    }
}