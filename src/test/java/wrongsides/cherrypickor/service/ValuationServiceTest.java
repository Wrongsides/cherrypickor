package wrongsides.cherrypickor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValuationServiceTest {

    private ValuationService valuationService;

    @Mock
    private PriceRepository priceRepository;

    @Before
    public void setUp() {
        valuationService = new ValuationService(priceRepository);
    }

    @Test
    public void appraise_givenAsteroidPrice10AndQuantity2_returns20() {
        when(priceRepository.getMaxBuyOrderFor(anyString(), anyString())).thenReturn(Optional.of(BigDecimal.TEN));

        BigDecimal value = valuationService.appraise("asteroidId", "regionId", 2, Criteria.VALUE);

        verify(priceRepository).getMaxBuyOrderFor("asteroidId", "regionId");
        assertThat(value).isEqualTo(BigDecimal.valueOf(20L));
    }

    @Test
    public void appraise_givenAsteroidWithNoPriceAndQuantity2_returns0() {
        when(priceRepository.getMaxBuyOrderFor(anyString(), anyString())).thenReturn(Optional.empty());

        BigDecimal value = valuationService.appraise("asteroidId", "regionId", 2, Criteria.VALUE);

        verify(priceRepository).getMaxBuyOrderFor("asteroidId", "regionId");
        assertThat(value).isEqualTo(BigDecimal.ZERO);
    }
}