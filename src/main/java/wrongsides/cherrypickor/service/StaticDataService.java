package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class StaticDataService {

    private IdRepository idRepository;
    private ItemService itemService;

    public StaticDataService(IdRepository idRepository, ItemService itemService) {
        this.idRepository = idRepository;
        this.itemService = itemService;
    }

    public String refreshItemStaticData(String rootItemName) {
        if (itemService.removeAll()) {
            String rootTypeId = idRepository.findTypeId(rootItemName);
            String rootGroupId = idRepository.findGroupId(rootTypeId);
            String categoryId = idRepository.findCategoryId(rootGroupId);

            Set<String> typeIds = new LinkedHashSet<>();
            idRepository.getGroupIds(categoryId)
                    .forEach(groupId -> typeIds.addAll(idRepository.getTypeIds(groupId)));

            CompletableFuture[] items = typeIds.stream()
                    .map(typeId -> itemService.getByTypeId(typeId))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(items).join();
            return "Asteroid item static data refreshed";
        }
        return "Failed to clear cache, Asteroid item static data has not been refreshed";
    }
}
