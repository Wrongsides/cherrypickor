package wrongsides.cherrypickor.service;

import org.springframework.stereotype.Service;
import wrongsides.cherrypickor.domain.Category;
import wrongsides.cherrypickor.domain.Item;
import wrongsides.cherrypickor.repository.IdRepository;
import wrongsides.cherrypickor.repository.ItemRepository;

import java.util.List;

@Service
public class StaticDataService {

    private static final String ROOT_ASTEROID = "Dark Ochre";
    private IdRepository idRepository;
    private ItemRepository itemRepository;

    public StaticDataService(IdRepository idRepository, ItemRepository itemRepository) {
        this.idRepository = idRepository;
        this.itemRepository = itemRepository;
    }

    public void loadStaticData() {
        Item rootAsteroid = new Item(Category.INVENTORY_TYPE, ROOT_ASTEROID);
        rootAsteroid.setTypeId(idRepository.findItemTypeId(rootAsteroid));
        idRepository.setGroupId(rootAsteroid);
        idRepository.setCategoryId(rootAsteroid);

        idRepository.setGroupIds(rootAsteroid.getCategoryId()).forEach(groupId -> {
            List<String> typeIds = idRepository.setTypeIds(groupId);
            if(!typeIds.isEmpty()){
                typeIds.forEach(typeId -> itemRepository.getByTypeId(typeId));
            }
        });
    }
}
