package wrongsides.cherrypickor.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.domain.MarketOrder;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class PriceRepository {

    private RestTemplate restTemplate;

    public PriceRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getMaxBuyOrderFor(String asteroidId, String regionId) {
        String url = String.format("https://esi.tech.ccp.is/latest/markets/%s/orders/?datasource=tranquility&order_type=buy&type_id=%s", regionId, asteroidId);
        ResponseEntity<List<MarketOrder>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketOrder>>() {});
        List<MarketOrder> marketOrders = responseEntity.getBody();
        if (marketOrders != null) {
            marketOrders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
        }
        return marketOrders.get(0).getPrice();
    }
}
