package wrongsides.cherrypicker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wrongsides.cherrypicker.domain.Criteria;
import wrongsides.cherrypicker.repository.IdRepository;
import wrongsides.cherrypicker.repository.PriceRepository;

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
