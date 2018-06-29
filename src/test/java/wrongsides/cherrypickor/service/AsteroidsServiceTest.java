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
import wrongsides.cherrypickor.repository.ItemRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    private List<Asteroid> asteroids;
    @Mock
    private Item item;

    @Before
    public void setUp() {
        asteroidsService = new AsteroidsService(idRepository, itemRepository, valuationService);
    }

    @Test
    public void getAsteroid_givenItemWithName_returnsAsteroidOfName() {
        String asteroidName = "Veldspar";
        when(itemRepository.getByName(anyString())).thenReturn(item);
        when(item.getName()).thenReturn(asteroidName);

        Asteroid veldspar = asteroidsService.getAsteroid(asteroidName);

        verify(itemRepository).getByName(asteroidName);
        verify(item).getName();
        assertThat(veldspar.getName()).isEqualTo(asteroidName);
    }

    @Test
    public void sortByValue_givenAsteroids_appraiseEachAndSortByValueDescending() {
        Asteroid spod1 = Asteroid.of("Bright Spodumain").withQuantity(100).build();
        Asteroid spod2 = Asteroid.of("Bright Spodumain").withQuantity(90).build();
        Asteroid crock = Asteroid.of("Sharp Crokite").withQuantity(50).build();
        List<Asteroid> asteroids = Arrays.asList(spod1, spod2, crock);
        when(idRepository.findRegionId(anyString())).thenReturn("regionId");
        when(itemRepository.getByName(anyString())).thenReturn(item);
        when(item.getTypeId()).thenReturn("itemTypeId");
        when(valuationService.appraise(anyString(), anyString(), eq(100))).thenReturn(new BigDecimal("100"));
        when(valuationService.appraise(anyString(), anyString(), eq(90))).thenReturn(new BigDecimal("900"));
        when(valuationService.appraise(anyString(), anyString(), eq(50))).thenReturn(new BigDecimal("500"));

        asteroidsService.sortByValue(asteroids);

        verify(itemRepository, times(2)).getByName("Bright Spodumain");
        verify(itemRepository).getByName("Sharp Crokite");
        verify(valuationService).appraise("itemTypeId", "regionId", 100);
        verify(valuationService).appraise("itemTypeId", "regionId", 90);
        verify(valuationService).appraise("itemTypeId", "regionId", 50);
        assertThat(asteroids).containsExactly(spod2, crock, spod1);
    }

    @Test
    public void sortByValue_givenAsteroidWithNoItem_doesNotAppraiseAndSetsValueToZero() {
        Asteroid spod1 = Asteroid.of("Bright Spodumain").withQuantity(100).build();
        Asteroid spod2 = Asteroid.of("Bright Spodumain").withQuantity(90).build();
        Asteroid crock = Asteroid.of("Sharp Crokite").withQuantity(50).build();
        Asteroid noItem = Asteroid.of("noItem").withQuantity(60).build();
        List<Asteroid> asteroids = Arrays.asList(spod1, spod2, crock, noItem);
        when(idRepository.findRegionId(anyString())).thenReturn("regionId");
        when(itemRepository.getByName("noItem")).thenReturn(null);
        when(itemRepository.getByName("Bright Spodumain")).thenReturn(item);
        when(itemRepository.getByName("Sharp Crokite")).thenReturn(item);
        when(item.getTypeId()).thenReturn("itemTypeId");
        when(valuationService.appraise(anyString(), anyString(), eq(100))).thenReturn(new BigDecimal("100"));
        when(valuationService.appraise(anyString(), anyString(), eq(90))).thenReturn(new BigDecimal("900"));
        when(valuationService.appraise(anyString(), anyString(), eq(50))).thenReturn(new BigDecimal("500"));

        asteroidsService.sortByValue(asteroids);

        verify(valuationService, never()).appraise("itemTypeId", "regionId", 60);
        assertThat(asteroids).containsExactly(spod2, crock, spod1, noItem);
        assertThat(asteroids.get(3).getValue()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void sortByValue_givenAsteroidsWithNoRegion_doesNothing() {
        when(idRepository.findRegionId(anyString())).thenReturn(null);

        asteroidsService.sortByValue(asteroids);

        verifyZeroInteractions(asteroids);
        verifyZeroInteractions(itemRepository);
        verifyZeroInteractions(valuationService);
    }

    @Test
    public void parseScannerOutput_givenScannerOutput_returnsAsteroids() throws ParseException {
        String scannerOutput = "Bright Spodumain\t2,829\t45,264 m3\t217 km\nSharp Crokite\t10,015\t160,240 m3\t234 km";
        Asteroid expectedSpod = Asteroid.of("Bright Spodumain").withQuantity(2829).withVolume(new Measure(45264, Unit.CUBIC_METRES)).withDistance(new Measure(217, Unit.KILOMETRES)).build();
        Asteroid expectedCrok = Asteroid.of("Sharp Crokite").withQuantity(10015).withVolume(new Measure(160240, Unit.CUBIC_METRES)).withDistance(new Measure(234, Unit.KILOMETRES)).build();

        List<Asteroid> asteroids = asteroidsService.parseScannerOutput(scannerOutput);

        assertThat(asteroids.get(0)).isEqualToComparingFieldByFieldRecursively(expectedSpod);
        assertThat(asteroids.get(1)).isEqualToComparingFieldByFieldRecursively(expectedCrok);
    }

    @Test
    public void parseScannerOutput_givenNull_throwsException() {
        assertThatExceptionOfType(ParseException.class)
                .isThrownBy(() -> asteroidsService.parseScannerOutput("Boom!\tBoom!"));
    }
}