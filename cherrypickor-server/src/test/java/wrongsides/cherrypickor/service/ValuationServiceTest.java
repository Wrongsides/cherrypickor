package wrongsides.cherrypickor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Order;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

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
    public void appraise_givenAsteroidOrderPrice10AndQuantity2_returns20() {
        Order order = new Order("system-id", BigDecimal.TEN);
        order.setCreated(ZonedDateTime.now());
        when(priceRepository.getOrders(anyString(), anyString())).thenReturn(Collections.singletonList(order));

        BigDecimal value = valuationService.appraise("asteroidId", "regionId", 2);

        verify(priceRepository).getOrders("asteroidId", "regionId");
        assertThat(value).isEqualTo(BigDecimal.valueOf(20L));
    }

    @Test
    public void appraise_givenAsteroidOrderCreatedMoreThanADayAgo_removesFromCacheAndGetsAgain() {
        Order order = new Order("system-id", BigDecimal.TEN);
        order.setCreated(ZonedDateTime.now().minus(1, ChronoUnit.DAYS));
        when(priceRepository.getOrders(anyString(), anyString())).thenReturn(Collections.singletonList(order));

        BigDecimal value = valuationService.appraise("asteroidId", "regionId", 2);

        InOrder inOrder = Mockito.inOrder(priceRepository);
        inOrder.verify(priceRepository).getOrders("asteroidId", "regionId");
        inOrder.verify(priceRepository).removeOrders("asteroidId", "regionId");
        inOrder.verify(priceRepository).getOrders("asteroidId", "regionId");
        assertThat(value).isEqualTo(BigDecimal.valueOf(20L));
    }

    @Test
    public void appraise_givenAsteroidWithNoOrdersAndQuantity2_returns0() {
        BigDecimal value = valuationService.appraise("asteroidId", "regionId", 2);
        assertThat(value).isEqualTo(BigDecimal.ZERO);
    }
}