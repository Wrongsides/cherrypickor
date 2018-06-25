package wrongsides.cherrypickor.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wrongsides.cherrypickor.adapter.EsiAdapter;
import wrongsides.cherrypickor.config.CacheConfig;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CacheConfig.class, ItemRepository.class})
public class ItemRepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @MockBean
    private EsiAdapter esiAdapter;

    @Before
    public void setUp() {
        itemRepository.removeAll();
    }

    @Test
    public void getByName_cachesRequestWithKeyOfName() {
        itemRepository.getByName("asteroid");
        itemRepository.getByName("asteroid");

        verify(esiAdapter, times(1)).findByName("asteroid");
        verifyNoMoreInteractions(esiAdapter);
    }

    @Test
    public void getByTypeId_putRequestInCacheWithKeyOfName() {
        Item item = new Item(Category.INVENTORY_TYPE, "asteroid");
        when(esiAdapter.find(anyString())).thenReturn(item);

        itemRepository.getByTypeId("typeId");
        itemRepository.getByName("asteroid");

        verify(esiAdapter, times(1)).find("typeId");
        verifyNoMoreInteractions(esiAdapter);
    }

    @Test
    public void removeAll_emptysItemCache() {
        itemRepository.getByName("asteroid");
        itemRepository.getByName("another-item");

        itemRepository.removeAll();

        itemRepository.getByName("asteroid");
        itemRepository.getByName("another-item");

        verify(esiAdapter, times(4)).findByName(anyString());
        verifyNoMoreInteractions(esiAdapter);
    }
}