package wrongsides.cherrypickor.repository;


import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceRepositoryTest {

    private PriceRepository priceRepository = new PriceRepository();

    @Test
    public void getMaxBuyOrderFor_() {
        assertThat(priceRepository.getMaxBuyOrderFor("17466","10000002")).isEqualTo(new BigDecimal("3050.67"));
    }
}