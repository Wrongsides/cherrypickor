package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StaticDataService {

    private static final String ROOT_ASTEROID = "Dark Ochre";
    private IdRepository idRepository;
    private ItemService itemService;

    public StaticDataService(IdRepository idRepository, ItemService itemService) {
        this.idRepository = idRepository;
        this.itemService = itemService;
    }

    public String refreshAsteroidStaticData() {
        if (itemService.removeAll()) {
            Item rootAsteroid = new Item(Category.INVENTORY_TYPE, ROOT_ASTEROID);
            rootAsteroid.setTypeId(idRepository.findTypeId(rootAsteroid));
            rootAsteroid.setGroupId(idRepository.findGroupId(rootAsteroid));
            rootAsteroid.setCategoryId(idRepository.findCategoryId(rootAsteroid));

            List<String> typeIds = new LinkedList<>();
            idRepository.getGroupIds(rootAsteroid.getCategoryId())
                    .forEach((String groupId) -> typeIds.addAll(idRepository.getTypeIds(groupId)));

            if (!typeIds.isEmpty()) {
                CompletableFuture[] items = new CompletableFuture[typeIds.size()];
                for (int i = 0; i < typeIds.size(); i++) {
                    items[i] = itemService.getByTypeId(typeIds.get(i));
                }
                CompletableFuture.allOf(items).join();
            }
            return "Asteroid item static data refreshed.";
        }
        return "Failed to clear cache, Asteroid item static data has not been refreshed.";
    }
}
