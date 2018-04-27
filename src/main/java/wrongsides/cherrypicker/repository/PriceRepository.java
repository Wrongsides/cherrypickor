package wrongsides.cherrypicker.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypicker.domain.MarketOrder;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class PriceRepository {

    public BigDecimal getMaxBuyOrderFor(String asteroidId, String regionId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://esi.tech.ccp.is/latest/markets/%s/orders/?datasource=tranquility&order_type=buy&type_id=%s", regionId, asteroidId);
        List<MarketOrder> marketOrders = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<MarketOrder>>() {}).getBody();
        marketOrders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
        return marketOrders.get(0).getPrice();
    }
}
