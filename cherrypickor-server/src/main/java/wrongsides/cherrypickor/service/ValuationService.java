package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Order;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ValuationService {

    private PriceRepository priceRepository;

    public ValuationService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    BigDecimal appraise(String typeId, String regionId, int quantity) {
        return getMaxBuyOrderFor(typeId, regionId)
                .orElse(BigDecimal.ZERO)
                .multiply(new BigDecimal(quantity));
    }

    private Optional<BigDecimal> getMaxBuyOrderFor(String typeId, String regionId) {
        List<Order> orders = priceRepository.getOrders(typeId, regionId);
        orders = refreshOrders(typeId, regionId, orders);
        if (orders != null && !orders.isEmpty()) {
            return Optional.of(orders.get(0).getPrice());
        } else {
            return Optional.empty();
        }
    }

    private List<Order> refreshOrders(String typeId, String regionId, List<Order> orders) {
        if (orders != null && !orders.isEmpty()) {
            Order maxBuyOrder = orders.get(0);
            if (maxBuyOrder.getCreated().toInstant().isBefore(Instant.now().minus(1, ChronoUnit.DAYS))) {
                priceRepository.removeOrders(typeId, regionId);
                orders = priceRepository.getOrders(typeId, regionId);
            }
        }
        return orders;
    }
}
