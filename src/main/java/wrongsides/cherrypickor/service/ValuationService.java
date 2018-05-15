package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;

@Service
public class ValuationService {

    private PriceRepository priceRepository;

    public ValuationService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BigDecimal appraise(String asteroidId, String regionId, int quantity, Criteria valuationCriteria) {
        return priceRepository.getMaxBuyOrderFor(asteroidId, regionId)
                .orElse(BigDecimal.ZERO)
                .multiply(new BigDecimal(quantity));
    }
}
