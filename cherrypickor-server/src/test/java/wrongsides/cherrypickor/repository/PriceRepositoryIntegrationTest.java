package wrongsides.cherrypickor.repository;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.config.CacheConfig;
import wrongsides.cherrypickor.domain.Order;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CacheConfig.class, PriceRepository.class, EsiAdapter.class})
public class PriceRepositoryIntegrationTest {

    @Autowired
    private PriceRepository priceRepository;

    @MockBean
    private EsiAdapter esiAdapter;

    @Before
    public void setUp() {
        priceRepository.removeAll();
    }

    @Test
    public void getOrders_cachesRequestWithKeyOfTypeIdRegionId() {
        Order order = new Order("systemId", BigDecimal.TEN);
        when(esiAdapter.getOrders(anyString(), anyString())).thenReturn(Lists.newArrayList(order));

        List<Order> orders = priceRepository.getOrders("typeId", "regionId");
        List<Order> cachedOrders = priceRepository.getOrders("typeId", "regionId");

        verify(esiAdapter, times(1)).getOrders("typeId", "regionId");
        verifyNoMoreInteractions(esiAdapter);
        assertThat(orders).isEqualTo(cachedOrders);
    }

    @Test
    public void removeOrders_evictsOrdersFromCache() {

        priceRepository.getOrders("typeId", "regionId");
        priceRepository.removeOrders("typeId", "regionId");
        priceRepository.getOrders("typeId", "regionId");

        verify(esiAdapter, times(2)).getOrders("typeId", "regionId");
        verifyNoMoreInteractions(esiAdapter);
    }

    @Test
    public void removeAll_evictsAllOrdersFromCache() {

        priceRepository.getOrders("typeId", "regionId");
        priceRepository.getOrders("anotherTypeId", "anotherRegionId");
        priceRepository.removeAll();
        priceRepository.getOrders("typeId", "regionId");
        priceRepository.getOrders("anotherTypeId", "anotherRegionId");

        verify(esiAdapter, times(2)).getOrders("typeId", "regionId");
        verify(esiAdapter, times(2)).getOrders("anotherTypeId", "anotherRegionId");
        verifyNoMoreInteractions(esiAdapter);
    }
}