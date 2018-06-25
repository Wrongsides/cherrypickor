package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;

@Service
public class ValuationService {

    private PriceRepository priceRepository;

    public ValuationService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    BigDecimal appraise(String typeId, String regionId, int quantity) {
        return priceRepository.getMaxBuyOrderFor(typeId, regionId)
                .orElse(BigDecimal.ZERO)
                .multiply(new BigDecimal(quantity));
    }
}
