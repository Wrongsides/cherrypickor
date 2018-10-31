package wrongsides.cherrypickor.repository;

import org.assertj.core.util.Lists;
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
import wrongsides.cherrypickor.domain.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceRepositoryTest {

    @Mock
    private Config config;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity<List<Order>> responseEntity;

    private PriceRepository priceRepository;

    @Before
    public void setUp() {
        priceRepository = new PriceRepository(config, restTemplate);
    }

    @Test
    public void getOrders_returnsMaxPriceMarketOrderWithCreatedDate() {
        Order minOrder = new Order("1", new BigDecimal("3050.60"));
        Order maxOrder = new Order("1", new BigDecimal("3050.67"));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<Order>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(Lists.newArrayList(minOrder, maxOrder));

        List<Order> repositoryOrders = priceRepository.getOrders("17466", "10000002");

        assertThat(repositoryOrders).containsExactly(maxOrder, minOrder);
        assertThat(repositoryOrders).extracting("created").doesNotContainNull();
    }

    @Test
    public void getOrders_givenNoMarketOrders_returnsEmpty() {
        List<Order> orders = new ArrayList<>();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<Order>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(orders);

        List<Order> repositoryOrders = priceRepository.getOrders("no", "orders");

        assertThat(repositoryOrders).isEmpty();
    }

    @Test
    public void getOrders_givenNullMarketOrders_returnsEmpty() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(new ParameterizedTypeReference<List<Order>>() {}))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(null);

        List<Order> repositoryOrders = priceRepository.getOrders("no", "orders");

        assertThat(repositoryOrders).isEmpty();
    }
}