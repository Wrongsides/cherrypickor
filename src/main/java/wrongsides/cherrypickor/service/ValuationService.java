package wrongsides.cherrypickor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Criteria;
import wrongsides.cherrypickor.repository.IdRepository;
import wrongsides.cherrypickor.repository.PriceRepository;

import java.math.BigDecimal;

@Service
public class ValuationService {

    private PriceRepository priceRepository;
    private IdRepository idRepository;

    @Autowired
    public ValuationService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BigDecimal appraise(String asteroidId, String regionId, int quantity, Criteria valuationCriteria) {
        return priceRepository.getMaxBuyOrderFor(asteroidId, regionId).multiply(new BigDecimal(quantity));
    }
}
