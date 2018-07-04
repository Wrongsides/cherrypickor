package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.MarketOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceRepositoryTest {

    @Mock
    private Config config;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity<List<MarketOrder>> responseEntity;

    private PriceRepository priceRepository;

    @Before
    public void setUp() {
        priceRepository = new PriceRepository(config, restTemplate);
    }

    @Test
    public void getMaxBuyOrderFor_returnsTheHighestMarketOrderPrice() {
        List<MarketOrder> marketOrders = new ArrayList<>();
        marketOrders.add(new MarketOrder("1", new BigDecimal("3050.67")));
        marketOrders.add(new MarketOrder("1", new BigDecimal("3050.60")));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<MarketOrder>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(marketOrders);

        Optional<BigDecimal> maxBuyOrder = priceRepository.getMaxBuyOrderFor("17466", "10000002");

        assertThat(maxBuyOrder.get()).isEqualTo(new BigDecimal("3050.67"));
    }

    @Test
    public void getMaxBuyOrderFor_givenNoMarketOrders_returnsEmpty() {
        List<MarketOrder> marketOrders = new ArrayList<>();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<MarketOrder>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(marketOrders);

        Optional<BigDecimal> maxBuyOrder = priceRepository.getMaxBuyOrderFor("no", "orders");

        assertThat(maxBuyOrder).isEmpty();
    }

    @Test
    public void getMaxBuyOrderFor_givenNullMarketOrders_returnsEmpty() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<MarketOrder>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(null);

        Optional<BigDecimal> maxBuyOrder = priceRepository.getMaxBuyOrderFor("no", "orders");

        assertThat(maxBuyOrder).isEmpty();
    }
}