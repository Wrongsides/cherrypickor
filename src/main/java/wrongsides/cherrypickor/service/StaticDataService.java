package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.repository.IdRepository;

import java.util.LinkedList;
import java.util.List;
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

            List<String> typeIds = new LinkedList<>();
            idRepository.getGroupIds(categoryId)
                    .forEach(groupId -> typeIds.addAll(idRepository.getTypeIds(groupId)));

            CompletableFuture[] items = new CompletableFuture[typeIds.size()];
            for (int i = 0; i < typeIds.size(); i++) {
                items[i] = itemService.getByTypeId(typeIds.get(i));
            }
            CompletableFuture.allOf(items).join();
            return "Asteroid item static data refreshed";
        }
        return "Failed to clear cache, Asteroid item static data has not been refreshed";
    }
}
