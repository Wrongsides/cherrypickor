package wrongsides.cherrypickor.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.environment.Config;
import wrongsides.cherrypickor.domain.Order;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class PriceRepository {

    private Config config;
    private RestTemplate restTemplate;

    public PriceRepository(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "orders")
    public List<Order> getOrders(String typeId, String regionId) {
        String url = String.format("%s/%s/markets/%s/orders/?datasource=%s&order_type=buy&type_id=%s",
                config.getEsiUrl(), config.getEsiVersion(), regionId, config.getEsiDatasource(), typeId);
        ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
        });
        List<Order> orders = responseEntity.getBody();
        if (orders == null) {
            return Collections.emptyList();
        } else {
            orders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
            orders.forEach(order -> order.setCreated(ZonedDateTime.now()));
            return orders;
        }
    }

    @CacheEvict(value = "orders")
    public void removeOrders(String typeId, String regionId) {
        //remove orders from cache
    }
}
