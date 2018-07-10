package wrongsides.cherrypickor.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Order;

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

    public Optional<BigDecimal> getMaxBuyOrderFor(String typeId, String regionId) {
        String url = String.format("%s/%s/markets/%s/orders/?datasource=%s&order_type=buy&type_id=%s",
                config.getEsiUrl(), config.getEsiVersion(), regionId, config.getEsiDatasource(), typeId);
        ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {});
        List<Order> orders = responseEntity.getBody();
        if (orders != null && !orders.isEmpty()) {
            orders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
            return Optional.of(orders.get(0).getPrice());
        } else {
            return Optional.empty();
        }
    }
}
