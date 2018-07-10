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
import wrongsides.cherrypickor.adapter.Category;
import wrongsides.cherrypickor.domain.Item;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        Item item = new Item(Category.INVENTORY_TYPE, "asteroid");
        item.setTypeId("type-id");
        when(esiAdapter.find(anyString(), any(Category.class))).thenReturn(Optional.of(item.getTypeId()));

        Item asteroid = itemRepository.getByName("asteroid");
        Item cachedAsteroid = itemRepository.getByName("asteroid");

        verify(esiAdapter, times(1)).find("asteroid", Category.INVENTORY_TYPE);
        verifyNoMoreInteractions(esiAdapter);
        assertThat(asteroid).isEqualToComparingFieldByField(item);
        assertThat(cachedAsteroid).isEqualToComparingFieldByField(item);
    }

    @Test
    public void getByTypeId_putRequestInCacheWithKeyOfName() {
        Item item = new Item(Category.INVENTORY_TYPE, "asteroid");
        item.setTypeId("type-id");
        when(esiAdapter.findItem(anyString(), any(Category.class))).thenReturn(Optional.of(item));

        Item asteroid = itemRepository.getByTypeId("type-id");
        Item cachedAsteroid = itemRepository.getByName("asteroid");

        verify(esiAdapter, times(1)).findItem("type-id", Category.TYPES);
        verifyNoMoreInteractions(esiAdapter);
        assertThat(asteroid).isEqualToComparingFieldByField(item);
        assertThat(cachedAsteroid).isEqualToComparingFieldByField(item);
    }

    @Test
    public void removeAll_emptysItemCache() {
        itemRepository.getByName("asteroid");
        itemRepository.getByName("another-item");

        itemRepository.removeAll();

        itemRepository.getByName("asteroid");
        itemRepository.getByName("another-item");

        verify(esiAdapter, times(4)).find(anyString(), any(Category.class));
        verifyNoMoreInteractions(esiAdapter);
    }
}