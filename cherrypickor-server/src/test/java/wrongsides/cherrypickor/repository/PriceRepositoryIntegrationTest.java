package wrongsides.cherrypickor.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import wrongsides.cherrypickor.config.CacheConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CacheConfig.class, PriceRepository.class})
public class PriceRepositoryIntegrationTest {

    @Autowired
    private PriceRepository priceRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void getOrders() {

    }

    @Test
    public void removeOrders() {

    }
}