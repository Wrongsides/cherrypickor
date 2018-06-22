package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.IdRepository;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StaticDataService {

    private static final String ROOT_ASTEROID = "Dark Ochre";
    private IdRepository idRepository;
    private ItemRepository itemRepository;

    public StaticDataService(IdRepository idRepository, ItemRepository itemRepository) {
        this.idRepository = idRepository;
        this.itemRepository = itemRepository;
    }

    public void refreshAsteroidStaticData() {
        itemRepository.removeAll();

        Item rootAsteroid = new Item(Category.INVENTORY_TYPE, ROOT_ASTEROID);
        rootAsteroid.setTypeId(idRepository.findItemTypeId(rootAsteroid));
        idRepository.setGroupId(rootAsteroid);
        idRepository.setCategoryId(rootAsteroid);

        List<String> typeIds = new LinkedList<>();
        idRepository.setGroupIds(rootAsteroid.getCategoryId())
                .forEach((String groupId) -> typeIds.addAll(idRepository.setTypeIds(groupId)));

        if (!typeIds.isEmpty()) {
            CompletableFuture[] items = new CompletableFuture[typeIds.size()];
            for (int i = 0; i < typeIds.size(); i++) {
                items[i] = getItemsByTypeId(typeIds.get(i));
            }
            CompletableFuture.allOf(items).join();
        }
    }

    private CompletableFuture<Item> getItemsByTypeId(String typeId) {
        return CompletableFuture.supplyAsync(() -> itemRepository.getByTypeId(typeId));
    }
}
