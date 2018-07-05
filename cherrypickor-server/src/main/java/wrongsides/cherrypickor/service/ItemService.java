package wrongsides.cherrypickor.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    boolean removeAll() {
        return itemRepository.removeAll();
    }

    @Async
    CompletableFuture<Item> getByTypeId(String typeId) {
        return CompletableFuture.completedFuture(itemRepository.getByTypeId(typeId));
    }
}
