package wrongsides.cherrypickor.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.domain.Order;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class PriceRepository {

    private EsiAdapter esiAdapter;

    public PriceRepository(EsiAdapter esiAdapter) {
        this.esiAdapter = esiAdapter;
    }

    @Cacheable(value = "orders")
    public List<Order> getOrders(String typeId, String regionId) {
        List<Order> orders = esiAdapter.getOrders(typeId, regionId);
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
