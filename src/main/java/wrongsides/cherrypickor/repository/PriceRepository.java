package wrongsides.cherrypickor.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.MarketOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PriceRepository {

    private Config config;
    private RestTemplate restTemplate;

    public PriceRepository(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public Optional<BigDecimal> getMaxBuyOrderFor(String asteroidId, String regionId) {
        String url = String.format("%s/%s/markets/%s/orders/?datasource=%s&order_type=buy&type_id=%s",
                config.getEsiUrl(), config.getEsiVersion(), regionId, config.getEsiDatasource(), asteroidId);
        ResponseEntity<List<MarketOrder>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketOrder>>() {});
        List<MarketOrder> marketOrders = responseEntity.getBody();
        if (marketOrders != null) {
            marketOrders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
            return Optional.of(marketOrders.get(0).getPrice());
        } else {
            return Optional.empty();
        }
    }
}
