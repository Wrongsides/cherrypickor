package wrongsides.cherrypickor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        itemService = new ItemService(itemRepository);
    }

    @Test
    public void removeAll_delegatesToItemRepository() {
        itemService.removeAll();
        verify(itemRepository).removeAll();
    }

    @Test
    public void getByTypeId_givenInventoryTypeId_returnsCompletedFutureOfItem() {
        Item item = new Item();
        when(itemRepository.getByTypeId(anyString())).thenAnswer(invocation -> {
            Thread.sleep(100L);
            return item;
        });

        CompletableFuture<Item> itemCompletableFuture = itemService.getByTypeId("type-id");

        verify(itemRepository).getByTypeId("type-id");
        assertThat(itemCompletableFuture).isCompletedWithValue(item);
    }
}