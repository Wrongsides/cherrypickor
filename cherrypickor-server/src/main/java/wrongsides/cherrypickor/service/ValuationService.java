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
        if (orders.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(refreshOrders(typeId, regionId, orders).get(0).getPrice());
        }
    }

    private List<Order> refreshOrders(String typeId, String regionId, List<Order> orders) {
        Order maxBuyOrder = orders.get(0);
        if (maxBuyOrder.getCreated().toInstant().isBefore(Instant.now().minus(1, ChronoUnit.DAYS))) {
            priceRepository.removeOrders(typeId, regionId);
            return priceRepository.getOrders(typeId, regionId);
        } else {
            return orders;
        }
    }
}
